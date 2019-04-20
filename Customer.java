package cs2030.simulator;

import cs2030.simulator.RandomGenerator;
import java.util.List;
import java.util.PriorityQueue;

public class Customer {
    private final double arrivalTime;
    private static int IDCount = 1;
    protected final int idNo;
    private static RandomGenerator myRG;
    private static double timeStamp = 0;
    private static double greedyChance = 0.0;
    
    public static final int ARRIVES = 1;
    public static final int SERVED = 2;
    public static final int LEAVES = 3;
    public static final int DONE = 4;
    public static final int WAITS = 5;
    public static final int SERVER_REST = 6;
    public static final int SERVER_BACK = 7;
    
    //==Constructors=============================================================================\\

    /**
    * Sets the RandomGenerator for all Customers.
    */
    public static void setSeed(int seedValue, double arrivalRate, 
                                    double serviceRate, double restRate) {
        myRG = new RandomGenerator(seedValue, arrivalRate, serviceRate, restRate);
    }
    
    /**
    * Set the greedChance for all Servers.
    */
    public static void setGreedyChance(double chance) {
        greedyChance = chance;
    }
    
    public static boolean genCustomerGreediness() {
        return myRG.genCustomerType() < greedyChance;
    }
    
    /**
    * Constructs a new Customer using the set RandomGenerator.
    */
    public Customer() {
        this.arrivalTime = timeStamp;
        timeStamp += myRG.genInterArrivalTime();
        
        idNo = IDCount;
        IDCount++;
    }
    
    //==Operations===============================================================================\\
   
    /**
    * Finds an available server to go to. 
    * If none are available, tries to find a server with available waiting queue.
    * If none are available, will leave.
    * @param arriveEvent the arriveEvent preceding this method call
    * @param servers the list of servers to look for
    * @return a SERVED event if a server is available, a WAITS event if a queue is available, 
    *           or a LEAVES event if nothing is available
    */
    public Event pickServer(Event arriveEvent, List<Server> servers) {
        double time = arriveEvent.getTime();
        
        // Try to find available server
        for (Server s: servers) {
            if (s.isAvailable(time)) {
                return new Event(this, s, time, SERVED);
            }
        }
        // Try to find available queue
        for (Server s: servers) {
            if (s.queueIsAvailable()) {
                return new Event(this, s, time, WAITS);
            }
        }
        
        // Leaves
        return new Event(this, null, time, LEAVES);
    }
   
    //==Miscellaneous============================================================================\\

    public int getID() {
        return idNo;
    }

    public double getArrival() {
        return arrivalTime;
    }
    
    @Override
    public String toString() {
        return "" + idNo;
    }
}
