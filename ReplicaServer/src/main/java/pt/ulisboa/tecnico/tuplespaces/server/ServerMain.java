package pt.ulisboa.tecnico.tuplespaces.server;

import java.io.IOException;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import pt.ulisboa.tecnico.tuplespaces.server.grpc.HeaderServerInterceptor;
import pt.ulisboa.tecnico.tuplespaces.server.grpc.TuplespacesServiceImpl;

public class ServerMain {

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

      // Extract and validate the port
      final int port;
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.err.println("Error: Port must be an integer!");
        return;
      }

      // check arguments
      if (numArgs< 1 || numArgs > 2 || (numArgs == 2 && !args[numArgs - 1].equals("-debug"))) {
        System.err.println("Invalid number of arguments!");
        System.err.println("Usage: mvn exec:java -Dexec.args=<port> [-debug]");
        return;
      }

      final BindableService impl = new TuplespacesServiceImpl(debugFlag);

      // Create a new server to listen on port
      Server server = ServerBuilder.forPort(port)
                                   .addService(ServerInterceptors
                                   .intercept(impl, new HeaderServerInterceptor()))
                                   .build();

      // Start the server
      server.start();

      // Server threads are running in the background.
      System.out.println("Server started");

      // Do not exit the main thread. Wait until server is terminated.
      server.awaitTermination();

      // Shutdown the server properly
      server.shutdown();
    }
}

