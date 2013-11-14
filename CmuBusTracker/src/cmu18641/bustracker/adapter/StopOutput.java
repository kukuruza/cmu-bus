package cmu18641.bustracker.adapter;

import java.util.Locale;

import cmu18641.bustracker.entities.Stop;
import android.location.Location;


// Note:
//   This class is to be used for output of Stop at wireframes 3 - 5


public class StopOutput extends Stop {
	
	final static private float MetersToMiles = (float) 0.000621371;
	final static private String MilesStringFormat = "%.1f";
	final static private float AvgSpeedMilesPerMin = (float)(3) / 60;
	
	private Location _here = null;
	
	
	public void setLocation (Location here)
	{
		_here = here;
	}
	
	public StopOutput (Stop stop)
	{
		super (stop);
	}
	
	public String getNameString ()
	{
	    return getName();
	}
	
	public String getAddressString ()
	{
		return new String (getStreet1() + " & " + getStreet2());
	}
	
	// in miles
	protected float getDistance ()
	{
	    return getLocation().distanceTo(_here) * MetersToMiles;
	}
	
	// return string in miles
	public String getDistanceString ()
	{
		return String.format (Locale.US, MilesStringFormat, getDistance());
	}
	
	// return string in minutes
	public String getWalkingTimeString ()
	{
		return String.format (Locale.US, "%f.", getDistance() / AvgSpeedMilesPerMin);
	}
}
