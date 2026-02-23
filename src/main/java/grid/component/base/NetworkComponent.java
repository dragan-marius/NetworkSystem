package main.java.grid.component.base;

public abstract class NetworkComponent {
    protected String id;
    protected boolean operationalStatus;
    public NetworkComponent(String id) {
        this.id = id;
        this.operationalStatus =true;
    }

    public boolean isOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(boolean operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
