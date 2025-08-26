package pt.ulisboa.tecnico.tuplespaces.server.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.grpc.Status.CANCELLED;
import static io.grpc.Status.INVALID_ARGUMENT;
import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.TupleSpacesReplicasGrpc;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest;
import pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse;
import pt.ulisboa.tecnico.tuplespaces.server.domain.ServerState;

public class TuplespacesServiceImpl extends TupleSpacesReplicasGrpc.TupleSpacesReplicasImplBase {
    /** Tuplespaces implementation. */

    private static final String REQUEST_CLIENT = "Client request";
    private static final String REQUEST_FROM = "Request from ";
    private static final String REQUEST_PUT = " -> PUT : Tuple = ";
    private static final String REQUEST_READ = " -> READ : Tuple = ";
    private static final String REQUEST_TAKE = " -> TAKE : Tuple = ";
    private static final String REQUEST_GET_TUPLE_SPACES_STATE = " -> GET_TUPLE_SPACES_STATE";
    private static final String RESPONSE_SERVER_FEEDBACK = "Operation complete: ";
    private static final String OPERATION_CANCELLED = "Operation cancelled: ";
    private static final String OPERATION_SUCCESSFUL = "SUCCESS";
    private static final String DEBUG = "[DEBUG] ";
    private static final String TUPLE = " | Tuple = ";
    private static final String REQUEST_NR = " | #request = ";
    private static final String CLIENT_ID = " Client ID = ";
    private static final String INVALID_CLIENT_INPUT_ERROR = "invalid input format from the client.";
    private static final String INVALID_DELAY = "invalid delay value: ";
    private static final String REQUEST_INTERRUPTED = "request interrupted on the server.";
    private static final String REQUEST_WAITING = "waiting for previous requests on client ";

	private ServerState tuplespaces = new ServerState();
    private final ConcurrentHashMap<Integer, AtomicInteger> clientRequests;

    private boolean debugFlag;

    public TuplespacesServiceImpl(boolean debugFlag){
        this.debugFlag = debugFlag;
        this.clientRequests = new ConcurrentHashMap<>();
    }

    // Helper method to print debug messages
    private void debug(String debugMessage) {
        if (this.debugFlag) {
            System.err.println(DEBUG + debugMessage);
        }
    }

    private void delay(String delayValue) throws  NumberFormatException, InterruptedException {
        debug("Delaying execution for " + delayValue + " s");
        int sleepTime = Integer.parseInt(delayValue);
        if (sleepTime > 0) { Thread.sleep(sleepTime*1000); }
    }

    private AtomicInteger getClientRequests(int clientId) {
        return clientRequests.computeIfAbsent(clientId, k -> new AtomicInteger(-1));
    }

    private int getLastCompletedRequest(int clientId) { 
        return clientRequests.get(clientId).get();
    }

    private void incrementRequestNumber(AtomicInteger clientReqs) { 
        synchronized (clientReqs) {
            clientReqs.incrementAndGet();
            clientReqs.notifyAll();
        }
    }

    private void waitForPreviousRequest(int clientId, int requestNr, AtomicInteger clientReqs){
        while (getLastCompletedRequest(clientId) != requestNr - 1){ // check if the last completed request was the previous
            try {
                synchronized (clientReqs) {
                    debug(REQUEST_WAITING + clientId + " -> request " + requestNr);
                    clientReqs.wait(); // Wait for previous requests to be completed
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <T> boolean checkMetadata(StreamObserver<T> responseObserver) {
        String delayValue = HeaderServerInterceptor.DELAY_CONTEXT_KEY.get();

        if (delayValue != null) {
            try {
                delay(delayValue);
            } catch (InterruptedException e) {
                debug(OPERATION_CANCELLED + REQUEST_INTERRUPTED);
                responseObserver.onError(CANCELLED
                                .withDescription(REQUEST_INTERRUPTED)
                                .withCause(e)
                                .asRuntimeException());
                return false; // Execution should stop due to error
            } catch (NumberFormatException e) {
                debug(OPERATION_CANCELLED + INVALID_DELAY + delayValue);
                responseObserver.onError(CANCELLED
                                .withDescription(INVALID_DELAY + delayValue)
                                .withCause(e)
                                .asRuntimeException());
                return false; // Execution should stop due to error
            }
        }
        return true; // Delay applied successfully
    }

    @Override
    public void put(PutReplicaRequest request, StreamObserver<PutReplicaResponse> responseObserver){
        int clientId = request.getClientId(); 
        int requestNr = request.getRequestNr();
        debug(REQUEST_FROM + CLIENT_ID + clientId + REQUEST_NR + requestNr);
        AtomicInteger clientReqs = getClientRequests(clientId);
        waitForPreviousRequest(clientId, requestNr, clientReqs);
        
        if (!checkMetadata(responseObserver)) { return; }

        String tuple = request.getNewTuple();
        debug(REQUEST_CLIENT + REQUEST_PUT + tuple);
        tuplespaces.put(tuple);

        incrementRequestNumber(clientReqs);

        debug(RESPONSE_SERVER_FEEDBACK + OPERATION_SUCCESSFUL + CLIENT_ID + clientId + REQUEST_NR + requestNr);
        PutReplicaResponse response = PutReplicaResponse.newBuilder().build();
        // Send a single response through the stream.
        responseObserver.onNext(response);
        // Notify the client that the operation has been completed.
        responseObserver.onCompleted();
    }

    @Override
    public void read(ReadReplicaRequest request, StreamObserver<ReadReplicaResponse> responseObserver){
        int clientId = request.getClientId(); 
        int requestNr = request.getRequestNr();
        debug(REQUEST_FROM + CLIENT_ID + clientId + REQUEST_NR + requestNr);
        AtomicInteger clientReqs = getClientRequests(clientId);
        waitForPreviousRequest(clientId, requestNr, clientReqs);

        if (!checkMetadata(responseObserver)) { return; }
        
        String searchPattern = request.getSearchPattern();
        debug(REQUEST_CLIENT + REQUEST_READ + searchPattern);
        String result = tuplespaces.read(searchPattern);
        boolean success = (result != null);

        incrementRequestNumber(clientReqs);
        
        if (!success) {
            debug(RESPONSE_SERVER_FEEDBACK + INVALID_CLIENT_INPUT_ERROR);
			responseObserver.onError(INVALID_ARGUMENT.withDescription(INVALID_CLIENT_INPUT_ERROR)
                            .asRuntimeException());
		} else {
            debug(RESPONSE_SERVER_FEEDBACK + OPERATION_SUCCESSFUL + TUPLE + result  + CLIENT_ID + clientId + REQUEST_NR + requestNr);
			ReadReplicaResponse response = ReadReplicaResponse.newBuilder().setResult(result).build();
			// Send a single response through the stream.
			responseObserver.onNext(response);
			// Notify the client that the operation has been completed.
			responseObserver.onCompleted();
		}
    }

    @Override
    public void take(TakeReplicaRequest request, StreamObserver<TakeReplicaResponse> responseObserver){
        int clientId = request.getClientId(); 
        int requestNr = request.getRequestNr();
        debug(REQUEST_FROM + CLIENT_ID + clientId + REQUEST_NR + requestNr);
        AtomicInteger clientReqs = getClientRequests(clientId);
        waitForPreviousRequest(clientId, requestNr, clientReqs);

        if (!checkMetadata(responseObserver)) { return; }

        String tuple = request.getTuple();
        tuplespaces.releaseTuples(request.getReleaseTuplesList());
        debug(REQUEST_CLIENT + REQUEST_TAKE + tuple);
        String result = tuplespaces.take(tuple, request.getUnlock());

        incrementRequestNumber(clientReqs);
        
        debug(RESPONSE_SERVER_FEEDBACK + OPERATION_SUCCESSFUL + TUPLE + result + CLIENT_ID + clientId + REQUEST_NR + requestNr);
        TakeReplicaResponse response = TakeReplicaResponse.newBuilder().setResult(result).build();
        // Send a single response through the stream.
        responseObserver.onNext(response);
        // Notify the client that the operation has been completed.
        responseObserver.onCompleted();
    }

    @Override
    public void getTupleSpacesState(getTupleSpacesStateReplicaRequest request, 
        StreamObserver<getTupleSpacesStateReplicaResponse> responseObserver) {
        int clientId = request.getClientId(); 
        int requestNr = request.getRequestNr();
        debug(REQUEST_FROM + CLIENT_ID + clientId + REQUEST_NR + requestNr);
        AtomicInteger clientReqs = getClientRequests(clientId);
        waitForPreviousRequest(clientId, requestNr, clientReqs);

        debug(REQUEST_CLIENT + REQUEST_GET_TUPLE_SPACES_STATE);
        List<String> result = tuplespaces.getTupleSpacesState();
        debug(RESPONSE_SERVER_FEEDBACK + OPERATION_SUCCESSFUL + TUPLE + result + CLIENT_ID + clientId + REQUEST_NR +requestNr);
        
        incrementRequestNumber(clientReqs);
        
        getTupleSpacesStateReplicaResponse response = getTupleSpacesStateReplicaResponse.newBuilder().addAllTuple(result).build();

        // Send a single response through the stream.
        responseObserver.onNext(response);
        // Notify the client that the operation has been completed.
        responseObserver.onCompleted();  
    }

    @Override
    public void lock(LockReplicaRequest request, StreamObserver<LockReplicaResponse> responseObserver) {
        String searchPattern = request.getSearchPattern();
        int clientId = request.getClientId();
        List<String> matchingTuples = new ArrayList<>();

        debug("Lock requested by client Id :" + clientId);

        try {
            matchingTuples = tuplespaces.lockTuples(searchPattern, clientId);
        } catch (InterruptedException e) {
            debug(OPERATION_CANCELLED + REQUEST_INTERRUPTED);
            responseObserver.onError(CANCELLED
                            .withDescription(REQUEST_INTERRUPTED)
                            .withCause(e)
                            .asRuntimeException());
        }

        debug("Lock acquired by client: " + clientId);

        LockReplicaResponse response = LockReplicaResponse.newBuilder().addAllTuple(matchingTuples).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void unlock(UnlockReplicaRequest request, StreamObserver<UnlockReplicaResponse> responseObserver) {
        List<String> tuplesToUnlock = request.getTuplesToUnlockList();
        int clientId = request.getClientId();
        debug("Unlock requested by client Id :" + clientId);
        tuplespaces.releaseTuples(tuplesToUnlock);
        debug("Unlock acquired by client Id: " + clientId);
        UnlockReplicaResponse response = UnlockReplicaResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
