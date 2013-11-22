package cmu18641.bustracker.ws;

import java.util.ArrayList;
import java.util.Random;

import android.location.Location;
import android.util.Log;
import cmu18641.bustracker.dblayout.LocalDatabaseConnector;
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
		String names[] = {"A Route Shuttle", "B Route Shuttle", "AB Route Shuttle", 
				"Bakery Square Shuttle", "PTC Shuttle", "61A", "61B", "61C", "61D", "67", "83" };

		ArrayList<Bus> busList = new ArrayList<Bus>(); 
		
		
		Random generator = new Random(); 
		String direction; 
		Bus temp; 
		
		for(int i = 0; i < names.length; i++){
			 direction =  (generator.nextInt(2) != 0) ? "west": "east";
			 temp = new Bus(names[i], direction);		 
			 busList.add(temp);       
		}
		
		return busList; 
		
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
		
		Log.v("RouteQueryManager", "getStopsByCurrentLocation"); 
		
		String names[] = {"Morewood and Forbes", "5th and Craig", "Forbes and Craig", 
				"Mellwood and Fescher", "Bayard and Craig", "Gale and Smears", "Morewood and Bayard" };
	
		ArrayList<Stop> stopList = new ArrayList<Stop>(); 
		Stop temp; 
		
		// for testing
		for(int i = 0; i < names.length; i++){
			 temp = new Stop(names[i], "West Street", "East Street", new Location("loc"));		 
			 stopList.add(temp);       
		}
		
		return stopList; 
	}
	
	// return complete list of stops
	public ArrayList<Stop> getAllStops() { 
		ArrayList<Stop> stops = new ArrayList<Stop>(); 
		return stops; 
	}

	@Override
	// returns list of stops sorted by relevance to search words
	public ArrayList<Stop> getStopByAddress(String street)
			throws TrackerException {
		
		Log.v("RouteQueryManager", "getStopsByAddress");

		String names[] = {"Morewood and Forbes", "5th and Craig", "Forbes and Craig", 
				"Mellwood and Fescher", "Bayard and Craig", "Gale and Smears", "Morewood and Bayard" };
	
		ArrayList<Stop> stopList = new ArrayList<Stop>(); 
		Stop temp; 
		
		// for testing
		for(int i = 0; i < names.length; i++){
			 temp = new Stop(names[i], "West Street", "East Street", new Location("loc"));		 
			 stopList.add(temp);       
		}
		
		return null;
	
	}

}
