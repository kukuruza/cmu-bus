package cmu18641.bustracker.entities;

import java.util.ArrayList;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import cmu18641.bustracker.exceptions.TrackerException;
import cmu18641.bustracker.ws.RouteQueryManager;
import cmu18641.bustracker.ws.TimeQueryManager;

/*
 * GlobalManager.java
 * 
 * The manager enforces the interface between the activities
 * and query managers, and provides connectivity to the GPS
 * and network modules. 
 */

public class GlobalManager {

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
		locationService = new LocationService(context); 
		Location userLocation = new Location("user"); 
		
        if(locationService.canGetLocation()) {
            userLocation = new Location(locationService.getLocation()); 
            Log.d("Manager", "userLocation=" + locationService.getLatitude() + " " 
            			+ locationService.getLongitude());
        }
        else {
            locationService.showSettingsAlert();
        }
        
        locationService.stopUsingLocation(); 
        
        if(userLocation.getLatitude() != 0.0 && userLocation.getLongitude() != 0.0)
        	return routeQueryManager.getStopsByCurrentLocation(context, userLocation); 
        else { 
        	return routeQueryManager.getAllStops(context); 
        }
	}

	// returns list of stops sorted by relevance to search words
	public ArrayList<Stop> getStopByAddress(Context context, String street) throws TrackerException {
		Log.i("GlobalManager", "getStopByAddress"); 
		return routeQueryManager.getStopByAddress(context, street); 
	}
	
	// returns schedule for a stop and list of buses
	public Schedule getSchedule(Context context, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException {
		Log.i("GlobalManager", "getSchedule"); 
		return timeQueryManager.getSchedule(context, stop, buses); 
	}
	
}
