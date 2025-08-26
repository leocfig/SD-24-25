package pt.ulisboa.tecnico.tuplespaces.server.grpc;

import java.util.List;

import static io.grpc.Status.INVALID_ARGUMENT;
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
import pt.ulisboa.tecnico.tuplespaces.server.domain.ServerState;

public class TuplespacesServiceImpl extends TupleSpacesGrpc.TupleSpacesImplBase {
    /** Tuplespaces implementation. */

    private static final String REQUEST_CLIENT = "Client request";
    private static final String REQUEST_PUT = " -> PUT : Tuple = ";
    private static final String REQUEST_READ = " -> READ : Tuple = ";
    private static final String REQUEST_TAKE = " -> TAKE : Tuple = ";
    private static final String REQUEST_GET_TUPLE_SPACES_STATE = " -> GET_TUPLE_SPACES_STATE";
    private static final String RESPONSE_SERVER_FEEDBACK = "Operation complete: ";
    private static final String OPERATION_SUCCESSFUL = "SUCCESS";
    private static final String DEBUG = "[DEBUG] ";
    private static final String INVALID_CLIENT_INPUT_ERROR = "invalid input format from the client.";

	private ServerState tuplespaces = new ServerState();
    private boolean debugFlag;

    public TuplespacesServiceImpl(boolean debugFlag){
        this.debugFlag = debugFlag;
    }

    // Helper method to print debug messages
    private void debug(String debugMessage) {
        if (this.debugFlag) {
            System.err.println(DEBUG + debugMessage);
        }
    }

    @Override
    public void put(PutRequest request, StreamObserver<PutResponse> responseObserver){

        String tuple = request.getNewTuple();
        debug(REQUEST_CLIENT + REQUEST_PUT + tuple);
        boolean success = tuplespaces.put(tuple);

		if (!success) {
            debug(RESPONSE_SERVER_FEEDBACK + INVALID_CLIENT_INPUT_ERROR);
			responseObserver.onError(INVALID_ARGUMENT.withDescription(INVALID_CLIENT_INPUT_ERROR)
                            .asRuntimeException());
		} else {
            debug(RESPONSE_SERVER_FEEDBACK + OPERATION_SUCCESSFUL);
			PutResponse response = PutResponse.newBuilder().build();
			// Send a single response through the stream.
			responseObserver.onNext(response);
			// Notify the client that the operation has been completed.
			responseObserver.onCompleted();
		}
    }

    @Override
    public void read(ReadRequest request, StreamObserver<ReadResponse> responseObserver){
        String searchPattern = request.getSearchPattern();

        debug(REQUEST_CLIENT + REQUEST_READ + searchPattern);

        String result = tuplespaces.read(searchPattern);
        boolean success = (result != null);
        
        if (!success) {
            debug(RESPONSE_SERVER_FEEDBACK + INVALID_CLIENT_INPUT_ERROR);
			responseObserver.onError(INVALID_ARGUMENT.withDescription(INVALID_CLIENT_INPUT_ERROR)
                            .asRuntimeException());
		} else {
            debug(RESPONSE_SERVER_FEEDBACK + OPERATION_SUCCESSFUL + " | Tuple = " + result);
			ReadResponse response = ReadResponse.newBuilder().setResult(result).build();
			// Send a single response through the stream.
			responseObserver.onNext(response);
			// Notify the client that the operation has been completed.
			responseObserver.onCompleted();
		}
    }

    @Override
    public void take(TakeRequest request, StreamObserver<TakeResponse> responseObserver){
        String searchPattern = request.getSearchPattern();

        debug(REQUEST_CLIENT + REQUEST_TAKE + searchPattern);

        String result = tuplespaces.take(searchPattern);
        boolean success = (result != null);

        if (!success) {
            debug(RESPONSE_SERVER_FEEDBACK + INVALID_CLIENT_INPUT_ERROR);
			responseObserver.onError(INVALID_ARGUMENT.withDescription(INVALID_CLIENT_INPUT_ERROR)
                            .asRuntimeException());
		} else {
            debug(RESPONSE_SERVER_FEEDBACK + OPERATION_SUCCESSFUL + " | Tuple = " + result);
			TakeResponse response = TakeResponse.newBuilder().setResult(result).build();
			// Send a single response through the stream.
			responseObserver.onNext(response);
			// Notify the client that the operation has been completed.
			responseObserver.onCompleted();
		}
    }

    @Override
    public void getTupleSpacesState(getTupleSpacesStateRequest request, 
        StreamObserver<getTupleSpacesStateResponse> responseObserver) {

        debug(REQUEST_CLIENT + REQUEST_GET_TUPLE_SPACES_STATE);
        List<String> result = tuplespaces.getTupleSpacesState();
        debug(RESPONSE_SERVER_FEEDBACK + OPERATION_SUCCESSFUL + " | Tuple = " + result);

        getTupleSpacesStateResponse response = getTupleSpacesStateResponse.newBuilder().addAllTuple(result).build();

        // Send a single response through the stream.
        responseObserver.onNext(response);
        // Notify the client that the operation has been completed.
        responseObserver.onCompleted();
    }

}
