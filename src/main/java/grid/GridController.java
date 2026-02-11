package main.java.grid;

import java.util.ArrayList;

public class GridController {
    private ArrayList<EnergyConsumer> consumatoriEnergie = new ArrayList();
    private ArrayList<EnergyProducer> producatoriEnergie = new ArrayList();
    private ArrayList<Battery> baterii = new ArrayList();
    private ArrayList<String> mesaje = new ArrayList();
    private int nr_tickuri = 0;
    private boolean esteInBlackout = false;
    public boolean getEsteInBlackout() {
        return esteInBlackout;
    }
    public String simuleazaTick(double factorSoare,double factorVant) {
        if (getEsteInBlackout() == true) {
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita";
        }
        nr_tickuri++;
        for (EnergyConsumer energyConsumer : consumatoriEnergie) {
            energyConsumer.connectToNetwork();
        }
        double productieTotala = 0.0;
        double bTotal = 0.0;
        String listaDecuplati = "";
        //calcul productie de energie
        for (EnergyProducer energyProducer : producatoriEnergie) {
            if (energyProducer.operationalStatus) {
                productieTotala = productieTotala + energyProducer.calculateProduction(factorSoare, factorVant);
            }
        }
        double cerereTotala = 0.0;
        double deficit = 0.0;
        //calcul cerere energie
        for (EnergyConsumer energyConsumer : consumatoriEnergie) {
            //cerereTotala = cerereTotala + consumatorEnergie.getCerereCurenta();
            if (energyConsumer.operationalStatus) {
                cerereTotala = cerereTotala + energyConsumer.getCurrentRequest();
            }
        }
        double delta = productieTotala - cerereTotala;
        if (delta > 0) {
            for (Battery battery : baterii) {
                if (battery.operationalStatus) {
                    delta = battery.unload(delta);
                    if (delta <= 0.0) {
                        break;
                    }
                }
            }
        } else if (delta < 0) {
            deficit = -delta;
            for (Battery battery : baterii) {
                if (battery.operationalStatus) {
                    deficit = deficit - battery.download(deficit);
                }
            }
        }
        if (deficit > 0) {
            //triage
            //decuplare in ordinea prioritatii
            for (EnergyConsumer energyConsumer : consumatoriEnergie) {
                if (energyConsumer.getPriority() == 3 && deficit > 0) {
                    energyConsumer.disconnectFromNetwork();
                    deficit = deficit - energyConsumer.getEnergyDemand();
                    if (listaDecuplati.length() != 0) {
                        listaDecuplati = listaDecuplati + ", ";
                    }
                    listaDecuplati = listaDecuplati + energyConsumer.id;
                }
            }
            for (EnergyConsumer energyConsumer : consumatoriEnergie) {
                if (energyConsumer.getPriority() == 2 && deficit > 0) {
                    energyConsumer.disconnectFromNetwork();
                    deficit = deficit - energyConsumer.getEnergyDemand();
                    if (listaDecuplati.length() != 0) {
                        listaDecuplati = listaDecuplati + ", ";
                    }
                    listaDecuplati = listaDecuplati + energyConsumer.id;
                }
            }
        }
        if (deficit > 0) {
            esteInBlackout = true;
            mesaje.add("Tick " + nr_tickuri + ": BLACKOUT! SIMULARE OPRITA.");
            // nr_tickuri++;
            return "BLACKOUT! SIMULARE OPRITA.";
        }
        for (Battery battery : baterii) {
            bTotal = bTotal + battery.getStoredEenergy();
        }
        String productieTotalaFormat = String.format("%.2f", productieTotala);
        String bTotalFormat = String.format("%.2f", bTotal);
        String cerereTotalaFormat = String.format("%.2f", cerereTotala);
        return "TICK: Productie " + productieTotalaFormat + ", Cerere " + cerereTotalaFormat + ". Baterii: " + bTotalFormat + " MW. Decuplati: [" + listaDecuplati + "]";
    }

    public int verificare(String id){
        //verificare unicitate id
        for(EnergyProducer energyProducer :producatoriEnergie){
            if(energyProducer.id.equals(id)){
                return 0;
            }
        }
        for(EnergyConsumer energyConsumer :consumatoriEnergie){
            if(energyConsumer.id.equals(id)){
                return 0;
            }
        }
        for(Battery battery :baterii){
            if(battery.id.equals(id)){
                return 0;
            }
        }
        return 1;
    }

    public String adaugareProducator(String tip_producator,String id, double putere){
        //verificare stare retea
        if(esteInBlackout==true)
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita.";
        int ok=verificare(id);
        if(ok==0){
            return "EROARE: Exista deja o componenta cu id-ul "+id+"\n";
        }
        //verificare tip producator
        if(tip_producator.equals("solar")){
            SolarPanel producator_solar = new SolarPanel(putere,id);
            producatoriEnergie.add(producator_solar);
        }
        if(tip_producator.equals("reactor")){
            NuclearReactor producator_reactor = new NuclearReactor(putere,id);
            producatoriEnergie.add(producator_reactor);
        }
        if(tip_producator.equals("turbina")){
            WindTurbine producator_turbina = new WindTurbine(putere,id);
            producatoriEnergie.add(producator_turbina);
        }
        return "S-a adaugat producatorul "+id+" de tip " + tip_producator+"\n";
    }

    public String adaugareConsumator(String tip_consumator, String id, double putere){
        //verificare stare retea
        if(esteInBlackout==true)
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita.";
        int ok = verificare(id);
        if(ok == 0){
            return "EROARE: Exista deja o componenta cu id-ul " + id + "\n";
        }
        //verificare tip consumator
        if(tip_consumator.equals("suport_viata")){
            LifeSupportSystem lifeSupportSystem = new LifeSupportSystem(id, putere);
            consumatoriEnergie.add(lifeSupportSystem);
        }
        if(tip_consumator.equals("laborator")){
            ScientificLaboratory lab = new ScientificLaboratory(id,putere);
            consumatoriEnergie.add(lab);
        }
        if(tip_consumator.equals("iluminat")){
            LightingSystem sistem = new LightingSystem(id,putere);
            consumatoriEnergie.add(sistem);
        }
        return "S-a adaugat consumatorul " + id + " de tip " + tip_consumator+"\n";
    }

    public String adaugareBaterie(String id, double capacitate_maxima){
        //verificare stare retea
        if(esteInBlackout==true)
            return "EROARE: Reteaua este in BLACKOUT. Simulare oprita.";
        int ok = verificare(id);
        if(ok==0){
            return "EROARE: Exista deja o componenta cu id-ul " + id + "\n";
        }
        Battery batteryNew = new Battery(id, capacitate_maxima);
        baterii.add(batteryNew);
        return "S-a adaugat bateria " + id + " cu capacitatea " + capacitate_maxima + "\n";
    }

    public String verificareStatus(String id,boolean status_operational){
        //verificare id
        int ok=verificare(id);
        if (ok == 1)
            return "EROARE: Nu exista componenta cu id-ul " + id + "\n";
        //schimbare status operational
        for(EnergyConsumer energyConsumer :consumatoriEnergie){
            if(energyConsumer.id.equals(id)){
                energyConsumer.operationalStatus =status_operational;
                if(energyConsumer.operationalStatus ==true){
                    return "Componenta " + id + " este acum operationala\n";
                }
                else return "Componenta " + id + " este acum defecta\n";
            }
        }
        for(EnergyProducer energyProducer :producatoriEnergie){
            if(energyProducer.id.equals(id)){
                energyProducer.operationalStatus =status_operational;
                if(energyProducer.operationalStatus ==true){
                    return "Componenta " + id + " este acum operationala\n";
                }
                else return "Componenta " + id + " este acum defecta\n";
            }
        }
        for(Battery battery :baterii){
            if(battery.id.equals(id)){
                battery.operationalStatus =status_operational;
                if(battery.operationalStatus ==true){
                    return "Componenta " + id + " este acum operationala\n";
                }
                else return "Componenta " + id + " este acum defecta\n";
            }
        }
        return "";
    }

    public String stareRetea(){
        //tipul retelei
        if(baterii.size()==0 && producatoriEnergie.size()==0  && consumatoriEnergie.size()==0){
            return "Reteaua este goala\n";
        }
        String raspuns="";
        if(esteInBlackout==true) {
            raspuns = raspuns + "Stare Retea: BLACKOUT\n";
        } else {
            raspuns = raspuns+"Stare Retea: STABILA" + "\n";
        }
        for (EnergyProducer energyProducer : producatoriEnergie) {
            raspuns = raspuns + energyProducer.displayDetails();
        }
        for (EnergyConsumer energyConsumer : consumatoriEnergie) {
            raspuns = raspuns + energyConsumer.displayDetails();
        }
        for (Battery battery : baterii) {
            raspuns = raspuns + battery.showDetails();
        }
        return raspuns;
    }
    public String afisareIstoriTick(){
        String istoric="";
        for(String s:mesaje){
            istoric=istoric+s+"\n";
        }
        return istoric;
    }
}
