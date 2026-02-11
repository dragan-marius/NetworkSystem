package main.java.grid;

public class Battery extends NetworkComponent {
    private double capacityMaxim;
    private double storedEenergy;
    Battery(String id, double capacityMaxim) {
        super(id);
        this.capacityMaxim = capacityMaxim;
        this.storedEenergy =0.0;
    }
    public double getStoredEenergy() {
        return storedEenergy;
    }
    public double unload(double energieDisponibila) {
            this.storedEenergy = this.storedEenergy + energieDisponibila;
            if(this.storedEenergy > this.capacityMaxim) {
                double surplus = this.storedEenergy - this.capacityMaxim;
                this.storedEenergy = this.capacityMaxim;
                return surplus;
            }
            else return 0.0;
    }
    public double download(double energieCeruta) {
        double energie_ramasa = this.storedEenergy - energieCeruta;
        if(energie_ramasa < 0.0) {
            double energie_return = this.storedEenergy;
            this.storedEenergy = 0.0;
            return energie_return;
        }
        this.storedEenergy = energie_ramasa;
        return energieCeruta;
    }

    public String showDetails(){
        String storedEnergyFormat = String.format("%.2f",this.storedEenergy);
        String capacityFormat = String.format("%.2f",this.capacityMaxim);
        String s = "Baterie " + id + " - Stocare: " + storedEnergyFormat + "/" + capacityFormat + " - Status: ";
        if(operationalStatus == true){
            s = s + "Operational\n";
        }
        else s = s + "Neoperational\n";
        return s;
    }
}
