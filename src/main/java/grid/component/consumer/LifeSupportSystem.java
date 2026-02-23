package main.java.grid.component.consumer;

public class LifeSupportSystem extends EnergyConsumer {
    public LifeSupportSystem(String id, double energyDemand){
        super(id,1,energyDemand);
    }
    public String displayDetails() {
        String energy = String.format("%.2f", getEnergyDemand());
        String s = "Consumer "+id+" (LifeSupportSystem) - Demand: " + energy+" - Priority: " + getPriority()+" - Status: ";
        if(powered == true) {
            s = s + "powered\n";
        }
        else s = s + "Disconnected\n";
        return s;
    }
}
