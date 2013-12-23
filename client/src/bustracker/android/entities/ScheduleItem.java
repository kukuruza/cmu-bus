package bustracker.android.entities;

import bustracker.common.entities.BaseScheduleItem;
import android.text.format.Time;

public class ScheduleItem { 
	private Bus   _bus; 
	private Time  _time; 
	
	public ScheduleItem() 
	{ 
		_bus = new Bus(); 
		_time = new Time(); 
	}
	
	public ScheduleItem( Bus bus, Time time )
	{
		_bus = bus;
		_time = time;
	}

	public ScheduleItem( BaseScheduleItem another )
	{
		_bus = new Bus (another.getBus());
	    int minutes = another.getTime();
		_time = new Time();
	    _time.setToNow();
	    _time.second = 0; 
	    _time.monthDay = 1; 
	    _time.month = 1;
	    _time.minute = minutes % 60;
	    _time.hour = minutes / 60;
	}

	
	//public void   setBus( Bus bus )     { _bus = bus;  }
	public Bus    getBus()              { return _bus; }

	//public void   setTime( Time time )  {  _time = time; }
	public Time   getTime()             { return _time; }
}