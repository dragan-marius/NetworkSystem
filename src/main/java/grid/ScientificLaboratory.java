package main.java.grid;

public class ScientificLaboratory extends EnergyConsumer {
    public ScientificLaboratory(String id, double energyDemand){
        super(id,2, energyDemand);
    }
    public String displayDetails(){
        String energy = String.format("%.2f", getEnergyDemand());
        String s = "Consumer " + id + " (ScientificLaboratory) - Demand: " + energy + " - Priority: " + getPriority() + " - Status: ";
        if (powered == true){
            s = s + "Powered\n";
        }
        else s = s + "Disconnected\n";
        return s;
    }
}
