package bustracker.android.entities;

import bustracker.common.entities.BaseScheduleItem;

public class ScheduleItem { 
	private Bus   _bus; 
	private int   _minutes; 
	
	public ScheduleItem() 
	{ 
		_bus = new Bus(); 
		_minutes = 0; 
	}
	
	public ScheduleItem( Bus bus, int time )
	{
		_bus = bus;
		_minutes = time;
	}

	public ScheduleItem( BaseScheduleItem another )
	{
		_bus = new Bus (another.getBus());
		_minutes = another.getTime();
	}

	
	//public void   setBus( Bus bus )     { _bus = bus;  }
	public Bus    getBus()              { return _bus; }

	//public void   setTime( int minutes )  {  _time = time; }
	public int    getMinutes()             { return _minutes; }
}