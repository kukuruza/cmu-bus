package cmu18641.bustracker.common;


public class BaseBus {

	private String _name;
	private String _direction; 
	
	public BaseBus (String name, String direction)
	{
		_name = name;
		_direction = direction; 
	}
	public BaseBus (BaseBus another)
	{
		_name = another.getName(); 
		_direction = another.getDirection(); 
	}
	
	public String getName()
	{
		return new String (_name);
	}
	
	public String getDirection() 
	{
		return new String (_direction); 
	}
}
