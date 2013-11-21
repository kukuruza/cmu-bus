package cmu18641.bustracker.common;

import java.sql.Time;

public class BaseScheduleItem { 
	private BaseBus _bus = null; 
	private Time _time = null; 
	
	public BaseScheduleItem() { 
    }
	
	public BaseScheduleItem(BaseBus bus, Time time) { 
		_bus = new BaseBus(bus); 
		_time = time; 
	}

	public BaseBus getBus() { 
		return _bus; 
	}
	
	public Time getTime() { 
		return _time; 
	}
}