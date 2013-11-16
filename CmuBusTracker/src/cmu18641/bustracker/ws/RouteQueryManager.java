package cmu18641.bustracker.ws;

import java.util.ArrayList;
import android.location.Location;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.RouteQueryInterface;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;


public class RouteQueryManager implements RouteQueryInterface {
	
	@Override
	// returns list of buses associated with input stop 
	public ArrayList<Bus> getBusesByStop(Stop stop)
			throws TrackerException {
		// TODO
		// query for all buses with a particular stop string in ROUTE table
		
		// for testing 
		// populate busList with test data
		//String names[] = new String[50];
		//
		// for(int i = 0; i< 50; i++){
		//	 names[i] = "BusName" + i; 
		//	 Bus temp = new Bus(names[i], "West");		 
		//	 busList.add(temp);       
		// }
		
		
		
		return null;
	}

	@Override
	// returns list of stops sorted by distance from user
	public ArrayList<Stop> getStopsByCurrentLocation(Location here)
			throws TrackerException {
		// TODO
		// run query to get all stops in STOPS table
		// run through list of stops, setting distance in stops class
		    // based on stops location and user location
		// sort list by distance in stop class
		
		return null;
	}

	@Override
	// returns list of stops sorted by relevance to search words
	public ArrayList<Stop> getStopByAddress(String street)
			throws TrackerException {
		// TODO
		// query for all stops matching string in STOPS table
		return null;
	}

}
