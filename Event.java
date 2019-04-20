package cs2030.simulator;

public class Event {
    private final Customer customer;
    private final Server server;
    private final double time;
    private final int state;

    public static final int ARRIVES = 1;
    public static final int SERVED = 2;
    public static final int LEAVES = 3;
    public static final int DONE = 4;
    public static final int WAITS = 5;
    public static final int SERVER_REST = 6;
    public static final int SERVER_BACK = 7;

    /**
    * Constructs an Event instance given a customer, time, and state.
    * Nothing much happening beside assigning variables to their
    * respective values.
    */
    public Event(Customer customer,Server server, double time, int state) {
        this.customer = customer;
        this.server = server;
        this.time = time;
        this.state = state;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Server getServer() {
        return server;
    }

    public double getTime() {
        return time;
    }

    public int getState() {
        return state;
    }

    
    /**
    * Used to sort 'naturally' in a priority queue after implementing
    * Comparable. Earlier times get sorted first, and if times are
    * equal, Events by the lower consumer ID goes first.
    */
    /*
    public int compareTo(Event otherEvent) {
        if (time == otherEvent.getTime()) {
            int thisID = customer.getID();
            int thatID = otherEvent.getCustomer().getID();
            if (thisID < thatID) {
                return -1;
            } else if (thisID == thatID) {
                return 0;
            } else {
                return 1;
            }
        } else if (time < otherEvent.getTime()) {
            return -1;
        } else {
            return 1;
        }
    }
    */

    @Override
    public String toString() {
        String stateText = "no Event";
        if (state == ARRIVES) {
            return String.format("%.3f", time) + " " + customer +
                        " arrives";
        } else if (state == SERVED) {
            stateText = "served";
        } else if (state == LEAVES) {
            return String.format("%.3f", time) + " " + customer +
                        " leaves"; 
        } else if (state == DONE) {
            stateText = "done serving";
        } else if (state == WAITS) {
            stateText = "waits to be served";
        } else if (state == SERVER_REST) {
            return String.format("%.3f", time) + " " + server + " rest";
        } else if (state == SERVER_BACK) {
            return String.format("%.3f", time) + " " + server + " back";
        }

        return String.format("%.3f", time) + " " +  customer + 
                     " " + stateText + " by " + server;
    }
}
