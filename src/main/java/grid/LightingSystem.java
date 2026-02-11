package main.java.grid;

public class LightingSystem extends EnergyConsumer {
    LightingSystem(String id, double energyDemand){
        super(id,3,energyDemand);
    }
    public String displayDetails() {
        String energy = String.format("%.2f", getEnergyDemand());
        String s = "Consumator " + id + " (SistemIluminat) - Cerere: " + energy + " - Prioritate: " + getPriority() + " - Status: ";
        if(powered == true) {
            s = s + "Alimentat\n";
        }
        else s = s + "Decuplat\n";
        return s;
    }
}
