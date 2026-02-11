package main.java.grid;

public abstract class NetworkComponent {
    protected String id;
    protected boolean operationalStatus;
    NetworkComponent(String id) {
        this.id = id;
        this.operationalStatus =true;
    }
}
