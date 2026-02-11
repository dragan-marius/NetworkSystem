package main.java.grid;

public class ScientificLaboratory extends EnergyConsumer {
    public ScientificLaboratory(String id, double energyDemand){
        super(id,2, energyDemand);
    }
    public String displayDetails(){
        String energy = String.format("%.2f", getEnergyDemand());
        String s = "Consumator " + id + " (LaboratorStiintific) - Cerere: " + energy + " - Prioritate: " + getPriority() + " - Status: ";
        if (powered == true){
            s = s + "Alimentat\n";
        }
        else s = s + "Decuplat\n";
        return s;
    }
}
