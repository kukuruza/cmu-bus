package bustracker.entities;

import bustracker.exceptions.TrackerException;
import android.location.Location;
import android.text.format.Time;


// Note:
//   This class is to be used for output of Stop at wireframes 3 - 5


public class StopAdapter extends Stop {
	
	public StopAdapter (Stop stop)
	{
		super (stop);
	}
	
	String getNameString () throws TrackerException
	{
	    return getName();
	}
	
	String getAddressString () throws TrackerException
	{
		return new String (getStreet1() + " & " + getStreet2());
	}
	
	String getDistanceString (Location here) throws TrackerException
	{
		return "";
	}
	
	String getWalkingTimeString (Location here, Time now) throws TrackerException
	{
		return "";
	}
}
