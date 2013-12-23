package bustracker.android.entities;

import java.util.Locale;

import bustracker.common.entities.BaseStop;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Stop extends BaseStop implements Parcelable {

	final static private double MetersToMiles = 0.0006213727;  // 1609.34 meters per mile
	final static private double AvgSpeedMilesPerMin = 0.05; // 3 miles per hour
	final static private String StringFormat = "%.1f";
	
	private Location   _location;
	private double	   _distance; 
	
	// getters
	public String   getAddress()  { return new String (getStreet1() + " & " + getStreet2()); }
	public Location getLocation() { return new Location(_location); }
	public double   getDistance() { return _distance; }
	
	// constructor sets it all
    public Stop (String name, String street1, String street2, Location location)
    {
        super( name, street1, street2, location.getLatitude(), location.getLongitude() );
        _location = new Location (location);
        _distance = 0.0;
    }
    
	public Stop( Stop another ) 
	{
		super(another);
        _location = another.getLocation();
        _distance = another.getDistance();
        // location of baseStop
		setLatitude(_location.getLatitude());
		setLongitude(_location.getLongitude());
	}
	
	public Stop( BaseStop baseStop )
	{
		super(baseStop);
		_location = new Location( (String)null );
		_location.setLatitude( baseStop.getLatitude() );
		_location.setLongitude( baseStop.getLongitude() );
		_distance = 0;
	}
	
	public void setDistance(double distance) { 
		_distance = distance; 
	}
	
	
	// utility methods to extract and convert stop distance
	// returns distance in miles
	public String getDistanceString() 
	{ 
		return String.format(Locale.US, StringFormat, _distance*MetersToMiles);
	}
		
	public String getWalkingTimeString() 
	{ 
		return String.format (Locale.US, StringFormat, _distance*MetersToMiles / AvgSpeedMilesPerMin);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("StopParcel", "writeToParcel");
	    dest.writeString( getName() );
	    dest.writeString( getStreet1() );
	    dest.writeString( getStreet2() );
	    dest.writeParcelable( _location, flags ); 
	    dest.writeDouble( _distance );
		
	}
	
	public static final Parcelable.Creator<Stop> CREATOR = new Parcelable.Creator<Stop>() 
	{
		public Stop createFromParcel(Parcel in) {
			return new Stop(in);
		}

		public Stop[] newArray(int size) {
		    return new Stop[size];
		}
	};
	
	public Stop(Parcel source) {
		Log.i("StopParcel", "Assemble stop parcel data");
        setName( source.readString() ); 
        setStreet1( source.readString() ); 
        setStreet2( source.readString() );
        _location = source.readParcelable(Location.class.getClassLoader()); 
        _distance = source.readDouble(); 
    }
	
}
