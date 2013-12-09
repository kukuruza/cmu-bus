package cmu18641.bustracker.common.dblayout;

import java.util.ArrayList;

import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.Stop;


public interface DbConnectorInterface {

	public ArrayList<Stop> getAllStops();
	
	public ArrayList<Stop> getStopsByStreet (String street);
	
	public Schedule getSchedule (String stopName, ArrayList<String> busesNames, ArrayList<String> busesDirs);
	
	public ArrayList<Bus> getBusesForStop (Stop stop);
	
}
