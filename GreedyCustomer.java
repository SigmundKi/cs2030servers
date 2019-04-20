package cs2030.simulator;

import java.util.List;
import java.util.PriorityQueue;

public class GreedyCustomer extends Customer {
    
    /**
    * Finds an available server to go to. 
    * If none are available, tries to find the server with the smallest queue.
    * If none are available, will leave.
    * @param arriveEvent the arriveEvent preceding this method call
    * @param servers the list of servers to look for
    * @return a SERVED event if a server is available, a WAITS event if a queue is available, 
    *           or a LEAVES event if nothing is available
    */
    @Override
    public Event pickServer(Event arriveEvent, List<Server> servers) {
        double time = arriveEvent.getTime();
        
        // Try to find available server
        for (Server s: servers) {
            if (s.isAvailable(time)) {
                return new Event(this, s, time, SERVED);
            }
        }
        Server smallestQueue = servers.get(0);
        // Try to find smallest available queue
        for (Server s: servers) {
            if (s.getQueueSize() < smallestQueue.getQueueSize()) {
                smallestQueue = s;
            }
        }
        if (smallestQueue.queueIsAvailable()) {
            return new Event(this, smallestQueue, time, WAITS);
        }
        
        // Leaves
        return new Event(this, null, time, LEAVES);
    }
    
    @Override
    public String toString() {
        return idNo + "(greedy)";
    }
}