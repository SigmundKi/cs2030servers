import cs2030.simulator.Event;
import cs2030.simulator.Customer;
import cs2030.simulator.GreedyCustomer;
import cs2030.simulator.Server;
import cs2030.simulator.SelfServer;
import cs2030.simulator.EventComparator;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Comparator;


public class Main {
    public static final int ARRIVES = 1;
    public static final int SERVED = 2;
    public static final int LEAVES = 3;
    public static final int DONE = 4;
    public static final int WAITS = 5;
    public static final int SERVER_REST = 6;
    public static final int SERVER_BACK = 7;

    /**
    * "Serves" a queue of customers by iterating through the list of Events on what to do next.
    * First it will add an arrive event for all the customers, 
    * then it will iterate through the list of Events one by one, 
    * adding a response Event if necessary. 
    */
    public static void serveCustomers(List<Server> servers, Queue<Customer> customers) {

        double totalWaitTime = 0.0;
        int customersServed = 0;
        PriorityQueue<Event> events = new PriorityQueue<>(new EventComparator());
        
        for (Customer customer: customers) {
            events.add(new Event(customer, null, customer.getArrival(), ARRIVES));
        }

        // Iterates through each event
        while (!events.isEmpty()) {
            Event nextEvent = events.remove();
            Customer customer = nextEvent.getCustomer();
            Server server = nextEvent.getServer();
            System.out.println(nextEvent);

            if (nextEvent.getState() == ARRIVES) {
                events.add(customer.pickServer(nextEvent, servers)); // returns either SERVED, WAITS, or LEAVES
            } else if (nextEvent.getState() == SERVED) {
                events.add(server.startServing(nextEvent)); // returns DONE
                customersServed++;
                totalWaitTime += nextEvent.getTime() - customer.getArrival();
            } else if (nextEvent.getState() == DONE) {
                if (server.isTired()) {
                    events.add(server.tired(nextEvent)); // returns SERVER_REST
                } else {
                    if (server.hasCustomersWaiting()) {
                        events.add(server.serveQueue()); // returns SERVED
                    }
                }
            } else if (nextEvent.getState() == WAITS) {
                server.addToQueue(customer);
            } else if (nextEvent.getState() == SERVER_REST) {
                events.add(server.startResting(nextEvent)); // returns SERVER_BACK
            } else if (nextEvent.getState() == SERVER_BACK) {
                if (server.hasCustomersWaiting()) {
                    events.add(server.serveQueue()); // returns SERVED
                }
            }

        }
        
        // Prints out statistics at the end
        int customersLeft = customers.size() - customersServed;
        double averageWaitTime = customersServed == 0 ? 0 : totalWaitTime / customersServed;
        System.out.println("[" + String.format("%.3f", averageWaitTime) + " " +
                             customersServed + " " + customersLeft + "]");        
    }



    /**
    * Scans all the variables(thats a lot of variables) to construct a list of Customers and Servers.
    * Passes stuff to the serveCustomers() method for further processing.
    */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Queue<Customer> customers = new LinkedList<>();
        List<Server> servers = new LinkedList<>();

        int seedValue = scanner.nextInt();
        int numOfServers = scanner.nextInt();
        int numOfSelfCheck = scanner.nextInt();
        int maxQueueLength = scanner.nextInt();
        int numOfCustomers = scanner.nextInt();
        double arrivalRate = scanner.nextDouble();
        double serviceRate = scanner.nextDouble();
        double restRate = scanner.nextDouble();
        double restChance = scanner.nextDouble();
        double greedyChance = scanner.nextDouble();
        
        Customer.setSeed(seedValue, arrivalRate, serviceRate, restRate);
        Customer.setGreedyChance(greedyChance);
        Server.setSeed(seedValue, arrivalRate, serviceRate, restRate);
        Server.setMaxQueue(maxQueueLength);
        Server.setRestChance(restChance);

        for (int i = 0; i < numOfServers; i++) {
            servers.add(new Server());
        }
        for (int i = 0; i < numOfSelfCheck; i++) {
            servers.add(new SelfServer());
        }
        
        for (int i = 0; i < numOfCustomers; i++) {
            if (!Customer.genCustomerGreediness()) {
                customers.add(new Customer());
            } else {
                customers.add(new GreedyCustomer());
            }
        }
        
        // Main juice of everything that is happening
        serveCustomers(servers, customers);
    }
}
