package main.java.grid.component.consumer;

public class LightingSystem extends EnergyConsumer {
    public LightingSystem(String id, double energyDemand){
        super(id,3,energyDemand);
    }
    public String displayDetails() {
        String energy = String.format("%.2f", getEnergyDemand());
        String s = "Consumer " + id + " (LightingSystem) - Demand: " + energy + " - Priority: " + getPriority() + " - Status: ";
        if(powered == true) {
            s = s + "Powered\n";
        }
        else s = s + "Disconnected\n";
        return s;
    }
}
