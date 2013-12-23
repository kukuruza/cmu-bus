package bustracker.common.entities;


public class BaseBus {

	private String  _name;
	private String  _direction; 
	
	public void     setName( String name )              { _name = name; }
	public void     setDirection( String direction )    { _direction = direction; }
	public String   getName()                           { return new String (_name); }
	public String   getDirection()                      { return new String (_direction); }

	public BaseBus()
	{
		_name = "not set";
		_direction = "not set";
	}
	
	public BaseBus( String name, String direction )
	{
		_name = name;
		_direction = direction; 
	}
	
	public BaseBus( BaseBus another )
	{
		_name = another.getName(); 
		_direction = another.getDirection(); 
	}
	
}
