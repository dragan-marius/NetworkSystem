package main.java.grid.component.storage;

import main.java.grid.component.base.*;
public class Battery extends NetworkComponent {
    private double maxCapacity;
    private double storedEenergy;
    public Battery(String id, double maxCapacity) {
        super(id);
        this.maxCapacity = maxCapacity;
        this.storedEenergy =0.0;
    }
    public double getStoredEenergy() {
        return storedEenergy;
    }
    public double unload(double availableEnergy) {
            this.storedEenergy = this.storedEenergy + availableEnergy;
            if(this.storedEenergy > this.maxCapacity) {
                double surplus = this.storedEenergy - this.maxCapacity;
                this.storedEenergy = this.maxCapacity;
                return surplus;
            }
            else return 0.0;
    }
    public double download(double availableEnergy) {
        double remainingEnergy = this.storedEenergy - availableEnergy;
        if(remainingEnergy < 0.0) {
            double energie_return = this.storedEenergy;
            this.storedEenergy = 0.0;
            return energie_return;
        }
        this.storedEenergy = remainingEnergy;
        return availableEnergy;
    }

    public String showDetails(){
        String storedEnergyFormat = String.format("%.2f",this.storedEenergy);
        String capacityFormat = String.format("%.2f",this.maxCapacity);
        String s = "Batrery " + id + " - Storage: " + storedEnergyFormat + "/" + capacityFormat + " - Status: ";
        if(operationalStatus == true){
            s = s + "Operational\n";
        }
        else s = s + "Neoperational\n";
        return s;
    }
}
