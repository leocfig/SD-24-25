package pt.ulisboa.tecnico.tuplespaces.client;

import pt.ulisboa.tecnico.tuplespaces.client.grpc.ClientService;

public class ClientMain {

    private static final String SEPARATOR = ":";

    public static void main(String[] args) {

        System.out.println(ClientMain.class.getSimpleName());

        // checks to see if -debug flag is present (last position assumed)
        boolean debugFlag = false;
        int numArgs = args.length;

        // check arguments
        if (numArgs < 2 || numArgs > 3 || (numArgs == 3 && !args[numArgs - 1].equals("-debug"))) {
            System.err.println("Invalid number of arguments!");
            System.err.println("Usage: mvn exec:java -Dexec.args=<host:port> <client_id> [-debug]");
            return;
        }

        if (args[numArgs - 1].equals("-debug")) {
            debugFlag = true;
        }

        // receive and print arguments
        if (debugFlag) {
            System.out.printf("Received %d arguments%n", args.length);
            for (int i = 0; i < args.length; i++) {
                System.out.printf("arg[%d] = %s%n", i, args[i]);
            }
        }

        // get the host and the port
        final String hostPort = args[0];

        String[] parts = hostPort.split(SEPARATOR);

        // Ensure the format is correct (host:port)
        if (parts.length != 2) {
            System.err.println("Error: Invalid format for host:port -> " + hostPort);
            return;
        }

        // Extract and validate the port and client ID
        final int port;
        final int clientId;
        try {
            port = Integer.parseInt(parts[1]);
            clientId = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Error: Port and Client ID must both be integers!");
            return;
        }

        // Create the client service
        ClientService clientService = new ClientService(hostPort, clientId, debugFlag); 

        CommandProcessor parser = new CommandProcessor(clientService);
        parser.parseInput();

        // Shutdown the client properly
        clientService.shutdown();
    }
}
