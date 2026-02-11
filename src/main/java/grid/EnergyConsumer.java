package main.java.grid;

public abstract class EnergyConsumer extends NetworkComponent {
    private double energyDemand;
    private int priority;
    protected boolean powered;
    EnergyConsumer(String id, int priority, double energyDemand) {
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
    int getPriority(){
        return this.priority;
    }
    double getEnergyDemand(){
        return this.energyDemand;
    }
    public abstract String displayDetails();

}
