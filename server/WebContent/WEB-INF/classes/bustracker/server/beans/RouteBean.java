package bustracker.server.beans;

import java.util.ArrayList;

public class RouteBean {
    String stopName;
    ArrayList<String> busNames;    
    ArrayList<String> busDirs;
    
    public String getStopName() { return stopName; }
    public ArrayList<String> getBusNames() { return busNames; }
    public ArrayList<String> getBusDirs() { return busDirs; }
    
    public void setStopName (String stopName) { this.stopName = stopName; }
    public void setBusNames (ArrayList<String> busNames) { this.busNames = busNames; }
    public void setBusDirs (ArrayList<String> busDirs) { this.busDirs = busDirs; }
}
