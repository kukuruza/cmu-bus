package beans;

import java.util.ArrayList;

import cmu18641.bustracker.common.entities.BaseStop;

public class AllStopsBean {
    ArrayList<BaseStop> stops;
    
    public void setStops (ArrayList<BaseStop> stops) { this.stops = stops; }
    public ArrayList<BaseStop> getStops () { return stops; }
}
