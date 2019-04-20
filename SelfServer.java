package cs2030.simulator;

import java.util.PriorityQueue;

public class SelfServer extends Server {
    private static final double restChance = 0.0;
    
    @Override
    public boolean isTired() {
        // ROBITS DO NOT REST
        return false;
    }
    
    @Override
    public String toString() {
        return "self-check " +  idNo;
    }
}