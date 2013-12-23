package bustracker.android.entities;

import android.text.format.Time;

public class ScheduleItem { 
	private Bus bus; 
	private Time time; 
	
	public ScheduleItem() { 
		bus = new Bus(); 
		time = new Time(); 
	}
	
	public void setBus(Bus bus) { 
		this.bus = bus; 
	}
	
	public void setTime(Time time) { 
		this.time = time; 
	}
	
	public Bus getBus() { 
		return bus; 
	}
	
	public Time getTime() { 
		return time; 
	}
}