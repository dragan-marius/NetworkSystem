package main.java.grid;

public class LifeSupportSystem extends EnergyConsumer {
    LifeSupportSystem(String id, double energyDemand){
        super(id,1,energyDemand);
    }
    public String displayDetails() {
        String energy = String.format("%.2f", getEnergyDemand());
        String s = "Consumator "+id+" (SistemSuportViata) - Cerere: " + energy+" - Prioritate: " + getPriority()+" - Status: ";
        if(powered == true) {
            s = s + "Alimentat\n";
        }
        else s = s + "Decuplat\n";
        return s;
    }
}
