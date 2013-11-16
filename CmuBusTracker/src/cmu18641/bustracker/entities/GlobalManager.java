package cmu18641.bustracker.entities;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import cmu18641.bustracker.SearchStation;
import cmu18641.bustracker.SelectStationAndBus;
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
	
	Context context;
	
	public GlobalManager() { 
		routeQueryManager = new RouteQueryManager(); 
		timeQueryManager = new TimeQueryManager(); 
	}
	
	// returns list of buses associated with input stop 
	public ArrayList<Bus> getBusesByStop(Stop stop) throws TrackerException {
		return routeQueryManager.getBusesByStop(stop); 
	}

	// returns list of stops sorted by distance from user
	public ArrayList<Stop> getStopsByCurrentLocation(Context context) throws TrackerException {    
		locationService = new LocationService(context); 
		Location userLocation = new Location("userLocation"); 
		userLocation.setLatitude(5.0); 
		userLocation.setLongitude(10.0);
		
        if(locationService.canGetLocation()) {
            //userLocation = new Location(locationService.getLocation()); 
            Log.v("Manager", "userLocation=" + locationService.getLatitude() + " " + locationService.getLongitude());
            //Log.v("Manager", "userLocation=" + userLocation.getLatitude() + " " + userLocation.getLongitude());
            
        }
        else {
            locationService.showSettingsAlert();
        }
        
        locationService.stopUsingLocation(); 
        
        if(userLocation.getLatitude() != 0.0 && userLocation.getLongitude() != 0.0)
        	return routeQueryManager.getStopsByCurrentLocation(userLocation); 
        else { 
        	return null; //routeQueryManager.getStops(); 
        }
	}

	// returns list of stops sorted by relevance to search words
	public ArrayList<Stop> getStopByAddress(String street) throws TrackerException {
		return routeQueryManager.getStopByAddress(street); 
	}
	
	// return schedule for a stop and buses
	public Schedule getSchedule(Stop stop, ArrayList<Bus> buses) throws TrackerException {
		// TODO 
		// if there is network access
			return timeQueryManager.getSchedule(stop, buses); 
		// else throw error
			
	}
	
}
