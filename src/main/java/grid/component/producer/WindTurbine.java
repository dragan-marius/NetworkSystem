package main.java.grid.component.producer;

public class WindTurbine extends EnergyProducer {
    private double basePower;

    public WindTurbine(double basePower, String id) {
        super(id);
        this.basePower = basePower;
    }

    public double calculateProduction(double percentageSun, double windSpeed) {
        return windSpeed * basePower;
    }

    public String displayDetails() {
        String power = String.format("%.2f",this.basePower);
        String s = "Producer " + id + " (WindTurbine) - BasePower: " + power+ " - Status: ";
        if(operationalStatus == true){
            s = s+"Operational\n";
        }
        else s = s + "Defect\n";
        return s;
    }
}
