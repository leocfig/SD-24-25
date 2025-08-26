import sys
import time
import grpc

from typing import List

class CommandProcessor:
    SPACE = " "
    BGN_TUPLE = "<"
    END_TUPLE = ">"
    PUT = "put"
    READ = "read"
    TAKE = "take"
    EXIT = "exit"
    SLEEP = "sleep"
    GET_TUPLE_SPACES_STATE = "getTupleSpacesState"
    INVALID_SERVER_INPUT_ERROR = "Invalid response from server."
    OPERATION_FAILED = "operation failed:"

    def __init__(self, client_service):
        self.client_service = client_service

    def parse_input(self):
        exit_flag = False
        while not exit_flag:
            try:
                line = input("> ").strip()
                split = line.split(self.SPACE)
                command = split[0]

                if command == self.PUT:
                    self.put(split)
                elif command == self.READ:
                    self.read(split)
                elif command == self.TAKE:
                    self.take(split)
                elif command == self.GET_TUPLE_SPACES_STATE:
                    self.get_tuple_spaces_state()
                elif command == self.SLEEP:
                    self.sleep(split)
                elif command == self.EXIT:
                    exit_flag = True
                else:
                    self.print_usage()
                
            except EOFError:
                break

    def put(self, split: List[str]):
        # check if the input is valid
        if not self.input_is_valid(split):
            self.print_usage()
            return

        # get the tuple
        tuple_data = split[1]
        try:
            # put the tuple
            self.client_service.put(tuple_data)
            print()
        except grpc.RpcError as e:  # Thrown when the server encounters an error and responds with an exception
            print(f"{self.PUT} {self.OPERATION_FAILED} {e.details()}\n", file=sys.stderr)

    def read(self, split: List[str]):
        # check if the input is valid
        if not self.input_is_valid(split):
            self.print_usage()
            return

        # get the tuple
        tuple_data = split[1]
        try:
            # read the tuple
            result = self.client_service.read(tuple_data)

            if not self.tuple_is_valid(result):
                print(f"{self.READ} {self.OPERATION_FAILED} {self.INVALID_SERVER_INPUT_ERROR}", file=sys.stderr)
                return
            
            print(f"{result}\n")                
        except grpc.RpcError as e:  # Thrown when the server encounters an error and responds with an exception
            print(f"{self.READ} {self.OPERATION_FAILED} {e.details()}\n", file=sys.stderr)

    def take(self, split: List[str]):
        # check if the input is valid
        if not self.input_is_valid(split):
            self.print_usage()
            return

        # get the tuple
        tuple_data = split[1]
        try:
            # take the tuple
            result = self.client_service.take(tuple_data)

            if not self.tuple_is_valid(result):
                print(f"{self.TAKE} {self.OPERATION_FAILED} {self.INVALID_SERVER_INPUT_ERROR}", file=sys.stderr)
                return
            
            print(f"{result}\n")              
        except grpc.RpcError as e:  # Thrown when the server encounters an error and responds with an exception
            print(f"{self.TAKE} {self.OPERATION_FAILED} {e.details()}\n", file=sys.stderr)

    def get_tuple_spaces_state(self):
        result = []
        
        try:
            # get the tuple spaces state
            result = self.client_service.get_tuple_spaces_state()

            for tuple in result:
                if not self.tuple_is_valid(tuple):
                    print(f"{self.GET_TUPLE_SPACES_STATE} {self.OPERATION_FAILED} {self.INVALID_SERVER_INPUT_ERROR}", file=sys.stderr)
                    return
                
            # Print tuples in the desired format
            formatted_tuples = ', '.join(result)
            print(f"[{formatted_tuples}]\n")    
        except grpc.RpcError as e:   # Handle possible IO errors
            print(f"{self.TAKE} {self.OPERATION_FAILED} {e.details()}\n", file=sys.stderr)
        
    def sleep(self, split: List[str]):
        
        
        if len(split) != 2:
            self.print_usage()
            return

        try:
            sleep_time = int(split[1])
            self.client_service.debug("Client " + self.client_service.client_id + \
                                      "-> sleeping for " + sleep_time + " seconds.")
        except ValueError:
            self.print_usage()
            return
        try:
            time.sleep(sleep_time)
        except KeyboardInterrupt:
            raise RuntimeError("Sleep interrupted")

    def print_usage(self):
        print("Usage:\n"
              "- put <element[,more_elements]>\n"
              "- read <element[,more_elements]>\n"
              "- take <element[,more_elements]>\n"
              "- getTupleSpacesState\n"
              "- sleep <integer>\n"
              "- exit\n")

    def input_is_valid(self, input_data: List[str]) -> bool:
        if (len(input_data) < 2
                or not input_data[1].startswith(self.BGN_TUPLE)
                or not input_data[1].endswith(self.END_TUPLE)
                or len(input_data) > 2):
            return False
        return True
    
    def tuple_is_valid(self, input: str) -> bool:
        split = input.split(self.SPACE)

        if (len(split) != 1
                or not split[0].startswith(self.BGN_TUPLE)
                or not split[0].endswith(self.END_TUPLE)):
            return False
        return True