package bustracker.android.ws;

import java.util.ArrayList;

import bustracker.android.entities.Bus;
import bustracker.android.entities.Stop;
import bustracker.android.exceptions.TrackerException;

import android.content.Context;
import android.location.Location;

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
