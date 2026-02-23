package main.java.grid.component.consumer;

import main.java.grid.component.base.*;
public abstract class EnergyConsumer extends NetworkComponent {
    private double energyDemand;
    private int priority;
    protected boolean powered;
    public EnergyConsumer(String id, int priority, double energyDemand) {
        super(id);
        this.priority = priority;
        this.energyDemand = energyDemand;
        this.powered = true;
    }
    public double getCurrentRequest() {
        if (this.powered) {
            return this.energyDemand;
        }
        return 0.0;
    }
    public void connectToNetwork() {

        this.powered =true;
    }
    public void disconnectFromNetwork() {
        this.powered =false;
    }
    public int getPriority(){
        return this.priority;
    }
    public double getEnergyDemand(){
        return this.energyDemand;
    }
    public abstract String displayDetails();

}
