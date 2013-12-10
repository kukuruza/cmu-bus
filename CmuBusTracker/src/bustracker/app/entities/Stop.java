package bustracker.app.entities;

import java.util.Locale;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Stop implements Parcelable {

	final static private double MetersToMiles = 0.0006213727;  // 1609.34 meters per mile
	final static private double AvgSpeedMilesPerMin = 0.05; // 3 miles per hour
	final static private String StringFormat = "%.1f";
	
	private String     _name;
	private String     _street1;
	private String     _street2;
	private Location   _location;
	private double	   _distance; 
	
	// getters
	public String   getName()     { return new String(_name); }
	public String   getStreet1()  { return new String(_street1); }
	public String   getStreet2()  { return new String(_street2); }
	public String   getAddress()  { return new String (getStreet1() + 
											" & " + getStreet2()); }
	public Location getLocation() { return new Location(_location); }
	public double   getDistance() { return _distance; }
	
	// constructor sets it all
    public Stop (String name, String street1, String street2, Location location)
    {
        _name = new String (name);
        _street1 = new String (street1);
        _street2 = new String (street2);
        _location = new Location (location);
        _distance = 0.0;
    }
    
	public Stop (Stop stop) 
	{
        _name = stop.getName();
        _street1 = stop.getStreet1();
        _street2 = stop.getStreet2();
        _location = stop.getLocation();
        _distance = stop.getDistance(); 
	}
	
	public void setDistance(double distance) { 
		_distance = distance; 
	}
	
	
	// utility methods to extract and convert stop distance
	// returns distance in miles
	public String getDistanceString() { 
		return String.format(Locale.US, StringFormat, _distance*MetersToMiles);
	}
		
	public String getWalkingTimeString() { 
		return String.format (Locale.US, StringFormat, _distance*MetersToMiles / AvgSpeedMilesPerMin);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("StopParcel", "writeToParcel");
	    dest.writeString(_name);
	    dest.writeString(_street1);
	    dest.writeString(_street2);
	    dest.writeParcelable(_location, flags); 
	    dest.writeDouble(_distance);
		
	}
	
	public static final Parcelable.Creator<Stop> CREATOR
    		= new Parcelable.Creator<Stop>() {
		public Stop createFromParcel(Parcel in) {
			return new Stop(in);
		}

		public Stop[] newArray(int size) {
		    return new Stop[size];
		}
	};
	
	public Stop(Parcel source) {
		Log.i("StopParcel", "Assemble stop parcel data");
        _name = source.readString(); 
        _street1 = source.readString(); 
        _street2 = source.readString();
        _location = source.readParcelable(Location.class.getClassLoader()); 
        _distance = source.readDouble(); 
    }
	
}
