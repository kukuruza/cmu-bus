package bustracker.android.ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import bustracker.android.dblayout.LocalDatabaseConnector;
import bustracker.android.entities.Bus;
import bustracker.android.entities.Stop;
import bustracker.android.exceptions.TrackerException;
import bustracker.common.entities.BaseBus;
import bustracker.common.entities.BaseStop;

import android.content.Context;
import android.location.Location;

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
		
		BaseStop baseStop = new BaseStop(stop.getName(), stop.getStreet1(), stop.getStreet2(),
				                         stop.getLocation().getLatitude(), stop.getLocation().getLongitude());
		
		ArrayList<BaseBus> baseBusList = db.getBusesForStop(baseStop);
		
		ArrayList<Bus> busList = new ArrayList<Bus>();
		for(int i = 0; i < baseBusList.size(); i++) 
		{
			BaseBus baseBus = baseBusList.get(i);
			busList.add (new Bus(baseBus));
		}		
		
		db = null; 
		return busList; 
		
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

	}
	
	@Override
	// return complete list of stops
	public ArrayList<Stop> getAllStops(Context context) throws TrackerException {
		
		// retrieves all stops from database
		LocalDatabaseConnector db = new LocalDatabaseConnector(context);
		ArrayList<BaseStop> baseStopList = db.getAllStops();

		// BaseStops to Stops
		ArrayList<Stop> stopList = new ArrayList<Stop>();
		for(int i = 0; i < baseStopList.size(); i++) 
			stopList.add (new Stop( baseStopList.get(i) ));
		
		db = null; 
		return stopList; 
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
		ArrayList<BaseStop> baseStopList = db.getStopsByStreet(street);
		db = null;
		
		// BaseStops to Stops
		ArrayList<Stop> stopList = new ArrayList<Stop>();
		for(int i = 0; i < baseStopList.size(); i++) 
			stopList.add ( new Stop( baseStopList.get(i) ));
		
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
	
	}

}
