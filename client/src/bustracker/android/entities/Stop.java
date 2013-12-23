package bustracker.android.entities;

import java.util.Locale;

import bustracker.common.entities.BaseStop;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Stop extends BaseStop implements Parcelable {

	final static private double MetersToMiles = 0.0006213727;  // 1609.34 meters per mile
	final static private double AvgSpeedMilesPerMin = 0.05;    // 3 miles per hour
	final static private String StringFormat = "%.1f";
	
	private double	            _distance; 
	
	public String   getAddress()  { return new String (getStreet1() + " & " + getStreet2()); }
	public double   getDistance() { return _distance; }
	
    public Stop (String name, String street1, String street2, double latitude, double longitude)
    {
        super( name, street1, street2, latitude, longitude );
        _distance = 0.0;
    }
    
	public Stop( Stop another ) 
	{
		super(another);
        _distance = another.getDistance();
	}
	
	public Stop( BaseStop baseStop )
	{
		super(baseStop);
		_distance = 0.0;
	}
	
	public void setDistance( double distance ) { 
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
        _distance = source.readDouble(); 
    }
	
}
