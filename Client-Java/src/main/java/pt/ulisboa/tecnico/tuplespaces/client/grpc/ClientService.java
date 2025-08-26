package pt.ulisboa.tecnico.tuplespaces.client.grpc;

import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.PutRequest;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.PutResponse;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.ReadRequest;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.ReadResponse;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.TakeRequest;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.TakeResponse;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.TupleSpacesGrpc;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.getTupleSpacesStateRequest;
import pt.ulisboa.tecnico.tuplespaces.centralized.contract.getTupleSpacesStateResponse;

public class ClientService {

    private static final String CLIENT_PREFIX = "Client ";
    private static final String REQUEST_PUT = " -> PUT request: Tuple = ";
    private static final String REQUEST_READ = " -> READ request: Tuple = ";
    private static final String REQUEST_TAKE = " -> TAKE request: Tuple = ";
    private static final String REQUEST_GET_TUPLE_SPACES_STATE = " -> GET_TUPLE_SPACES_STATE request";
    private static final String REQUEST_DELAY = " -> requested the servers to apply the following delays: ";
    private static final String RESPONSE_SERVER = "Response from Server: ";
    private static final String SHUTDOWN_MESSAGE = "Channel is shutting down...";
    private static final String SUCCESS = "OK";
    private static final String OPERATION_SUCCESSFUL = "SUCCESS";
    private static final String DEBUG = "[DEBUG] ";
    private static final Metadata.Key<String> DELAY_KEY_1 = Metadata.Key.of("delay1", Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata.Key<String> DELAY_KEY_2 = Metadata.Key.of("delay2", Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata.Key<String> DELAY_KEY_3 = Metadata.Key.of("delay3", Metadata.ASCII_STRING_MARSHALLER);
    
    private final ManagedChannel channel;
    private final TupleSpacesGrpc.TupleSpacesBlockingStub blockingStub;
    int clientId;
    private boolean debugFlag;

    public ClientService(String hostPort, int clientId, boolean debugFlag) {
		this.channel = ManagedChannelBuilder.forTarget(hostPort).usePlaintext().build();
        this.blockingStub = TupleSpacesGrpc.newBlockingStub(this.channel);
        this.clientId = clientId;
        this.debugFlag = debugFlag;
    }

    // Getter to access stub
    public TupleSpacesGrpc.TupleSpacesBlockingStub getBlockingStub() {
        return this.blockingStub;
    }

    // Getter to access client ID
    public int getClientId() {
        return this.clientId;
    }

    // Shutdown method to properly close the channel
    public void shutdown() {
        debug(SHUTDOWN_MESSAGE);
        this.channel.shutdown();
    }

    // Helper method to print debug messages
    public void debug(String debugMessage) {
        if (this.debugFlag) {
            System.err.println(DEBUG + debugMessage);
        }
    }

    // Displays the operation success outcome
    private void displaySuccess() {
        System.out.println(SUCCESS);
    }

    // Creates a Metadata object containing delay values for the replicas
    private Metadata createMetadata(String delayReplica1, String delayReplica2, String delayReplica3) {
        Metadata metadata = new Metadata();
        metadata.put(DELAY_KEY_1, delayReplica1);
        metadata.put(DELAY_KEY_2, delayReplica2);
        metadata.put(DELAY_KEY_3, delayReplica3);
        return metadata;
    }

    // Determines which gRPC stub to use based on the client input
    private TupleSpacesGrpc.TupleSpacesBlockingStub determineStub(String delayReplica1, String delayReplica2, String delayReplica3) {
        
        // If no delay value was specified by the client
        if (delayReplica1.isEmpty() && delayReplica2.isEmpty() && delayReplica3.isEmpty()) {
            return this.blockingStub;     // Returns the default blocking stub
        }
        //Otherwise, returns a stub with the new metadata
        debug(CLIENT_PREFIX + this.clientId + REQUEST_DELAY + delayReplica1 + " ; " + delayReplica2 + " ; " + delayReplica3 + " s");
        Metadata metadata = createMetadata(delayReplica1, delayReplica2, delayReplica3);
        return this.blockingStub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor((metadata)));
    }

    public void put(String tuple, String delayReplica1, String delayReplica2, String delayReplica3) {

        debug(CLIENT_PREFIX + this.clientId + REQUEST_PUT + tuple);
        TupleSpacesGrpc.TupleSpacesBlockingStub stub = determineStub(delayReplica1, delayReplica2, delayReplica3);
        PutRequest request = PutRequest.newBuilder().setClientId(clientId).setNewTuple(tuple).build();
        PutResponse response = null;
        try {
            response = stub.put(request);    // Uses the determined stub, either the default one or the one with metadata attached
        } catch (StatusRuntimeException e) { // Catching exceptions from the server
            throw e;
        }
        debug(RESPONSE_SERVER + OPERATION_SUCCESSFUL); // It only reaches here if no exception occurred
        displaySuccess();
    }

    public String read(String tuple, String delayReplica1, String delayReplica2, String delayReplica3) {

        debug(CLIENT_PREFIX + this.clientId + REQUEST_READ + tuple);
        TupleSpacesGrpc.TupleSpacesBlockingStub stub = determineStub(delayReplica1, delayReplica2, delayReplica3);
        ReadRequest request = ReadRequest.newBuilder().setClientId(clientId).setSearchPattern(tuple).build();
        ReadResponse response = null;
        try {
            response = stub.read(request);   // Uses the determined stub, either the default one or the one with metadata attached
        } catch (StatusRuntimeException e) { // Catching exceptions from the server
            throw e;
        }
        debug(RESPONSE_SERVER + OPERATION_SUCCESSFUL); // It only reaches here if no exception occurred
        displaySuccess();
        return response.getResult();
    }

    public String take(String tuple, String delayReplica1, String delayReplica2, String delayReplica3) {

        debug(CLIENT_PREFIX + this.clientId + REQUEST_TAKE + tuple);
        TupleSpacesGrpc.TupleSpacesBlockingStub stub = determineStub(delayReplica1, delayReplica2, delayReplica3);
        TakeRequest request = TakeRequest.newBuilder().setClientId(clientId).setSearchPattern(tuple).build();
        TakeResponse response = null;
        try {
            response = stub.take(request);   // Uses the determined stub, either the default one or the one with metadata attached
        } catch (StatusRuntimeException e) { // Catching exceptions from the server
            throw e;
        }
        debug(RESPONSE_SERVER + OPERATION_SUCCESSFUL); // It only reaches here if no exception occurred
        displaySuccess(); 
        return response.getResult();
    }

    public List<String> getTupleSpacesState() {
        debug(CLIENT_PREFIX + this.clientId + REQUEST_GET_TUPLE_SPACES_STATE);
        getTupleSpacesStateRequest request = getTupleSpacesStateRequest.newBuilder().setClientId(clientId).build();
        getTupleSpacesStateResponse response = this.blockingStub.getTupleSpacesState(request);
        debug(RESPONSE_SERVER + OPERATION_SUCCESSFUL);
        displaySuccess(); 
        return response.getTupleList();
    }
}
