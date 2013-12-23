package bustracker.common.entities;


public class BaseStop {

	private String     _name;
	
	// where
	private String     _street1, _street2;
	private double     _latitude, _longitude;
	
	public BaseStop()
	{
		_name = "not set";
		_street1 = "not set";
		_street2 = "not set";
	}
	
	// constructor sets it all
    public BaseStop( String name, String street1, String street2,  double latitude, double longitude)
    {
        _name = new String (name);
        _street1 = new String (street1);
        _street2 = new String (street2);
        setLatitude(latitude);
        setLongitude(longitude); 
    }
    
	public BaseStop( BaseStop another ) 
	{
        _name = another.getName();
        _street1 = another.getStreet1();
        _street2 = another.getStreet2();
        setLatitude(another.getLatitude());
        setLongitude(another.getLongitude()); 
	}

	public String  getName()                          { return new String(_name); }
	public void    setName( String name )             { _name = name; }
	
	public String  getStreet1()                       { return _street1; }
	public void    setStreet1( String street1 )       { _street1 = street1; }

	public String  getStreet2()                       { return _street2; }
	public void    setStreet2( String street2 )       { _street2 = street2; }

	public double  getLongitude()                     { return _longitude; }
	public void    setLongitude( double longitude )   { _longitude = longitude; }

	public double  getLatitude()                      { return _latitude; }
	public void    setLatitude(double latitude)       { _latitude = latitude; }
	
}
