package pt.ulisboa.tecnico.tuplespaces.server.grpc;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class HeaderServerInterceptor implements ServerInterceptor {
    
    static final Metadata.Key<String> DELAY_KEY = 
        Metadata.Key.of("delay", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> DELAY_CONTEXT_KEY = 
        Context.key("delayContextKey");


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        final Metadata requestHeaders,
        ServerCallHandler<ReqT, RespT> next) {

            String delayValue = requestHeaders.get(DELAY_KEY);
            if (delayValue != null) {
                Context context = Context.current().withValue(DELAY_CONTEXT_KEY, delayValue);
                return Contexts.interceptCall(context, call, requestHeaders, next);
            }
            return next.startCall(call, requestHeaders);
    }
}