package main.java.grid;

import java.util.ArrayList;

public class GridController {
    private ArrayList<EnergyConsumer> energyConsumer = new ArrayList();
    private ArrayList<EnergyProducer> energyProducer = new ArrayList();
    private ArrayList<Battery> batteries = new ArrayList();
    private ArrayList<String> messages = new ArrayList();
    private int tickNumber = 0;
    private boolean blackout = false;
    public boolean getBlackout() {
        return blackout;
    }
    public String simulateTick(double sunFactor, double windFactor) {
        if (getBlackout() == true) {
            return "ERROR: Network is in BLACKOUT. Simulation stopped";
        }
        tickNumber++;
        for (EnergyConsumer energyConsumer : energyConsumer) {
            energyConsumer.connectToNetwork();
        }
        double totalProduction = 0.0;
        double bTotal = 0.0;
        String disconnectList = "";
        /*energy production calculation*/
        for (EnergyProducer energyProducer : energyProducer) {
            if (energyProducer.operationalStatus) {
                totalProduction = totalProduction + energyProducer.calculateProduction(sunFactor, windFactor);
            }
        }
        double demandTotal = 0.0;
        double deficit = 0.0;
        /*energy demand calculation*/
        for (EnergyConsumer energyConsumer : energyConsumer) {
            if (energyConsumer.operationalStatus) {
                demandTotal = demandTotal + energyConsumer.getCurrentRequest();
            }
        }
        double delta = totalProduction - demandTotal;
        if (delta > 0) {
            for (Battery battery : batteries) {
                if (battery.operationalStatus) {
                    delta = battery.unload(delta);
                    if (delta <= 0.0) {
                        break;
                    }
                }
            }
        } else if (delta < 0) {
            deficit = -delta;
            for (Battery battery : batteries) {
                if (battery.operationalStatus) {
                    deficit = deficit - battery.download(deficit);
                }
            }
        }
        if (deficit > 0) {
            /*triage*/
            /*disconnection in order of priority*/
            for (EnergyConsumer energyConsumer : energyConsumer) {
                if (energyConsumer.getPriority() == 3 && deficit > 0) {
                    energyConsumer.disconnectFromNetwork();
                    deficit = deficit - energyConsumer.getEnergyDemand();
                    if (disconnectList.length() != 0) {
                        disconnectList = disconnectList + ", ";
                    }
                    disconnectList = disconnectList + energyConsumer.id;
                }
            }
            for (EnergyConsumer energyConsumer : energyConsumer) {
                if (energyConsumer.getPriority() == 2 && deficit > 0) {
                    energyConsumer.disconnectFromNetwork();
                    deficit = deficit - energyConsumer.getEnergyDemand();
                    if (disconnectList.length() != 0) {
                        disconnectList = disconnectList + ", ";
                    }
                    disconnectList = disconnectList + energyConsumer.id;
                }
            }
        }
        if (deficit > 0) {
            blackout = true;
            messages.add("Tick " + tickNumber + ": BLACKOUT! SIMULATION OFF.");
            return "BLACKOUT! SIMULATION OFF.";
        }
        for (Battery battery : batteries) {
            bTotal = bTotal + battery.getStoredEenergy();
        }
        String productieTotalaFormat = String.format("%.2f", totalProduction);
        String bTotalFormat = String.format("%.2f", bTotal);
        String cerereTotalaFormat = String.format("%.2f", demandTotal);
        return "TICK: Production " + productieTotalaFormat + ", Demand: " + cerereTotalaFormat + ". Batteries: " + bTotalFormat + " MW. Unplug: [" + disconnectList + "]";
    }

    public int verification(String id){
        /*ID uniqueness check*/
        for(EnergyProducer energyProducer : energyProducer){
            if(energyProducer.id.equals(id)){
                return 0;
            }
        }
        for(EnergyConsumer energyConsumer : energyConsumer){
            if(energyConsumer.id.equals(id)){
                return 0;
            }
        }
        for(Battery battery : batteries){
            if(battery.id.equals(id)){
                return 0;
            }
        }
        return 1;
    }

    public String addProducer(String producerType, String id, double power){
        /*check network status*/
        if(blackout ==true)
            return "ERROR: Network is in BLACKOUT. Simulation stopped.";
        int ok= verification(id);
        if(ok==0){
            return "ERROR: A component with the id already exists. "+id+"\n";
        }
        /*producer type verification*/
        if(producerType.equals("solar")){
            SolarPanel solarProducer = new SolarPanel(power,id);
            energyProducer.add(solarProducer);
        }
        if(producerType.equals("reactor")){
            NuclearReactor reactorProducer = new NuclearReactor(power,id);
            energyProducer.add(reactorProducer);
        }
        if(producerType.equals("turbine")){
            WindTurbine turbineProducer = new WindTurbine(power,id);
            energyProducer.add(turbineProducer);
        }
        return "The producer has been added. "+id+" of type " + producerType +"\n";
    }

    public String addConsumer(String ConsumatorType, String id, double power){
        /*check network status*/
        if(blackout ==true)
            return "ERROR: Network is in BLACKOUT. Simulation stopped.";
        int ok = verification(id);
        if(ok == 0){
            return "ERROR: A component with the id: " + id +"already exists: "+ "\n";
        }
        /*consumer verification*/
        if(ConsumatorType.equals("life_support")){
            LifeSupportSystem lifeSupportSystem = new LifeSupportSystem(id, power);
            energyConsumer.add(lifeSupportSystem);
        }
        if(ConsumatorType.equals("laboratory")){
            ScientificLaboratory lab = new ScientificLaboratory(id, power);
            energyConsumer.add(lab);
        }
        if(ConsumatorType.equals("lighting")){
            LightingSystem system = new LightingSystem(id, power);
            energyConsumer.add(system);
        }
        return "Consumer added " + id + " of type " + ConsumatorType +"\n";
    }

    public String addBattery(String id, double maximumCapacity){
        /*check network status*/
        if(blackout ==true)
            return "ERROR: Network is in BLACKOUT. Simulation stopped.";
        int ok = verification(id);
        if(ok==0){
            return "ERROR: A component with the id " + id + " already exists.\n";
        }
        Battery batteryNew = new Battery(id, maximumCapacity);
        batteries.add(batteryNew);
        return "Battery added" + id + " with capacity " + maximumCapacity + "\n";
    }

    public String statusVerification(String id, boolean OperationalStatus){
        /*check id*/
        int ok= verification(id);
        if (ok == 1)
            return "ERROR: Compound with id does not exist"+ id + "\n";
        /*operational status change*/
        for(EnergyConsumer energyConsumer : energyConsumer){
            if(energyConsumer.id.equals(id)){
                energyConsumer.operationalStatus = OperationalStatus;
                if(energyConsumer.operationalStatus ==true){
                    return "Compound " + id + " is now operational\n";
                }
                else return "Compound " + id + " it is now defective\n";
            }
        }
        for(EnergyProducer energyProducer : energyProducer){
            if(energyProducer.id.equals(id)){
                energyProducer.operationalStatus = OperationalStatus;
                if(energyProducer.operationalStatus ==true){
                    return "Compound " + id + " is now operational\n";
                }
                else return "Compound " + id + " it is now defective\n";
            }
        }
        for(Battery battery : batteries){
            if(battery.id.equals(id)){
                battery.operationalStatus = OperationalStatus;
                if(battery.operationalStatus ==true){
                    return "Compound " + id + " is now operational\n";
                }
                else return "Compound " + id + " it is now defective\n";
            }
        }
        return "";
    }

    public String NetworkState(){
        /*network type*/
        if(batteries.size()==0 && energyProducer.size()==0  && energyConsumer.size()==0){
            return "The network is empty.\n";
        }
        String answer="";
        if(blackout ==true) {
            answer = answer + "Network type: BLACKOUT\n";
        } else {
            answer = answer+"Network type: STABLE" + "\n";
        }
        for (EnergyProducer energyProducer : energyProducer) {
            answer = answer + energyProducer.displayDetails();
        }
        for (EnergyConsumer energyConsumer : energyConsumer) {
            answer = answer + energyConsumer.displayDetails();
        }
        for (Battery battery : batteries) {
            answer = answer + battery.showDetails();
        }
        return answer;
    }
    public String historyTick(){
        String history="";
        for(String s: messages){
            history=history+s+"\n";
        }
        return history;
    }
}
