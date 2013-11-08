package cmu18641.bustracker.entities;

import java.util.Locale;
import android.location.Location;
import android.text.format.Time;


// Note:
//   This class is to be used for output of Stop at wireframes 3 - 5


public class StopAdapter extends Stop {
	
	final static private float MetersToMiles = (float) 0.000621371;
	final static private String MilesStringFormat = "%.1f";
	final static private float AvgSpeedMilesPerMin = (float)(3) / 60;
	
	public StopAdapter (Stop stop)
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
	protected float getDistance (Location here)
	{
	    return getLocation().distanceTo(here) * MetersToMiles;
	}
	
	// return string in miles
	public String getDistanceString (Location here)
	{
		return String.format (Locale.US, MilesStringFormat, getDistance(here));
	}
	
	// return string in minutes
	public String getWalkingTimeString (Location here, Time now)
	{
		return String.format (Locale.US, "%f.", getDistance(here) / AvgSpeedMilesPerMin);
	}
}
