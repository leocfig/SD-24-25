package pt.ulisboa.tecnico.tuplespaces.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseCollector {

    ArrayList<String> collectedResponses;
    Map<Integer, List<String>> collectedLists;
    Throwable error;
    int count;
    int errorCount;

    public ResponseCollector() {
        collectedResponses = new ArrayList<>();
        error = null;
        count = 0;
        errorCount = 0;
    }

    public ResponseCollector(boolean collectVoterLists) {
        collectedLists = new HashMap<>();
        error = null;
        count = 0;
        errorCount = 0;
    }

    public void reset() {
        collectedLists.clear();
        error = null;
        count = 0;
        errorCount = 0;
    }

    synchronized public void addVoterList(List<String> list, int voter) {
        collectedLists.put(voter, list);
        count++;
        notifyAll();
    }

    synchronized public void addString(String s) {
        collectedResponses.add(s);
        count++;
        notifyAll();
    }

    synchronized public void addError(Throwable error) {
        this.error = error;
        errorCount++;
        notifyAll();
    }

    synchronized public boolean hasErrors() {
        return !(error == null);
    }

    synchronized public String getTuple() {
        return collectedResponses.get(0);
    }

    synchronized public Map<Integer, List<String>> getCollectedLists() {
        return collectedLists;
    }

    synchronized public List<String> getVoterList(int voter) {
        return collectedLists.get(voter); 
    }

    synchronized public Throwable getError() {
        return error;
    }

    synchronized public void waitUntilAllReceived(int numResponses, int numErrors) throws InterruptedException {
        while (count < numResponses && error == null){ // exit when one server has an error 
            wait();
        }
        if (error != null) {
            while (errorCount < numErrors){ // numErrors: errors required before frontend notifies client
                wait();
            }
        }
    }
}
