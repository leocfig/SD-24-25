package pt.ulisboa.tecnico.tuplespaces.frontend;

import java.io.IOException;
import java.util.Arrays;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import pt.ulisboa.tecnico.tuplespaces.frontend.grpc.HeaderFrontendInterceptor;
import pt.ulisboa.tecnico.tuplespaces.frontend.grpc.FrontEndServiceImpl;


public class FrontEndMain {

    private static final int NUM_SERVERS = 3;
    private static final String SEPARATOR = ":";

    public static void main(String[] args) throws IOException, InterruptedException {

        // checks to see if -debug flag is present (last position assumed)
        boolean debugFlag = false;
        int numArgs = args.length;
        if (args[numArgs - 1].equals("-debug")) {
            debugFlag = true;
            // receive and print arguments in debug mode
            System.out.printf("Received %d arguments%n", args.length);
            for (int i = 0; i < args.length; i++) {
                System.out.printf("arg[%d] = %s%n", i, args[i]);
            }
        }

        // check arguments
        if (numArgs < 4 || numArgs > 5 || (numArgs == 5 && !args[numArgs - 1].equals("-debug"))) {
            System.err.println("Invalid number of arguments!");
            System.err.println("Usage: mvn exec:java -Dexec.args= <frontend port> <host:port_server1> <host:port_server2> <host:port_server3> [-debug]");
            return;
        }

        // Extract frontend port
        final int frontendPort;
        try {
            frontendPort = Integer.parseInt(args[0]);   // The port for the front-end server
        } catch (NumberFormatException e) {
            System.err.println("Port must be an integer value!");
            System.err.println("Usage: mvn exec:java -Dexec.args= <frontend port> <host:port_server1> <host:port_server2> <host:port_server3> [-debug]");
            return;
        }

        // Array to store the host:port values
        String[] hostPorts = Arrays.copyOfRange(args, 1, 4);

        for (String hostPort : hostPorts) {
            String[] parts = hostPort.split(SEPARATOR);

            // Ensure the format is correct (host:port)
            if (parts.length != 2) {
                System.err.println("Error: Invalid format for host:port -> " + hostPort);
                return;
            }

            // Validate port is an integer
            try {
                int port = Integer.parseInt(parts[1]);
                if (port < 1 || port > 65535) {
                    System.err.println("Error: Port number out of range (1-65535) -> " + port);
                    return;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Port must be an integer -> " + parts[1]);
                return;
            }
        }

        final BindableService impl = new FrontEndServiceImpl(hostPorts ,debugFlag, NUM_SERVERS);

        Server frontEnd = ServerBuilder.forPort(frontendPort)
                .addService(ServerInterceptors
                .intercept(impl, new HeaderFrontendInterceptor()))
                .build();

        frontEnd.start();
        
        System.out.println("Frontend started on port: " + frontendPort);

        frontEnd.awaitTermination();

        // Shutdown the frontend properly
        ((FrontEndServiceImpl) impl).shutDown();
        frontEnd.shutdown();
    }
}
