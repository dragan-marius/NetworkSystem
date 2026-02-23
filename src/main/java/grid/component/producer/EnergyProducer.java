package main.java.grid.component.producer;

import main.java.grid.component.base.*;
public abstract class EnergyProducer extends NetworkComponent {
    public abstract double calculateProduction(double percentageSun, double windSpeed);
    public EnergyProducer(String id) {
        super(id);
    }
    public abstract String displayDetails();

}
