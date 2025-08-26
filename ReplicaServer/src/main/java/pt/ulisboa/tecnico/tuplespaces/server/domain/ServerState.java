package pt.ulisboa.tecnico.tuplespaces.server.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;


public class ServerState {

  private static class TupleData {
    int count;
    boolean locked;

    TupleData(int count) {
      this.count = count;
      this.locked = false;
    }
  }

  private static final String SPACE = " ";
  private static final String BGN_TUPLE = "<";
  private static final String END_TUPLE = ">";

  private final ConcurrentHashMap<String, TupleData> tuples;

  public ServerState() {
    this.tuples = new ConcurrentHashMap<>();
  }

  public boolean isRegex(String input) {
    return input.matches(".*[.*+?\\[\\](){}|\\\\].*");
  }

  public String getTuple(String input) {
    if (isRegex(input)) {
      // Handle as a regex pattern
      return getTupleByPattern(input);
    }
    else {
      // Handle as an exact tuple lookup
      return tuples.containsKey(input) ? input : null;
    }
  }

  public String getTupleByPattern(String pattern) {
    Pattern regex = Pattern.compile(pattern);
    for (String key : tuples.keySet()) {
      if (regex.matcher(key).matches()) {
        return key;
      }
    }
    return null;  // If no match was found
  }

  public void put(String tuple) {
    // Concurrent thread-safe update of the map
    tuples.compute(tuple, (key, data) -> {
      if (data == null) {
        return new TupleData(1);
      }
      else {
        data.count++;
        return data;
      }
    });

    synchronized (this) {
      notifyAll(); // notifies that a new tuple was added
    }
  }

  public String read(String pattern) {
    if (!this.tupleIsValid(pattern)) {
      return null;  // Invalid pattern
    }

    while (true) {
      String tuple = getTuple(pattern);
      if (tuple != null) {      // Only proceed if the tuple exists
        return tuple;           // Doesn't check if it is already locked to allow concurrent read and take
      }
      try {
        synchronized (this) {
          wait(); // Wait for a new tuple to be added
        }
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public String take(String pattern, boolean unlock) {
    while (true) {
      TupleData data = tuples.get(pattern); // Direct lookup
      if (data != null) {     // Only proceed if the tuple exists
        synchronized (data) { // Lock only the specific tuple
          data.count--;
          // Remove from map if this was the last tuple
          if (data.count == 0) {
            tuples.remove(pattern);
            data.notifyAll(); // Notify waiting threads
          }
          else if (unlock) {
            data.locked = false;
            data.notifyAll(); // Notify waiting threads
          }
          return pattern;
        }
      }
    }
  }

  public synchronized List<String> getTupleSpacesState() { 
    List<String> tupleList = new ArrayList<>();
    
    for (Map.Entry<String, TupleData> entry : tuples.entrySet()) {
      String tuple = entry.getKey();
      int count = entry.getValue().count;

      // Add the tuple 'count' times to the list to reflect its occurrences
      for (int i = 0; i < count; i++) {
        tupleList.add(tuple);
      }
    }
    
    // The tuples will not be printed in insertion order
    // since they are stored in a Hashmap
    return tupleList;
  }

  private boolean tryLockTuple(TupleData data) throws InterruptedException {
    synchronized (data) { // Lock only this specific tuple
      if (!data.locked) {
        data.locked = true;
        return true;
      }
    }
    return false;
  }

  public List<String> lockTuples(String searchPattern, int clientId) throws InterruptedException {
    List<String> matchingTuples = new ArrayList<>();
    boolean isRegex = isRegex(searchPattern);

    if (isRegex) {
      for (Map.Entry<String, TupleData> entry : tuples.entrySet()) {
        if (entry.getKey().matches(searchPattern)) {
          if (tryLockTuple(entry.getValue())) matchingTuples.add(entry.getKey()); 
        }
      }
    }
    else {
      TupleData data;
      while (true) {
        while ((data = tuples.get(searchPattern)) == null) {  
          synchronized (this) {
            try {
              wait();
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        }
        // Exit loop when lock was successful
        if (tryLockTuple(data)) {
          matchingTuples.add(searchPattern);
          break;
        }
      }
    }
    return matchingTuples;
  }

  public void releaseTuples(List<String> releaseTuples) {
    for (String tuple : releaseTuples) {
      unlockTuple(tuple);
    }
  }

  private void unlockTuple(String tuple) {
    TupleData data = tuples.get(tuple);

    if (data != null) {
      synchronized (data) {
        data.locked = false;
        data.notifyAll(); // Notify waiting threads
      }
    }
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
