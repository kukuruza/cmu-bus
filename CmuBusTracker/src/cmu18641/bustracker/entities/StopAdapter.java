package cmu18641.bustracker.entities;

import cmu18641.bustracker.exceptions.TrackerException;
import android.location.Location;
import android.text.format.Time;


// Note:
//   This class is to be used for output of Stop at wireframes 3 - 5


public class StopAdapter extends Stop {
	
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
	
	public float getDistance (Location here)
	{
	    return -1;
	}
	
	// return string in miles
	public String getDistanceString (Location here)
	{
		return "";
	}
	
	// return string in minutes
	public String getWalkingTimeString (Location here, Time now)
	{
		return "";
	}
}
