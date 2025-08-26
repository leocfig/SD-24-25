package pt.ulisboa.tecnico.tuplespaces.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.grpc.StatusRuntimeException;
import pt.ulisboa.tecnico.tuplespaces.client.grpc.ClientService;


public class CommandProcessor {

    private static final String SPACE = " ";
    private static final String BGN_TUPLE = "<";
    private static final String END_TUPLE = ">";
    private static final String PUT = "put";
    private static final String READ = "read";
    private static final String TAKE = "take";
    private static final String SLEEP = "sleep";
    private static final String EXIT = "exit";
    private static final String GET_TUPLE_SPACES_STATE = "getTupleSpacesState";
    private static final String INVALID_SERVER_INPUT_ERROR = "Invalid response from server.";
    private static final String OPERATION_FAILED = " operation failed: ";

    private final ClientService clientService;

    public CommandProcessor(ClientService clientService) {
        this.clientService = clientService;
    }

    void parseInput() {

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            String[] split = line.split(SPACE);
             switch (split[0]) {
                case PUT:
                    this.put(split);
                    break;

                case READ:
                    this.read(split);
                    break;

                case TAKE:
                    this.take(split);
                    break;

                case GET_TUPLE_SPACES_STATE:
                    this.getTupleSpacesState();
                    break;

                case SLEEP:
                    this.sleep(split);
                    break;

                case EXIT:
                    exit = true;
                    break;

                default:
                    this.printUsage();
                    break;
             }
        }
        scanner.close();
    }

    private String[] parseDelays(String[] input) {
        // Default delay values (assume "" if not provided)
        String[] delays = new String[] { "", "", "" };

        // Fill available delays
        for (int i = 2; i < input.length && i < 5; i++) {
            delays[i - 2] = input[i];
        }

        return delays;
    }

    private void put(String[] split) {

        // check if input is valid
        if (!this.inputIsValid(split)) {
            this.printUsage();
            return;
        }
        
        // get the tuple
        String tuple = split[1];

        String[] delays = parseDelays(split);

        // get the arguments after the tuple
        String delayReplica1 = delays[0];
        String delayReplica2 = delays[1];
        String delayReplica3 = delays[2];

        try {
            // put the tuple
            this.clientService.put(tuple, delayReplica1, delayReplica2, delayReplica3);
            System.out.println();
        } catch (StatusRuntimeException e) { // Thrown when the server encounters an error and responds with an exception
            System.err.println(PUT + OPERATION_FAILED + e.getStatus().getDescription() + "\n");
        }
    }

    private void read(String[] split){
        // check if input is valid
        if (!this.inputIsValid(split)) {
            this.printUsage();
            return;
        }
        
        // get the tuple
        String tuple = split[1];

        String[] delays = parseDelays(split);

        // get the arguments after the tuple
        String delayReplica1 = delays[0];
        String delayReplica2 = delays[1];
        String delayReplica3 = delays[2];

        String result = "";

        try {
            // read the tuple
            result = this.clientService.read(tuple, delayReplica1, delayReplica2, delayReplica3);
            // Making sure the server didn't return an invalid tuple
            if (!this.tupleIsValid(result)) {
                System.out.println(READ + OPERATION_FAILED + INVALID_SERVER_INPUT_ERROR + "\n");
                return;
            }
            System.out.println(result + "\n");
        } catch (StatusRuntimeException e) { // Thrown when the server encounters an error and responds with an exception
            System.err.println(READ + OPERATION_FAILED + e.getStatus().getDescription() + "\n");
        }
    }


    private void take(String[] split){
         // check if input is valid
        if (!this.inputIsValid(split)) {
            this.printUsage();
            return;
        }
        
        // get the tuple
        String tuple = split[1];

        String[] delays = parseDelays(split);

        // get the arguments after the tuple
        String delayReplica1 = delays[0];
        String delayReplica2 = delays[1];
        String delayReplica3 = delays[2];
        
        String result = "";

        try {
            // take the tuple
            result = this.clientService.take(tuple, delayReplica1, delayReplica2, delayReplica3);
            // Making sure the server didn't return an invalid tuple
            if (!this.tupleIsValid(result)) {
                System.out.println(TAKE + OPERATION_FAILED + INVALID_SERVER_INPUT_ERROR + "\n");
                return;
            }
            System.out.println(result + "\n");
        } catch (StatusRuntimeException e) { // Thrown when the server encounters an error and responds with an exception
            System.err.println(TAKE + OPERATION_FAILED + e.getStatus().getDescription() + "\n");
        }
    }

    private void getTupleSpacesState(){

        List<String> result = new ArrayList<>();

        try {
            // get the tuple spaces state
            result = this.clientService.getTupleSpacesState();
            // Making sure the server didn't return invalid tuples
            for (String tuple : result) {
                if (!this.tupleIsValid(tuple)) {
                    System.out.println(GET_TUPLE_SPACES_STATE + OPERATION_FAILED + INVALID_SERVER_INPUT_ERROR + "\n");
                    return;
                }
            }
            System.out.println(result + "\n"); 
        } catch (StatusRuntimeException e) { // Handle possible IO errors
            System.err.println(GET_TUPLE_SPACES_STATE + OPERATION_FAILED + e.getStatus().getDescription() + "\n");
        }
    }

    private void sleep(String[] split) {
      if (split.length != 2){
        this.printUsage();
        return;
      }
      Integer time;

      // checks if input String can be parsed as an Integer
      try {
         time = Integer.parseInt(split[1]);
      } catch (NumberFormatException e) {
        this.printUsage();
        return;
      }

      try {
        this.clientService.debug("Client " + this.clientService.getClientId() + 
                                      "-> sleeping for " + time + " seconds.");
        Thread.sleep(time*1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    private void printUsage() {
        System.out.println("Usage:\n" +
                "- put <element[,more_elements]>\n" +
                "- read <element[,more_elements]>\n" +
                "- take <element[,more_elements]>\n" +
                "- getTupleSpacesState <server>\n" +
                "- sleep <integer>\n" +
                "- exit\n");
    }

    private boolean inputIsValid(String[] input){
        if (input.length < 2 || !input[1].substring(0,1).equals(BGN_TUPLE)
            || !input[1].endsWith(END_TUPLE)) {
            return false;
        }

        // Number of optional delay values
        int numDelays = input.length - 2;

        // Only accept 0 or exactly 3 delay values
        if (numDelays != 0 && numDelays != 3) {
            return false;
        }

        // Check if all delay values (if present) are non-negative integers
        for (int i = 2; i < input.length; i++) {
            if (!input[i].matches("\\d+")) {
                return false;
            }
        }
        return true;
    }

    private boolean tupleIsValid(String input){
        String[] split = input.split(SPACE);

        if (split.length != 1
            ||
            !split[0].substring(0,1).equals(BGN_TUPLE) 
            || 
            !split[0].endsWith(END_TUPLE)) {
            return false;
        }
        else {
            return true;
        }
    }
}
