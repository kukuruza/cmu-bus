package bustracker.android.ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import bustracker.android.dblayout.LocalDatabaseConnector;
import bustracker.android.entities.Bus;
import bustracker.android.entities.Stop;
import bustracker.android.exceptions.TrackerException;
import bustracker.common.dblayout.DbStructure;
import bustracker.common.entities.BaseBus;
import bustracker.common.entities.BaseStop;

import android.content.Context;
import android.location.Location;
import android.util.Log;

public class RouteQueryManager implements RouteQueryInterface {
	private final static String TAG = "RouteQueryManager";
	
	//LocalDatabaseConnector db; 
	
	public RouteQueryManager() { }
	
	
	@Override
	// returns list of buses associated with input stop 
	public ArrayList<Bus> getBusesByStop(Context context, Stop stop) 
			throws TrackerException {

		// query for all buses with a particular stop string in ROUTE table
		LocalDatabaseConnector db = new LocalDatabaseConnector(context);
		
		// from Stop to BaseStop
		BaseStop baseStop = new BaseStop(stop.getName(), stop.getStreet1(), stop.getStreet2(),
				                         stop.getLatitude(), stop.getLongitude());
		
		// make request
		ArrayList<BaseBus> baseBusList = db.getBusesForStop(baseStop);
		
		// from BaseBus to Bus
		ArrayList<Bus> busList = new ArrayList<Bus>();
		for (BaseBus baseBus : baseBusList) 
			busList.add (new Bus(baseBus));
		
		db = null; 
		return busList; 
	}

	@Override
	// returns list of stops sorted by distance from user
	public ArrayList<Stop> getStopsByCurrentLocation (Context context, Location here)
			throws TrackerException {
		
		// retrieve all the stops from the database
		ArrayList<Stop> stopList = getAllStops(context);

		// set the distance for each stop to the distance from user to stop
		for (Stop stop : stopList) 
		{
			Location stopLocation = new Location(stop.getName());
			stopLocation.setLatitude( stop.getLatitude() );
			stopLocation.setLongitude( stop.getLongitude() );
			stop.setDistance(here.distanceTo(stopLocation));
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
		for (BaseStop baseStop : baseStopList) 
			stopList.add (new Stop(baseStop));
		
		db = null; 
		return stopList; 
	}

	@Override
	// returns list of stops sorted by relevance to search words
	public ArrayList<Stop> getStopByAddress(Context context, Location here, String street)
			throws TrackerException {
		
		street = DbStructure.cleanupStreetQuery(street);
		Log.e(TAG, "clean street name: " + street);
		
		//retrieves all stops from the database that have a matching street
		LocalDatabaseConnector db = new LocalDatabaseConnector(context);
		ArrayList<BaseStop> baseStopList = db.getStopsByStreet(street);
		db = null;
		
		// BaseStops to Stops
		ArrayList<Stop> stopList = new ArrayList<Stop>();
		for (BaseStop baseStop : baseStopList) 
			stopList.add (new Stop(baseStop));
		
		// set the distance for each stop to the distance from user to stop
		for (Stop stop : stopList) 
		{
			Location stopLocation = new Location(stop.getName());
			stopLocation.setLatitude( stop.getLatitude() );
			stopLocation.setLongitude( stop.getLongitude() );
			stop.setDistance(here.distanceTo(stopLocation));
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
