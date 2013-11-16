package cmu18641.bustracker.entities;

import java.io.Serializable;

public class Bus implements Serializable {

	private String _name;
	private String _direction; 
	
	public Bus (String name, String direction)
	{
		_name = name;
		_direction = direction; 
	}
	public Bus (Bus another)
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
