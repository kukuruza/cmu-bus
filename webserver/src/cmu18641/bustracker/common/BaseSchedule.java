package cmu18641.bustracker.common;

import java.util.ArrayList;

public class BaseSchedule {

	private String _stopName; 
	private ArrayList<BaseScheduleItem> _scheduleItem; 
	
	public BaseSchedule() { 
		_stopName = null; 
		_scheduleItem = new ArrayList<BaseScheduleItem>(); 
	}
	
	public void setStop (String stopName) { 
		_stopName = stopName; 
	}
	
	public BaseSchedule(String stopName, ArrayList<BaseScheduleItem> scheduleItem) { 
		_stopName = stopName; 
		_scheduleItem = scheduleItem; 
	}
	
	public void setScheduleItem(ArrayList<BaseScheduleItem> scheduleItem) { 
		_scheduleItem = scheduleItem;
	}
	
	public String getStop() { 
		return _stopName; 
	}
	
	public ArrayList<BaseScheduleItem> getScheduleItemList() { 
		return _scheduleItem;
	}
	
	public void addItem (BaseScheduleItem item)
	{
		_scheduleItem.add(item);
	}
	
}
