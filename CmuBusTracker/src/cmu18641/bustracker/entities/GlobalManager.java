package cmu18641.bustracker.entities;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
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
	
	Context context = null;
	
	public GlobalManager() { 
		routeQueryManager = new RouteQueryManager(); 
		timeQueryManager = new TimeQueryManager(); 
		locationService = new LocationService(context); 
	}
	
	// returns list of buses associated with input stop 
	public ArrayList<Bus> getBusesByStop(Stop stop) throws TrackerException {
		return routeQueryManager.getBusesByStop(stop); 
	}

	// returns list of stops sorted by distance from user
	public ArrayList<Stop> getStopsByCurrentLocation() throws TrackerException {    
		Location userLocation = new Location("userLocation"); 
		
        if(locationService.canGetLocation()) {
            userLocation = locationService.getLocation(); 
        }
        else {
            locationService.showSettingsAlert();
        }
        
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
