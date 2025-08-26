import sys
sys.path.insert(1, '../Contract/target/generated-sources/protobuf/python')

import grpc
import TupleSpaces_pb2 as pb2
import TupleSpaces_pb2_grpc as pb2_grpc

class ClientService:
    CLIENT_PREFIX = "Client "
    REQUEST_PUT = " -> PUT request: Tuple = "
    REQUEST_READ = " -> READ request: Tuple = "
    REQUEST_TAKE = " -> TAKE request: Tuple = "
    REQUEST_GET_TUPLE_SPACES_STATE = " -> GET_TUPLE_SPACES_STATE request"
    RESPONSE_SERVER = "Response from Server: "
    SHUTDOWN_MESSAGE = "Channel is shutting down..."
    SUCCESS = "OK"
    OPERATION_SUCCESSFUL = "SUCCESS"
    DEBUG = "[DEBUG] "


    def __init__(self, host_port: str, client_id: int, debug_flag: bool):
        self.channel = grpc.insecure_channel(host_port)
        self.stub = pb2_grpc.TupleSpacesStub(self.channel)
        self.client_id = client_id
        self.debug_flag = debug_flag

    def close(self):
        self.debug(self.SHUTDOWN_MESSAGE)
        self.channel.close()

    # Helper method to print debug messages
    def debug(self, debugMessage):
        if (self.debug_flag) :
            print(self.DEBUG + debugMessage, file=sys.stderr)

    def display_success(self):
        print(self.SUCCESS)

    def put(self, tuple_data: str):
        self.debug(self.CLIENT_PREFIX + str(self.client_id) + self.REQUEST_PUT + tuple_data)
        try:
            response = self.stub.put(pb2.PutRequest(clientId=self.client_id, newTuple=tuple_data))
        except grpc.RpcError:
            raise
        self.debug(self.RESPONSE_SERVER + self.OPERATION_SUCCESSFUL)
        self.display_success()

    def read(self, tuple_data: str):
        self.debug(self.CLIENT_PREFIX + str(self.client_id) + self.REQUEST_READ + tuple_data)
        try:
            response = self.stub.read(pb2.ReadRequest(clientId=self.client_id, searchPattern=tuple_data))
        except grpc.RpcError:
            raise
        self.debug(self.RESPONSE_SERVER + self.OPERATION_SUCCESSFUL)
        self.display_success()
        return response.result

    def take(self, tuple_data: str):
        self.debug(self.CLIENT_PREFIX + str(self.client_id) + self.REQUEST_TAKE + tuple_data)
        try:
            response = self.stub.take(pb2.TakeRequest(clientId=self.client_id, searchPattern=tuple_data))
        except grpc.RpcError:
            raise
        self.debug(self.RESPONSE_SERVER + self.OPERATION_SUCCESSFUL)
        self.display_success()
        return response.result

    def get_tuple_spaces_state(self) -> list[str]:
        self.debug(self.CLIENT_PREFIX + str(self.client_id) + self.REQUEST_GET_TUPLE_SPACES_STATE)
        response = self.stub.getTupleSpacesState(pb2.getTupleSpacesStateRequest(clientId=self.client_id))
        self.debug(self.RESPONSE_SERVER + self.OPERATION_SUCCESSFUL)
        self.display_success()
        return response.tuple

