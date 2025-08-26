package pt.ulisboa.tecnico.tuplespaces.frontend.grpc;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

public class HeaderFrontendInterceptor implements ServerInterceptor {

    private static final String DEFAULT_VALUE = "0";

    static final Metadata.Key<String> DELAY_KEY_1 = Metadata.Key.of("delay1", Metadata.ASCII_STRING_MARSHALLER);
    static final Metadata.Key<String> DELAY_KEY_2 = Metadata.Key.of("delay2", Metadata.ASCII_STRING_MARSHALLER);
    static final Metadata.Key<String> DELAY_KEY_3 = Metadata.Key.of("delay3", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> DELAY_CONTEXT_KEY_1 = Context.key("delayContextKey1");
    public static final Context.Key<String> DELAY_CONTEXT_KEY_2 = Context.key("delayContextKey2");
    public static final Context.Key<String> DELAY_CONTEXT_KEY_3 = Context.key("delayContextKey3");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        final Metadata requestHeaders,
        ServerCallHandler<ReqT, RespT> next) {

            int count = 0;
    
            for (String key : requestHeaders.keys()) {
                count++;  // Each key represents a key-value pair
            }

            // The request includes 3 expected delay values, but gRPC automatically adds 3 extra metadata keys  
            // (e.g., "content-type", "grpc-accept-encoding", "user-agent")
            if (count > 6) {
                call.close(Status.INVALID_ARGUMENT.withDescription("Too many metadata values"), null);
                return new ServerCall.Listener<ReqT>() {}; // Return empty listener to terminate call
            }
            
            // Extract metadata values
            String delayValue1 = requestHeaders.get(DELAY_KEY_1);
            String delayValue2 = requestHeaders.get(DELAY_KEY_2);
            String delayValue3 = requestHeaders.get(DELAY_KEY_3);

            // If the 3 values are present
            if (delayValue1 != null && delayValue2 != null && delayValue3 != null) {
                Context context = Context.current().withValue(DELAY_CONTEXT_KEY_1, delayValue1)
                                                   .withValue(DELAY_CONTEXT_KEY_2, delayValue2)
                                                   .withValue(DELAY_CONTEXT_KEY_3, delayValue3);
                return Contexts.interceptCall(context, call, requestHeaders, next);
            }
            // If no values are present
            else if (delayValue1 == null && delayValue2 == null && delayValue3 == null) {
                return next.startCall(call, requestHeaders);
            }

            // If 1 or 2 values are missing, add default values
            Metadata newHeaders = new Metadata();
            if (delayValue1 == null) newHeaders.put(DELAY_KEY_1, DEFAULT_VALUE);
            if (delayValue2 == null) newHeaders.put(DELAY_KEY_2, DEFAULT_VALUE);
            if (delayValue3 == null) newHeaders.put(DELAY_KEY_3, DEFAULT_VALUE);

            requestHeaders.merge(newHeaders);

            return next.startCall(call, requestHeaders);
    }
}