package bustracker.entities;

import android.location.Location;

public class Stop {

	private String     _name;
	
	// where
	private String     _street1;
	private String     _street2;
	private Location   _location;
	
	// getters
	protected String   getName()     { return new String(_name); }
	protected String   getStreet1()  { return new String(_street1); }
	protected String   getStreet2()  { return new String(_street2); }
	protected Location getLocation() { return new Location(_location); }
	
	// constructor sets it all
    public Stop (String name, String street1, String street2, Location location)
    {
        _name = new String (name);
        _street1 = new String (street1);
        _street2 = new String (street2);
        _location = new Location (location);
    }
	public Stop (Stop stop) 
	{
        _name = stop.getName();
        _street1 = stop.getStreet1();
        _street2 = stop.getStreet2();
        _location = stop.getLocation();
	}
	
}
