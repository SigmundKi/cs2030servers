package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;

public class Server {
    private double timeAvailable; //The next time this server is available to serve. Is not immutable, is bad, i know :(
    private static int IDCount = 1;
    protected final int idNo;
    private static RandomGenerator myRG;
    private static int maxQueueLength = 1;
    private final Queue<Customer> queue;
    private static double restChance = 0.0;
    
    public static final int ARRIVES = 1;
    public static final int SERVED = 2;
    public static final int LEAVES = 3;
    public static final int DONE = 4;
    public static final int WAITS = 5;
    public static final int SERVER_REST = 6;
    public static final int SERVER_BACK = 7;

    //==Constructors=============================================================================\\
    
    /**
    * Sets the RandomGenerator for all Servers.
    */
    public static void setSeed(int seedValue, double arrivalRate, 
                                double serviceRate, double restRate) {
        myRG = new RandomGenerator(seedValue, arrivalRate, serviceRate, restRate);
    }
    
    /**
    * Set the maxQueueLength for all Servers.
    */
    public static void setMaxQueue(int max) {
        maxQueueLength = max;
    }
    
    /**
    * Set the restChance for all Servers.
    */
    public static void setRestChance(double chance) {
        restChance = chance;
    }
    
    /**
    * Constructs a new Server. Available to serve right away.
    */
    public Server() {
        idNo = IDCount;
        IDCount++;
        timeAvailable = 0.0;
        queue = new LinkedList<>();
    }
    
    
    //==Operations===============================================================================\\
    
    /**
    * Starts serving a Customer.
    * Updates the timeAvailable of this Server.
    * @param servedEvent the SERVED event preceding this method call
    * @return a DONE event for after the service is done and the server is ready to serve again
    */
    public Event startServing(Event servedEvent) {
        Customer customer = servedEvent.getCustomer();
        double startTime = servedEvent.getTime();
        double serviceTime = myRG.genServiceTime();
        timeAvailable = startTime + serviceTime;
        return new Event(customer, this, startTime + serviceTime, DONE);
    }

    /**
    * Serves the waiting customers in this Server's queue.
    * @return a SERVED event for the first waiting customer
    */
    public Event serveQueue() {
        return new Event(queue.poll(), this, timeAvailable, SERVED);
    }

    /**
    * Declares that the server is tired.
    * @param doneEvent the DONE event preceding this method call
    * @return a SERVER_REST for the server to startResting
    */
    public Event tired(Event doneEvent) {
        double time = doneEvent.getTime();
        return new Event(null, this, time, SERVER_REST);
    }
    
    /**
    * Starts resting. 
    * Updates the timeAvailable of this Server.
    * @param restEvent the REST event preceding this method call
    * @return a SERVER_BACK event for when the server is back and ready to serve again
    */
    public Event startResting(Event restEvent) {
        double time = restEvent.getTime();
        double restPeriod = myRG.genRestPeriod();
        timeAvailable += restPeriod;
        return new Event(null, this, time + restPeriod, SERVER_BACK);
    }
    
    //==Miscellaneous============================================================================\\
    
    public int getID() {
        return idNo;
    }
    
    public boolean isAvailable(double time) {
        return time >= timeAvailable;
    }
    
    public boolean queueIsAvailable() {
        return queue.size() < maxQueueLength;
    }
    
    public boolean hasCustomersWaiting() {
        return !queue.isEmpty();
    }
    
    public int getQueueSize() {
        return queue.size();
    }
    
    public void addToQueue(Customer customer) {
        queue.add(customer);
    }
    
    public boolean isTired() {
        return myRG.genRandomRest() < restChance;
    }
    
    @Override
    public String toString() {
        return "server " +  idNo;
    }

}
