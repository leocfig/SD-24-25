package pt.ulisboa.tecnico.tuplespaces.server.domain;

import java.util.ArrayList;
import java.util.List;


public class ServerState {

  private static final String SPACE = " ";
  private static final String BGN_TUPLE = "<";
  private static final String END_TUPLE = ">";

  private List<String> tuples;

  public ServerState() {
    this.tuples = new ArrayList<String>();
  }

  public boolean put(String tuple) {
    if (!this.tupleIsValid(tuple)) {
      return false;
    }
    synchronized (this) {
      tuples.add(tuple);
      notifyAll(); // notifies that a new tuple was added
    }
    return true;
  }

  private synchronized String getMatchingTuple(String pattern, boolean remove) {
    String result = null;
    while (true) {
      // Optimize tuple search by checking the most recently added tuples first
      for (int i = this.tuples.size() - 1; i >= 0; i--) { // Iterate from end to start
        String tuple = this.tuples.get(i);
        if (tuple.matches(pattern)) {
          result = tuple;
          if (remove) tuples.remove(i);
          return result;
        }
      }
      try {
        wait(); // wait until a new tuple is added
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public String read(String pattern) {
    String tuple = null;
    if (this.tupleIsValid(pattern)) {
      tuple = getMatchingTuple(pattern, false);
    }
    return tuple; // tuple remains null only if pattern is not valid
  }

  public String take(String pattern) {
    String tuple = null;
    if (this.tupleIsValid(pattern)) {
      tuple = getMatchingTuple(pattern, true);
    }
    return tuple; // tuple remains null only if pattern is not valid
  }

  public synchronized List<String> getTupleSpacesState() {
    return this.tuples;
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
