package cmu18641.bustracker.entities;

import java.util.ArrayList;

public class UserChoice {
	
	public String         stopName;
	public ArrayList<Bus> buses;
	public UserChoice (String stopName, ArrayList<Bus> buses)
	{
		assert (stopName != null);
		assert (buses != null);
		this.stopName = stopName;
		this.buses = buses;
	}
	
	// for simplicity, if stops are the same, objects are equal
	public boolean equals (UserChoice another)
	{
		assert (this.stopName    != null);
		assert (another.stopName != null);
		if (! this.stopName.equals(another.stopName) ) return false;
		return true;
	}
	
}
