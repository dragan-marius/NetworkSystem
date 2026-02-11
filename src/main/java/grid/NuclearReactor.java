package main.java.grid;

public class NuclearReactor extends EnergyProducer {
   protected double constantPower;
    NuclearReactor(double constantPower, String id) {
        super(id);
        this.constantPower =constantPower;
    }
    public double calculateProduction(double percentageSun, double windSpeed){
        if (operationalStatus) {
            return constantPower;
        }
        return 0;
    }
    public String displayDetails() {
        String power = String.format("%.2f", this.constantPower);
        String s = "Producator "+id+" (ReactorNuclear) - PutereBaza: " + power + " - Status: ";
        if(operationalStatus == true) {
            s = s + "Operational\n";
        }
        else s = s + "Defect\n";
        return s;
    }
}
