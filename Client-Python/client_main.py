import sys
from typing import List
from client_service import ClientService
from command_processor import CommandProcessor

class ClientMain:
    SEPARATOR = ":"

    @staticmethod
    def main(args: List[str]):
        print("ClientMain")

        # checks to see if -debug flag is present (last position assumed)
        debug_flag = False
        num_args = len(args)

        # check arguments
        if num_args < 2 or num_args > 3 or (num_args == 3 and not (args[num_args - 1] == "-debug")) :
            print("Invalid number of arguments!")
            print("Usage: python3 client_main.py <host:port> <client_id> [-debug]")
            return
        
        if args[num_args - 1] == "-debug":
            debug_flag = True
            # receive and print arguments
            print(f"Received {len(args)} arguments")
            for i, arg in enumerate(args):
                print(f"arg[{i}] = {arg}")

        # Get the host and port of the server or front-end
        host_port = args[0]
        parts = host_port.split(ClientMain.SEPARATOR)

        if len(parts) != 2:
            print(f"Error: Invalid format for host:port -> {host_port}", file=sys.stderr)
            return
        try:
            port = int(parts[1])
            client_id = int(args[1])
        except ValueError:
            print("Error: Port and Client ID must both be integers!", file=sys.stderr)
            return        

        client_service = ClientService(host_port, client_id, debug_flag)

        parser = CommandProcessor(client_service)
        parser.parse_input()
        
        client_service.close()


if __name__ == "__main__":
    ClientMain.main(sys.argv[1:])