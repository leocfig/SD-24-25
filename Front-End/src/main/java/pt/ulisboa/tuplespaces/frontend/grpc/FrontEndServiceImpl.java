package pt.ulisboa.tecnico.tuplespaces.frontend.grpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.Status;
import static io.grpc.Status.INVALID_ARGUMENT;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.PutRequest;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.PutResponse;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.ReadRequest;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.ReadResponse;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.TakeRequest;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.TakeResponse;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.TupleSpacesGrpc;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.getTupleSpacesStateRequest;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.getTupleSpacesStateResponse;
import pt.ulisboa.tecnico.tuplespaces.frontend.ResponseCollector;
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



public class FrontEndServiceImpl extends TupleSpacesGrpc.TupleSpacesImplBase {

    private static class ClientRequestInfo {
        int lastRequest = 0;
        Queue<Integer> pendingTakeRequests = new ConcurrentLinkedQueue<>();
    }    

    private static final String REQUEST_CLIENT = ">> Client request: ";
    private static final String REQUEST_PUT = "PUT Tuple = ";
    private static final String REQUEST_READ = "READ Tuple = ";
    private static final String REQUEST_TAKE = "TAKE Tuple = ";
    private static final String RESPONSE_FRONTEND_FEEDBACK = "Operation complete: ";
    private static final String INVALID_CLIENT_INPUT_ERROR = "invalid input format from the client.";
    private static final String ENTER_CRITICAL_SECTION = "Received permission from all voters. Entering critical section.";
    private static final String REQUEST_GET_TUPLE_SPACES_STATE = "GET_TUPLE_SPACES_STATE";
    private static final String REQUEST_DELAY = " -> the servers should apply the following delays: ";
    private static final String RESPONSE_SERVER = "<< Response from Server: ";
    private static final String REQUEST_INTERRUPTED = "request interrupted.";
    private static final String TUPLE = " | Tuple = ";
    private static final String REQUEST_NR = " | #request = ";
    private static final String CLIENT_ID = " Client ID = ";
    private static final String DEBUG = "[DEBUG] ";
    private static final String OPERATION_SUCCESSFUL = "SUCCESS";
    private static final String OPERATION_FAIL = "INVALID TUPLE";
    private static final String DELAY = "delay";
    private static final String SPACE = " ";
    private static final String BGN_TUPLE = "<";
    private static final String END_TUPLE = ">";
    private final long INITIAL_DELAY = 200; // Start backoff from 200ms

    static final Metadata.Key<String> DELAY_KEY_1 = Metadata.Key.of(DELAY, Metadata.ASCII_STRING_MARSHALLER);
    static final Metadata.Key<String> DELAY_KEY_2 = Metadata.Key.of(DELAY, Metadata.ASCII_STRING_MARSHALLER);
    static final Metadata.Key<String> DELAY_KEY_3 = Metadata.Key.of(DELAY, Metadata.ASCII_STRING_MARSHALLER);

    private final TupleSpacesReplicasGrpc.TupleSpacesReplicasStub[] asyncStubs;
    private final TupleSpacesReplicasGrpc.TupleSpacesReplicasBlockingStub[] blockingStub;
    private final ManagedChannel[] channels;
    private final ConcurrentHashMap<Integer, ClientRequestInfo> clientRequests;
    private final boolean debugFlag;
    private final int numServers;
    private final int quorumSize;

    public FrontEndServiceImpl(String[] hostPorts, boolean debugFlag, int numServers) {
        this.channels = new ManagedChannel[numServers];
        this.asyncStubs = new TupleSpacesReplicasGrpc.TupleSpacesReplicasStub[numServers];
        this.blockingStub = new TupleSpacesReplicasGrpc.TupleSpacesReplicasBlockingStub[numServers];
        this.clientRequests = new ConcurrentHashMap<>();
        this.numServers = numServers;
        this.quorumSize = (int) Math.ceil(Math.sqrt(numServers));
        this.debugFlag = debugFlag;

        for (int i = 0; i < numServers; i++) {
            String newTarget = hostPorts[i];
            this.channels[i] = ManagedChannelBuilder.forTarget(newTarget).usePlaintext().build();
            this.asyncStubs[i] = TupleSpacesReplicasGrpc.newStub(this.channels[i]);
            this.blockingStub[i] = TupleSpacesReplicasGrpc.newBlockingStub(this.channels[i]);
        }
    }

    // Helper method to print debug messages
    private void debug(String debugMessage) {
        if (this.debugFlag) {
            System.err.println(DEBUG + debugMessage);
        }
    }

    // Shuts down all the channels
    public void shutDown() {
        for (ManagedChannel channel : channels) {
            channel.shutdown();
        }
    }

    // Computes the voter set for a given client based on its ID
    private List<Integer> getVoterSet(int clientId) {
        return Arrays.asList(clientId % 3, (clientId + 1) % 3);
    }

    private String determineChosenTuple(Map<Integer, List<String>> mapOfLists) {
        Iterator<List<String>> iterator = mapOfLists.values().iterator();
        // Initialize the set with the first list
        Set<String> commonTuples = new HashSet<>(iterator.next());
        // Intersect with the remaining lists
        while (iterator.hasNext()) {
            commonTuples.retainAll(iterator.next());
        }
        // No common tuple found
        if (commonTuples.isEmpty()) return null;
        return commonTuples.iterator().next(); // Return any common element
    }

    private int getNextRequestId(int clientId) {
        ClientRequestInfo info = clientRequests.get(clientId);
        if (clientRequests.containsKey(clientId)) {
            info.lastRequest += 1;
            return info.lastRequest;
        } else { // Initialize if new Client ID
            clientRequests.put(clientId, new ClientRequestInfo());
            return 0;
        }
    }

    // Add take request to queue
    private void addTakeRequest(int clientId, int requestNr) {
        clientRequests.get(clientId).pendingTakeRequests.add(requestNr); 
        debug("add take request client ID " + clientId + " ; request: " + requestNr);
    }

    // Remove oldest take request from queue
    private void completeTakeRequest(int clientId, int requestNr){  
        Queue<Integer> pendingTakeRequests = clientRequests.get(clientId).pendingTakeRequests;
        synchronized (pendingTakeRequests) {
           pendingTakeRequests.poll(); 
           pendingTakeRequests.notifyAll();
        }
        debug("complete take request on client ID " + clientId + " ; request: " + requestNr);
    }

    // Wait for previous take requests to complete
    private void waitForPreviousTake(int clientId, int requestNr){
        Queue<Integer> pendingTakeRequests = clientRequests.get(clientId).pendingTakeRequests;
        synchronized (pendingTakeRequests) {
            while (true) {
                Integer pending = pendingTakeRequests.peek();
                if (pending == null || pending >= requestNr) break;
                try {
                    debug("waiting on client ID " + clientId + " ; request: " + requestNr);
                    pendingTakeRequests.wait(); 
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // Initiates a request for access following Maekawa's algorithm
    public String requestAccess(int clientId, String searchPattern, StreamObserver<TakeResponse> responseObserver,
                                ResponseCollector collector) {

        List<Integer> voters = getVoterSet(clientId);
        debug("Client " + clientId + " mapped to voter set: " + voters);
        String chosenTuple = null;
        long retryDelay = INITIAL_DELAY;

        // To keep sending requests until a common tuple is found
        while(chosenTuple == null) {
            // Notifies the relevant voters that access is being requested
            for (int voter : voters) {
                try {
                    sendLockRequest(voter, clientId, searchPattern, collector);
                } catch (Throwable e) {
                    responseObserver.onError(Status.fromThrowable(e)
                                .withDescription(e.getMessage())
                                .asRuntimeException());
                }
            }

            try {
                // Wait for all responses from the voting set
                collector.waitUntilAllReceived(quorumSize, 1);
                debug(ENTER_CRITICAL_SECTION);
            } catch (InterruptedException e) {
                responseObserver.onError(Status.INTERNAL
                                .withDescription(REQUEST_INTERRUPTED)
                                .asRuntimeException());
            }

            // Determine a common tuple from the lists
            chosenTuple = determineChosenTuple(collector.getCollectedLists());
            debug("ClientID: " + clientId + " | Chosen tuple: " + chosenTuple);

            if (chosenTuple == null) {
                // copy the matching tuple lists from each voter
                Map<Integer, List<String>> retryCollectedList = 
                                           collector.getCollectedLists()
                                                    .entrySet()
                                                    .stream()
                                                    .collect(Collectors.toMap(
                                                        Map.Entry::getKey, 
                                                        entry -> new ArrayList<>(entry.getValue())
                                                    ));
                releaseLocks(voters, clientId, retryCollectedList, responseObserver);
                collector.reset();
                // Apply exponential backoff before retrying
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException e) {
                    responseObserver.onError(Status.INTERNAL
                                    .withDescription(REQUEST_INTERRUPTED)
                                    .asRuntimeException());
                }
                // Increase delay for next retry
                retryDelay = retryDelay * 2;
            }
        }
        return chosenTuple;
    }

    public void releaseLocks(List<Integer> voters, int clientId, Map<Integer, List<String>> retryCollectedList,
                             StreamObserver<TakeResponse> responseObserver) {   
        for (int voter : voters) {
            List<String> tuplesToUnlock;
            tuplesToUnlock = retryCollectedList.get(voter);
            try {
                sendUnlockRequest(voter, clientId, tuplesToUnlock);
            } catch (Throwable e) {
                responseObserver.onError(Status.fromThrowable(e)
                                .withDescription(e.getMessage())
                                .asRuntimeException());
            }
        }
    }

    private void sendLockRequest(int voter, int clientId, String searchPattern, 
                                 ResponseCollector collector) {

        // Create a lock request message
        LockReplicaRequest request = LockReplicaRequest.newBuilder()
                                        .setSearchPattern(searchPattern)
                                        .setClientId(clientId)
                                        .build();

        // Send the request to the selected voter replica
        asyncStubs[voter].lock(request, new StreamObserver<LockReplicaResponse>() {
            @Override
            public void onNext(LockReplicaResponse response) {
                List<String> modifiableList = new ArrayList<>(response.getTupleList());
                collector.addVoterList(modifiableList, voter);
            }

            @Override
            public void onError(Throwable throwable) {  
                debug("Lock operation failed: " + throwable);
                throw new RuntimeException(throwable);
            }

            @Override
            public void onCompleted() {
            }
        });

        debug("Sent lock request to voter " + voter + " for client " + clientId);
    }

    private void sendUnlockRequest(int voter, int clientId, List<String> tuplesToUnlock) {

        UnlockReplicaRequest request = UnlockReplicaRequest.newBuilder()
                                                           .setClientId(clientId)
                                                           .addAllTuplesToUnlock(tuplesToUnlock)
                                                           .build();

        // Send the request to the selected voter replica
        asyncStubs[voter].unlock(request, new StreamObserver<UnlockReplicaResponse>() {
            @Override
            public void onNext(UnlockReplicaResponse response) {}

            @Override
            public void onError(Throwable throwable) { 
                debug("Unlock operation failed: " + throwable);
                throw new RuntimeException(throwable);
            }

            @Override
            public void onCompleted() {
            }
        });

        debug("Sent unlock request to voter " + voter);
    }

    // Creates an array of Metadata objects, each containing a delay value for a specific replica
    private Metadata[] createMetadataObjects(String delayReplica1, String delayReplica2, String delayReplica3) {
        Metadata metadata1 = new Metadata();
        Metadata metadata2 = new Metadata();
        Metadata metadata3 = new Metadata();
        metadata1.put(DELAY_KEY_1, delayReplica1);
        metadata2.put(DELAY_KEY_2, delayReplica2);
        metadata3.put(DELAY_KEY_3, delayReplica3);
        return new Metadata[]{metadata1, metadata2, metadata3};
    }

    // Determines which gRPC stub to use based on the client request
    private TupleSpacesReplicasGrpc.TupleSpacesReplicasStub[] determineStubs(String delayValue1, String delayValue2, String delayValue3) {
        
        // If no delay value was specified by the client
        if (delayValue1 == null && delayValue2 == null && delayValue3 == null) {
            return this.asyncStubs;     // Returns the default asynchronous stubs
        }
        //Otherwise, returns stubs with the new metadata
        debug(REQUEST_CLIENT + REQUEST_DELAY +  delayValue1 + ";" + delayValue2 + ";" + delayValue3 + " s");
        Metadata[] metadataObjects = createMetadataObjects(delayValue1, delayValue2, delayValue3);
        TupleSpacesReplicasGrpc.TupleSpacesReplicasStub[] stubsWithHeaders = new TupleSpacesReplicasGrpc.TupleSpacesReplicasStub[metadataObjects.length];

        // Create a stub for each metadata object
        for (int i = 0; i < metadataObjects.length; i++) {
            stubsWithHeaders[i] = this.asyncStubs[i].withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadataObjects[i]));
        }

        return stubsWithHeaders;
    }

    @Override     
    public void put(PutRequest request, StreamObserver<PutResponse> responseObserver) {

        String delayValue1 = HeaderFrontendInterceptor.DELAY_CONTEXT_KEY_1.get();
        String delayValue2 = HeaderFrontendInterceptor.DELAY_CONTEXT_KEY_2.get();
        String delayValue3 = HeaderFrontendInterceptor.DELAY_CONTEXT_KEY_3.get();

        TupleSpacesReplicasGrpc.TupleSpacesReplicasStub[] stubs = determineStubs(delayValue1, delayValue2, delayValue3);

        String requestedTuple = request.getNewTuple();
        int clientId = request.getClientId(); 
        int requestNr = getNextRequestId(clientId);
        debug(REQUEST_CLIENT + CLIENT_ID + clientId + REQUEST_NR +requestNr);
        debug(REQUEST_CLIENT + REQUEST_PUT + requestedTuple);

        if (!tupleIsValid(requestedTuple)) {
            debug(RESPONSE_FRONTEND_FEEDBACK + INVALID_CLIENT_INPUT_ERROR);
			responseObserver.onError(INVALID_ARGUMENT.withDescription(INVALID_CLIENT_INPUT_ERROR)
                            .asRuntimeException());
            return;
        }
        else { // Send the response right away to the client before forwarding the request to the server
            PutResponse response = PutResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        ResponseCollector collector = new ResponseCollector();
        PutReplicaRequest putReplicaRequest = PutReplicaRequest.newBuilder().setClientId(clientId).setRequestNr(requestNr).setNewTuple(requestedTuple).build();

        waitForPreviousTake(clientId, requestNr);

        for (TupleSpacesReplicasGrpc.TupleSpacesReplicasStub stub : stubs) {
            stub.put(putReplicaRequest, new StreamObserver<PutReplicaResponse>() {
                @Override
                public void onNext(PutReplicaResponse response) {
                    collector.addString(OPERATION_SUCCESSFUL);
                }

                @Override
                public void onError(Throwable throwable) {
                    collector.addError(throwable);
                }

                @Override
                public void onCompleted() {
                }
            });
        }

        try {
            // Wait for all responses
            collector.waitUntilAllReceived(numServers, 1);
        } catch (InterruptedException e) {
            responseObserver.onError(Status.INTERNAL
                            .withDescription(REQUEST_INTERRUPTED)
                            .asRuntimeException());
            return;
        }
        debug(RESPONSE_SERVER + OPERATION_SUCCESSFUL);
    }

    @Override
    public void read(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {

        String delayValue1 = HeaderFrontendInterceptor.DELAY_CONTEXT_KEY_1.get();
        String delayValue2 = HeaderFrontendInterceptor.DELAY_CONTEXT_KEY_2.get();
        String delayValue3 = HeaderFrontendInterceptor.DELAY_CONTEXT_KEY_3.get();

        TupleSpacesReplicasGrpc.TupleSpacesReplicasStub[] stubs = determineStubs(delayValue1, delayValue2, delayValue3);
        ResponseCollector collector = new ResponseCollector();
        ReadResponse response = null;
        Throwable error = null;
        int clientId = request.getClientId(); 
        int requestNr = getNextRequestId(clientId);
        debug(REQUEST_CLIENT + CLIENT_ID + clientId + REQUEST_NR +requestNr);
        debug(REQUEST_CLIENT + REQUEST_READ  + request.getSearchPattern());

        if (!tupleIsValid(request.getSearchPattern())) {
            debug(RESPONSE_FRONTEND_FEEDBACK + INVALID_CLIENT_INPUT_ERROR);
			responseObserver.onError(INVALID_ARGUMENT.withDescription(INVALID_CLIENT_INPUT_ERROR)
                            .asRuntimeException());
        }
        else { // Send the response right away to the client
            response = ReadResponse.newBuilder().setResult("<SD>").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void take(TakeRequest request, StreamObserver<TakeResponse> responseObserver) {
        String requestedSearchPattern = request.getSearchPattern();
        String chosenTuple = null;
        int clientId = request.getClientId(); 
        int requestNr = getNextRequestId(clientId);
        debug(REQUEST_CLIENT + CLIENT_ID + clientId + REQUEST_NR +requestNr);
        debug(REQUEST_CLIENT + REQUEST_TAKE  + requestedSearchPattern);
        ResponseCollector collectorTake = new ResponseCollector();

        String delayValue1 = HeaderFrontendInterceptor.DELAY_CONTEXT_KEY_1.get();
        String delayValue2 = HeaderFrontendInterceptor.DELAY_CONTEXT_KEY_2.get();
        String delayValue3 = HeaderFrontendInterceptor.DELAY_CONTEXT_KEY_3.get();

        TupleSpacesReplicasGrpc.TupleSpacesReplicasStub[] stubs = determineStubs(delayValue1, delayValue2, delayValue3);

        if (!tupleIsValid(requestedSearchPattern)) {
            debug(RESPONSE_FRONTEND_FEEDBACK + INVALID_CLIENT_INPUT_ERROR);
			responseObserver.onError(INVALID_ARGUMENT.withDescription(INVALID_CLIENT_INPUT_ERROR)
                            .asRuntimeException());
            return;
        }

        ResponseCollector collectorLock = new ResponseCollector(true);
        chosenTuple = requestAccess(clientId, requestedSearchPattern, responseObserver, collectorLock);

        // As soon as the chosen tuple is determind, send the response right away to the client
        TakeResponse response = TakeResponse.newBuilder().setResult(chosenTuple).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        addTakeRequest(clientId, requestNr);
        for (int i = 0; i < numServers; i++) {
            TupleSpacesReplicasGrpc.TupleSpacesReplicasStub stub = stubs[i];

            List<String> replicaTuples = collectorLock.getVoterList(i);
            if (replicaTuples == null) replicaTuples = new ArrayList<>();
            // If isVoter, the replica should unlock the chosen tuple
            boolean isVoter = !replicaTuples.isEmpty();
            replicaTuples.remove(chosenTuple);  // Remove the chosen tuple

            TakeReplicaRequest takeReplicaRequest = TakeReplicaRequest.newBuilder().setClientId(clientId).setRequestNr(requestNr)
                                                    .setTuple(chosenTuple).addAllReleaseTuples(replicaTuples).setUnlock(isVoter).build();

            stub.take(takeReplicaRequest, new StreamObserver<TakeReplicaResponse>() {
                @Override
                public void onNext(TakeReplicaResponse response) {
                    collectorTake.addString(response.getResult());
                }

                @Override
                public void onError(Throwable throwable) {
                    collectorTake.addError(throwable);
                }

                @Override
                public void onCompleted() {
                }
            });
        }

        try { 
            // Wait for all responses
            collectorTake.waitUntilAllReceived(numServers, 1);
            completeTakeRequest(clientId, requestNr);
            debug(RESPONSE_SERVER + OPERATION_SUCCESSFUL + TUPLE + chosenTuple);
        }  catch (InterruptedException e) {
            responseObserver.onError(Status.INTERNAL
                            .withDescription(REQUEST_INTERRUPTED)
                            .asRuntimeException());
        }
    }

    @Override
    public void getTupleSpacesState(getTupleSpacesStateRequest request, 
        StreamObserver<getTupleSpacesStateResponse> responseObserver) {

        List<String> allTuples = new ArrayList<>();
        getTupleSpacesStateReplicaResponse responseReplica = null;
        getTupleSpacesStateResponse finalResponse = null;
        int clientId = request.getClientId(); 
        int requestNr = getNextRequestId(clientId);
        debug(REQUEST_CLIENT + CLIENT_ID + clientId + REQUEST_NR +requestNr);
        debug(REQUEST_CLIENT + REQUEST_GET_TUPLE_SPACES_STATE);

        getTupleSpacesStateReplicaRequest getReplicaRequest = getTupleSpacesStateReplicaRequest.newBuilder().setClientId(clientId).setRequestNr(requestNr).build();

        try {
            for (int i = 0; i < numServers; i++) {
                responseReplica = blockingStub[i].getTupleSpacesState(getReplicaRequest);
                allTuples.addAll(responseReplica.getTupleList());
            }
            finalResponse = getTupleSpacesStateResponse.newBuilder().addAllTuple(allTuples).build();
            responseObserver.onNext(finalResponse);
            responseObserver.onCompleted();
            debug(RESPONSE_SERVER + OPERATION_SUCCESSFUL + TUPLE + finalResponse.getTupleList());

        } catch (StatusRuntimeException e) {
            // Forward the exception to the client
            responseObserver.onError(e);
            debug(RESPONSE_SERVER + OPERATION_FAIL);
        }
    }

    private boolean tupleIsValid(String input){
        String[] split = input.split(SPACE);

        if (split.length != 1
            ||
            !split[0].substring(0,1).equals(BGN_TUPLE) 
            || 
            !split[0].endsWith(END_TUPLE)) {
            return false;
        }
        else {
            return true;
        }
    }
}