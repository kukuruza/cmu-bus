package cmu18641.bustracker.ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.content.Context;
import android.location.Location;
import cmu18641.bustracker.dblayout.LocalDatabaseConnector;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.RouteQueryInterface;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;

public class RouteQueryManager implements RouteQueryInterface {
	
	//LocalDatabaseConnector db; 
	
	public RouteQueryManager() { 
		/* empty */
	}
	
	@Override
	// returns list of buses associated with input stop 
	public ArrayList<Bus> getBusesByStop(Context context, Stop stop) 
			throws TrackerException {

		// query for all buses with a particular stop string in ROUTE table
		LocalDatabaseConnector db = new LocalDatabaseConnector(context);
		ArrayList<Bus> busList = db.selectAllBusesByStop(stop);
		db = null; 
		return busList; 
		
		/* test data
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
		*/
		
	}

	@Override
	// returns list of stops sorted by distance from user
	public ArrayList<Stop> getStopsByCurrentLocation(Context context, Location here)
			throws TrackerException {
		
		// retrieve all the stops from the database
		ArrayList<Stop> stopList = getAllStops(context);

		// set the distance for each stop to the distance from user to stop
		for(int i = 0; i < stopList.size(); i++) {
			stopList.get(i).setDistance(here.distanceTo(stopList.get(i).getLocation()));
		}
		
		// sort the stops by distance
		Collections.sort(stopList, new Comparator<Stop>() {
	        public int compare(Stop s1, Stop s2) {
	            return (int)(s1.getDistance() - s2.getDistance());
	        }
	    });
		
		return stopList;
		
		/* test data
		Log.v("RouteQueryManager", "getStopsByCurrentLocation"); 
		String names[] = {"Morewood and Forbes", "5th and Craig", "Forbes and Craig", 
				"Mellwood and Fescher", "Bayard and Craig", "Gale and Smears", "Morewood and Bayard" };
		ArrayList<Stop> stopList = new ArrayList<Stop>(); 
		Stop temp; 
		for(int i = 0; i < names.length; i++){
			 temp = new Stop(names[i], "West Street", "East Street", new Location("loc"));		 
			 stopList.add(temp);       
		}
		return stopList; 
		*/
	}
	
	@Override
	// return complete list of stops
	public ArrayList<Stop> getAllStops(Context context) throws TrackerException {
		
		// retrieves all stops from database
		LocalDatabaseConnector db = new LocalDatabaseConnector(context);
		ArrayList<Stop> stops = db.selectAllStops(); 
		db = null; 
		return stops; 
	}

	@Override
	// returns list of stops sorted by relevance to search words
	public ArrayList<Stop> getStopByAddress(Context context, Location here, String street)
			throws TrackerException {
		
		//cleanup user street query entry
		street = street.toLowerCase(Locale.US); 
		street = street.replace("and", ""); 
		street = street.replace("street", "");
		street = street.replace("avenue", ""); 
		street = street.replace("road", ""); 
		street = street.trim(); 
		
		// capitilize first letter
		street = street.substring(0, 1).toUpperCase(Locale.US) + street.substring(1);

		//retrieves all stops from the database that have a matching street
		LocalDatabaseConnector db = new LocalDatabaseConnector(context);
		ArrayList<Stop> stopList = db.selectAllStopsByStreet(street);
		db = null;
		
		// set the distance for each stop to the distance from user to stop
		for(int i = 0; i < stopList.size(); i++) {
			stopList.get(i).setDistance(here.distanceTo(stopList.get(i).getLocation()));
		}
				
		// sort the stops by distance
		Collections.sort(stopList, new Comparator<Stop>() {
			 public int compare(Stop s1, Stop s2) {
			      return (int)(s1.getDistance() - s2.getDistance());
			 }
		});
		
		
		return stopList; 
		
		/* test data
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
		return stopList; 
		*/
	
	}

}
