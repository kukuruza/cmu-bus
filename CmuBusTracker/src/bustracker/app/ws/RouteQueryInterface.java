package bustracker.app.ws;

import java.util.ArrayList;

import bustracker.app.entities.Bus;
import bustracker.app.entities.Stop;
import bustracker.app.exceptions.TrackerException;

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
