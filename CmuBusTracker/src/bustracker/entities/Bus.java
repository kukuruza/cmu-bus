package bustracker.entities;

public class Bus {

	private String _name;
	
	public Bus (String name)
	{
		_name = name;
	}
	public Bus (Bus another)
	{
		_name = another._name;
	}
	
	public String getName()
	{
		return new String (_name);
	}
}
