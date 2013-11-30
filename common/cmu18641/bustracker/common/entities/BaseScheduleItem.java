package cmu18641.bustracker.common.entities;


public class BaseScheduleItem { 
	
	private BaseBus _bus = null; 
	private int _time = 0; 
	
	public BaseScheduleItem() { 
    }
	
	public BaseScheduleItem(BaseBus bus, int time) { 
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