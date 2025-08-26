package pt.ulisboa.tecnico.tuplespaces.replicated.contract;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.48.0)",
    comments = "Source: TupleSpacesReplicas.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TupleSpacesReplicasGrpc {

  private TupleSpacesReplicasGrpc() {}

  public static final String SERVICE_NAME = "pt.ulisboa.tecnico.tuplespaces.replicated.contract.TupleSpacesReplicas";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse> getLockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "lock",
      requestType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest.class,
      responseType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse> getLockMethod() {
    io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse> getLockMethod;
    if ((getLockMethod = TupleSpacesReplicasGrpc.getLockMethod) == null) {
      synchronized (TupleSpacesReplicasGrpc.class) {
        if ((getLockMethod = TupleSpacesReplicasGrpc.getLockMethod) == null) {
          TupleSpacesReplicasGrpc.getLockMethod = getLockMethod =
              io.grpc.MethodDescriptor.<pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "lock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TupleSpacesReplicasMethodDescriptorSupplier("lock"))
              .build();
        }
      }
    }
    return getLockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse> getUnlockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unlock",
      requestType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest.class,
      responseType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse> getUnlockMethod() {
    io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse> getUnlockMethod;
    if ((getUnlockMethod = TupleSpacesReplicasGrpc.getUnlockMethod) == null) {
      synchronized (TupleSpacesReplicasGrpc.class) {
        if ((getUnlockMethod = TupleSpacesReplicasGrpc.getUnlockMethod) == null) {
          TupleSpacesReplicasGrpc.getUnlockMethod = getUnlockMethod =
              io.grpc.MethodDescriptor.<pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unlock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TupleSpacesReplicasMethodDescriptorSupplier("unlock"))
              .build();
        }
      }
    }
    return getUnlockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse> getPutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "put",
      requestType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest.class,
      responseType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse> getPutMethod() {
    io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse> getPutMethod;
    if ((getPutMethod = TupleSpacesReplicasGrpc.getPutMethod) == null) {
      synchronized (TupleSpacesReplicasGrpc.class) {
        if ((getPutMethod = TupleSpacesReplicasGrpc.getPutMethod) == null) {
          TupleSpacesReplicasGrpc.getPutMethod = getPutMethod =
              io.grpc.MethodDescriptor.<pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "put"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TupleSpacesReplicasMethodDescriptorSupplier("put"))
              .build();
        }
      }
    }
    return getPutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse> getReadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "read",
      requestType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest.class,
      responseType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse> getReadMethod() {
    io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse> getReadMethod;
    if ((getReadMethod = TupleSpacesReplicasGrpc.getReadMethod) == null) {
      synchronized (TupleSpacesReplicasGrpc.class) {
        if ((getReadMethod = TupleSpacesReplicasGrpc.getReadMethod) == null) {
          TupleSpacesReplicasGrpc.getReadMethod = getReadMethod =
              io.grpc.MethodDescriptor.<pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "read"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TupleSpacesReplicasMethodDescriptorSupplier("read"))
              .build();
        }
      }
    }
    return getReadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse> getTakeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "take",
      requestType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest.class,
      responseType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse> getTakeMethod() {
    io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse> getTakeMethod;
    if ((getTakeMethod = TupleSpacesReplicasGrpc.getTakeMethod) == null) {
      synchronized (TupleSpacesReplicasGrpc.class) {
        if ((getTakeMethod = TupleSpacesReplicasGrpc.getTakeMethod) == null) {
          TupleSpacesReplicasGrpc.getTakeMethod = getTakeMethod =
              io.grpc.MethodDescriptor.<pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "take"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TupleSpacesReplicasMethodDescriptorSupplier("take"))
              .build();
        }
      }
    }
    return getTakeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse> getGetTupleSpacesStateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getTupleSpacesState",
      requestType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest.class,
      responseType = pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest,
      pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse> getGetTupleSpacesStateMethod() {
    io.grpc.MethodDescriptor<pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse> getGetTupleSpacesStateMethod;
    if ((getGetTupleSpacesStateMethod = TupleSpacesReplicasGrpc.getGetTupleSpacesStateMethod) == null) {
      synchronized (TupleSpacesReplicasGrpc.class) {
        if ((getGetTupleSpacesStateMethod = TupleSpacesReplicasGrpc.getGetTupleSpacesStateMethod) == null) {
          TupleSpacesReplicasGrpc.getGetTupleSpacesStateMethod = getGetTupleSpacesStateMethod =
              io.grpc.MethodDescriptor.<pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest, pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getTupleSpacesState"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TupleSpacesReplicasMethodDescriptorSupplier("getTupleSpacesState"))
              .build();
        }
      }
    }
    return getGetTupleSpacesStateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TupleSpacesReplicasStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TupleSpacesReplicasStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TupleSpacesReplicasStub>() {
        @java.lang.Override
        public TupleSpacesReplicasStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TupleSpacesReplicasStub(channel, callOptions);
        }
      };
    return TupleSpacesReplicasStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TupleSpacesReplicasBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TupleSpacesReplicasBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TupleSpacesReplicasBlockingStub>() {
        @java.lang.Override
        public TupleSpacesReplicasBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TupleSpacesReplicasBlockingStub(channel, callOptions);
        }
      };
    return TupleSpacesReplicasBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TupleSpacesReplicasFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TupleSpacesReplicasFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TupleSpacesReplicasFutureStub>() {
        @java.lang.Override
        public TupleSpacesReplicasFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TupleSpacesReplicasFutureStub(channel, callOptions);
        }
      };
    return TupleSpacesReplicasFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class TupleSpacesReplicasImplBase implements io.grpc.BindableService {

    /**
     */
    public void lock(pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLockMethod(), responseObserver);
    }

    /**
     */
    public void unlock(pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUnlockMethod(), responseObserver);
    }

    /**
     */
    public void put(pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPutMethod(), responseObserver);
    }

    /**
     */
    public void read(pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReadMethod(), responseObserver);
    }

    /**
     */
    public void take(pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTakeMethod(), responseObserver);
    }

    /**
     */
    public void getTupleSpacesState(pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTupleSpacesStateMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getLockMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest,
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse>(
                  this, METHODID_LOCK)))
          .addMethod(
            getUnlockMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest,
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse>(
                  this, METHODID_UNLOCK)))
          .addMethod(
            getPutMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest,
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse>(
                  this, METHODID_PUT)))
          .addMethod(
            getReadMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest,
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse>(
                  this, METHODID_READ)))
          .addMethod(
            getTakeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest,
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse>(
                  this, METHODID_TAKE)))
          .addMethod(
            getGetTupleSpacesStateMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest,
                pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse>(
                  this, METHODID_GET_TUPLE_SPACES_STATE)))
          .build();
    }
  }

  /**
   */
  public static final class TupleSpacesReplicasStub extends io.grpc.stub.AbstractAsyncStub<TupleSpacesReplicasStub> {
    private TupleSpacesReplicasStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TupleSpacesReplicasStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TupleSpacesReplicasStub(channel, callOptions);
    }

    /**
     */
    public void lock(pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unlock(pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUnlockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void put(pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void read(pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void take(pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTakeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTupleSpacesState(pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest request,
        io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTupleSpacesStateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TupleSpacesReplicasBlockingStub extends io.grpc.stub.AbstractBlockingStub<TupleSpacesReplicasBlockingStub> {
    private TupleSpacesReplicasBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TupleSpacesReplicasBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TupleSpacesReplicasBlockingStub(channel, callOptions);
    }

    /**
     */
    public pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse lock(pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLockMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse unlock(pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUnlockMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse put(pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPutMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse read(pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReadMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse take(pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTakeMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse getTupleSpacesState(pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTupleSpacesStateMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TupleSpacesReplicasFutureStub extends io.grpc.stub.AbstractFutureStub<TupleSpacesReplicasFutureStub> {
    private TupleSpacesReplicasFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TupleSpacesReplicasFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TupleSpacesReplicasFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse> lock(
        pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse> unlock(
        pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUnlockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse> put(
        pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse> read(
        pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReadMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse> take(
        pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTakeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse> getTupleSpacesState(
        pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTupleSpacesStateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOCK = 0;
  private static final int METHODID_UNLOCK = 1;
  private static final int METHODID_PUT = 2;
  private static final int METHODID_READ = 3;
  private static final int METHODID_TAKE = 4;
  private static final int METHODID_GET_TUPLE_SPACES_STATE = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TupleSpacesReplicasImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TupleSpacesReplicasImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOCK:
          serviceImpl.lock((pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaRequest) request,
              (io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.LockReplicaResponse>) responseObserver);
          break;
        case METHODID_UNLOCK:
          serviceImpl.unlock((pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaRequest) request,
              (io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.UnlockReplicaResponse>) responseObserver);
          break;
        case METHODID_PUT:
          serviceImpl.put((pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaRequest) request,
              (io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.PutReplicaResponse>) responseObserver);
          break;
        case METHODID_READ:
          serviceImpl.read((pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaRequest) request,
              (io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.ReadReplicaResponse>) responseObserver);
          break;
        case METHODID_TAKE:
          serviceImpl.take((pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaRequest) request,
              (io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.TakeReplicaResponse>) responseObserver);
          break;
        case METHODID_GET_TUPLE_SPACES_STATE:
          serviceImpl.getTupleSpacesState((pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaRequest) request,
              (io.grpc.stub.StreamObserver<pt.ulisboa.tecnico.tuplespaces.replicated.contract.getTupleSpacesStateReplicaResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TupleSpacesReplicasBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TupleSpacesReplicasBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return pt.ulisboa.tecnico.tuplespaces.replicated.contract.TupleSpacesReplicasOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TupleSpacesReplicas");
    }
  }

  private static final class TupleSpacesReplicasFileDescriptorSupplier
      extends TupleSpacesReplicasBaseDescriptorSupplier {
    TupleSpacesReplicasFileDescriptorSupplier() {}
  }

  private static final class TupleSpacesReplicasMethodDescriptorSupplier
      extends TupleSpacesReplicasBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TupleSpacesReplicasMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TupleSpacesReplicasGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TupleSpacesReplicasFileDescriptorSupplier())
              .addMethod(getLockMethod())
              .addMethod(getUnlockMethod())
              .addMethod(getPutMethod())
              .addMethod(getReadMethod())
              .addMethod(getTakeMethod())
              .addMethod(getGetTupleSpacesStateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
