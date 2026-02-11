package main.java.grid;

public class SolarPanel extends EnergyProducer {
    private double maximumPower;
    SolarPanel(double maximumPower, String id) {
        super(id);
        this.maximumPower = maximumPower;
    }
    public double calculateProduction(double percentageSun, double windSpeed) {
        return percentageSun* maximumPower;
    }
    public String displayDetails() {
        String power = String.format("%.2f",this.maximumPower);
        String s = "Producator "+id+" (PanouSolar) - PutereBaza: "+power+" - Status: ";
        if(operationalStatus ==true){
            s = s+"Operational\n";
        }
        else s = s + "Defect\n";
        return s;
    }
}
