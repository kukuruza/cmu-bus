package cmu18641.bustracker.entities;

import java.io.Serializable;

import android.location.Location;

public class Stop implements Serializable {

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
	
}
