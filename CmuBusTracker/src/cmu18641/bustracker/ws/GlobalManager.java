package cmu18641.bustracker.ws;

import java.util.ArrayList;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import cmu18641.bustracker.dblayout.LocalDatabaseConnector;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import cmu18641.bustracker.helpers.LocationService;
import cmu18641.bustracker.ws.remote.GetDatabaseQuery;

/*
 * GlobalManager.java
 * 
 * The manager enforces the interface between the activities
 * and query managers, and provides connectivity to the GPS
 * and network modules. 
 */

public class GlobalManager {
	private static final String TAG = "GlobalManager"; 

	RouteQueryManager routeQueryManager; 
	TimeQueryManager timeQueryManager; 
	LocationService locationService; 
	
	public GlobalManager() { 
		routeQueryManager = new RouteQueryManager(); 
		timeQueryManager = new TimeQueryManager(); 
	}
	
	// returns list of buses associated with input stop 
	public ArrayList<Bus> getBusesByStop(Context context, Stop stop) throws TrackerException {
		Log.i("GlobalManager", "getBusesByStop"); 
		return routeQueryManager.getBusesByStop(context, stop); 
	}

	// returns list of stops sorted by distance from user
	public ArrayList<Stop> getStopsByCurrentLocation(Context context) throws TrackerException {    
		Log.i("GlobalManager", "getStopsByCurrentLocation"); 
		
		Location userLocation = fetchLocation(context); 
        
        if(userLocation.getLatitude() != 0.0 && userLocation.getLongitude() != 0.0)
        	return routeQueryManager.getStopsByCurrentLocation(context, userLocation); 
        else { 
        	return routeQueryManager.getAllStops(context); 
        }
	}

	// returns list of stops sorted by relevance to search words
	public ArrayList<Stop> getStopByAddress(Context context, String street) throws TrackerException {
		Log.i("GlobalManager", "getStopByAddress"); 
		ArrayList<Stop> stopList = new ArrayList<Stop>(); 
		
		Location userLocation = fetchLocation(context); 
        
        if(userLocation.getLatitude() != 0.0 && userLocation.getLongitude() != 0.0)
        	stopList = routeQueryManager.getStopByAddress(context, userLocation, street); 
        
		return stopList; 
	}
	
	// fetch location from location service 
	private Location fetchLocation(Context context) { 
		
		locationService = new LocationService(context); 
		Location userLocation = new Location("user"); 
		
        if(locationService.canGetLocation() && locationService.getLocation() != null) {
            userLocation = new Location(locationService.getLocation()); 
            Log.d("Manager", "userLocation=" + locationService.getLatitude() + " " 
            			+ locationService.getLongitude());
        }
 
        //locationService.stopUsingLocation(); 
        //locationService = null; 
        
        Log.d("location", userLocation.getLatitude() + " " + userLocation.getLongitude());
		
		return userLocation; 
	}
	
	public Schedule getSchedule (Context context, Stop stop, ArrayList<Bus> buses) {
		return timeQueryManager.getSchedule (context, stop, buses);
	}
	
	// updated database to be saved 
	public void updateDatabase(Context context) 
	{ 
		try {
			// get path to private data directory
			String databasePath = context.getApplicationInfo().dataDir + "/databases";
			
			// delete old database
			context.deleteFile(LocalDatabaseConnector.DATABASE_NAME);
			
			// download new database to apps private data directory
			GetDatabaseQuery newDb = new GetDatabaseQuery(); 
			try {
				newDb.downloadDb(context, databasePath + "/" + LocalDatabaseConnector.DATABASE_NAME);
			} catch (TrackerException e) {
				e.printStackTrace();
			}
			
			// increment version
			LocalDatabaseConnector.incrementDbVersion();
			
		} catch (Exception e) {
			Log.e (TAG, "Could not update the database");
			
		}
		
	}
}
