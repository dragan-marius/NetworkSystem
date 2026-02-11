package main.java.grid;

public abstract class EnergyProducer extends NetworkComponent {
    public abstract double calculateProduction(double percentageSun, double windSpeed);
    EnergyProducer(String id) {
        super(id);
    }
    public abstract String displayDetails();

}
