package cmu18641.bustracker.entities;

import java.util.ArrayList;

public class Schedule {

	private Stop stop; 
	private ArrayList<ScheduleItem> scheduleItem; 
	
	public Schedule() { 
		stop = null; 
		scheduleItem = new ArrayList<ScheduleItem>(); 
	}
	
	public Schedule(Stop stop, ArrayList<ScheduleItem> scheduleItem) { 
		this.stop = stop; 
		this.scheduleItem = scheduleItem; 
	}
	
	public void setStop(Stop stop) { 
		this.stop = stop; 
	}
	
	public void setScheduleItem(ArrayList<ScheduleItem> scheduleItem) { 
		this.scheduleItem = scheduleItem;
	}
	
	public Stop getStop() { 
		return stop; 
	}
	
	public ArrayList<ScheduleItem> getScheduleItemList() { 
		return scheduleItem;
	}
	
}
