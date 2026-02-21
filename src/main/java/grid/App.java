package main.java.grid;
import java.io.*;
import java.util.*;

public class App {
    private Scanner scanner;

    public App(InputStream input) {
        this.scanner = new Scanner(input);
    }

    public void run() {
        String lineCommand=scanner.nextLine();
        GridController grid = new GridController();
        while(!lineCommand.equals("7")){
            String [] command=lineCommand.split(" ");
            int commandNumber=-1;
            /*order type and error handling*/
            try {
                    commandNumber = Integer.parseInt(command[0]);
            }catch (IllegalArgumentException e){
                System.out.println("ERROR: Unknown command.");
            }
            if(commandNumber>7)
                System.out.println("ERROR: Unknown command.");

            if(commandNumber==0){
                /*add producer*/
                if(command[1].equals("solar") || command[1].equals("reactor") || command[1].equals("turbine") ){
                    if(command.length==4) {
                        /*check number of parameters*/
                        double power = Double.parseDouble(command[3]);
                        if (power > 0.0) {
                            String add = grid.addProducer(command[1], command[2], power);
                            System.out.println(add);
                        } else {
                            System.out.println("ERROR: Invalid power\n");
                        }
                    } else {
                        System.out.println("ERROR: Invalid command format\n");
                    }
                }
                else {
                    System.out.println("ERROR: Invalid producer type\n");
                }
            }

            if(commandNumber==1){
                //add consumer
                if(command[1].equals("life_support") || command[1].equals("laboratory") || command[1].equals("lighting")){
                    if(command.length==4) {
                        /*check number of parameters*/
                        double maxCapacity = Double.parseDouble(command[3]);
                        if (maxCapacity > 0.0) {
                            String add = grid.addConsumer(command[1], command[2], maxCapacity);
                            System.out.println(add);
                        } else {
                            System.out.println("ERROR: Invalid power request\n");
                        }
                    } else {
                        System.out.println("ERROR: Invalid command format\n");
                    }
                }
                else {
                    System.out.println("ERROR: Invalid consumer type\n");
                }
            }

            if(commandNumber==2){
                /*add battery*/
                if(command.length==3) {
                    double capacitate_max = Double.parseDouble(command[2]);
                    if (capacitate_max > 0.0) {
                        String add = grid.addBattery(command[1], capacitate_max);
                        System.out.println(add);
                    } else {
                        System.out.println("ERROR: Invalid capacity\n");
                    }
                } else {
                    System.out.println("ERROR: Invalid command format\n");
                }
            }

            if(commandNumber==3){
                if(command.length==3){
                    try{
                        double sunFactor=Double.parseDouble(command[1]);
                        double windFactor=Double.parseDouble(command[2]);
                        String simulationAnswer=grid.simulateTick(sunFactor, windFactor);
                        System.out.println(simulationAnswer);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR: Invalid factors");
                    }
                }
                else{
                    System.out.println("ERROR: Invalid command format\n");
                }
            }

            if(commandNumber==4){
                /*check status type*/
                if(command[2].equals("true") || command[2].equals("false")){
                    boolean status=Boolean.parseBoolean(command[2]);
                    String verification=grid.statusVerification(command[1],status);
                    System.out.println(verification);
                }
                else System.out.println("ERROR: Invalid status\n");
            }

            if(commandNumber==5){
                String answer=grid.NetworkState();
                System.out.println(answer);
            }

            if(commandNumber==6){
                String history=grid.historyTick();
                if(history.equals("")){
                    System.out.println("Empty event history");
                }
                else
                    System.out.println(history);
            }
            /*next command*/
            lineCommand=scanner.nextLine();
        }
        if(lineCommand.equals("7")) {
            System.out.println("The simulator is closing.\n");
        }

    }

    public static void main(String[] args) {
        App app = new App(System.in);
        app.run();
    }
}