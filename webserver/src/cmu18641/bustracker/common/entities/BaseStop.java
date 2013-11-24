package cmu18641.bustracker.common.entities;


public class BaseStop {

	private String     _name;
	
	// where
	public String     street1, street2;
	public double     latitude, longitude;
	
	// getters
	public String   getName()     { return new String(_name); }
	
	// constructor sets it all
    public BaseStop (String name, String street1, String street2, 
    		         double latitude, double longitude)
    {
        _name = new String (name);
        this.street1 = new String (street1);
        this.street2 = new String (street2);
        this.latitude = latitude;
        this.longitude = longitude; 
    }
    
	public BaseStop (BaseStop stop) 
	{
        _name = stop.getName();
        this.street1 = stop.street1;
        this.street2 = stop.street1;
        this.latitude = stop.latitude;
        this.longitude = stop.longitude; 
	}
	
}
