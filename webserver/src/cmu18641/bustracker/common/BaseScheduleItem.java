package cmu18641.bustracker.common;

public class BaseScheduleItem { 
	
	private BaseBus _bus = null; 
	private long _time = 0; 
	
	public BaseScheduleItem() { 
    }
	
	public BaseScheduleItem(BaseBus bus, long time) { 
		_bus = new BaseBus(bus); 
		_time = time; 
	}

	public BaseBus getBus() { 
		return _bus; 
	}
	
	public long getTime() { 
		return _time; 
	}
}