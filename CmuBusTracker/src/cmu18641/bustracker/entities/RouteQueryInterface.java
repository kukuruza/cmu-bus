package cmu18641.bustracker.entities;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import cmu18641.bustracker.exceptions.TrackerException;

public interface RouteQueryInterface {

	public ArrayList<Bus> getBusesByStop (Context context, Stop stop) 
			throws TrackerException;
	
	public ArrayList<Stop> getStopsByCurrentLocation (Context context, Location here) 
			throws TrackerException;
	
	public ArrayList<Stop> getStopByAddress (Context context, Location here, String street) 
			throws TrackerException;
	
	public ArrayList<Stop> getAllStops(Context context) 
			throws TrackerException;
}
