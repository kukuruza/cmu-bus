package cmu18641.bustracker.entities;

import java.io.Serializable;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Stop implements Parcelable {

	private String     _name;
	
	// where
	private String     _street1;
	private String     _street2;
	private Location   _location;
	private float 	   _distance; 
	
	// getters
	public String   getName()     { return new String(_name); }
	public String   getStreet1()  { return new String(_street1); }
	public String   getStreet2()  { return new String(_street2); }
	public String   getAddress()  { return new String (getStreet1() + 
											" & " + getStreet2()); }
	public Location getLocation() { return new Location(_location); }
	public float    getDistance() { return _distance; }
	
	// constructor sets it all
    public Stop (String name, String street1, String street2, Location location, float distance)
    {
        _name = new String (name);
        _street1 = new String (street1);
        _street2 = new String (street2);
        _location = new Location (location);
        _distance = distance; 
    }
	public Stop (Stop stop) 
	{
        _name = stop.getName();
        _street1 = stop.getStreet1();
        _street2 = stop.getStreet2();
        _location = stop.getLocation();
        _distance = stop.getDistance(); 
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.v("StopParcel", "writeToParcel");
	    dest.writeString(_name);
	    dest.writeString(_street1);
	    dest.writeString(_street2);
	    dest.writeParcelable(_location,1); 
	    dest.writeFloat(_distance);
		
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
	
	  public Stop(Parcel source){
          /*
           * Reconstruct from the Parcel
           */
          Log.v("StopParcel", "ParcelData(Parcel source): time to put back parcel data");
          _name = source.readString(); 
          _street1 = source.readString(); 
          _street2 = source.readString(); 
          _distance = source.readFloat(); 
          
    }
	
}
