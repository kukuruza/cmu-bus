package cmu18641.bustracker.common;

import java.util.ArrayList;

public class BaseSchedule {

	private BaseStop _stop; 
	private ArrayList<BaseScheduleItem> _scheduleItem; 
	
	public BaseSchedule() { 
		_stop = null; 
		_scheduleItem = new ArrayList<BaseScheduleItem>(); 
	}
	
	public BaseSchedule(BaseStop stop, ArrayList<BaseScheduleItem> scheduleItem) { 
		_stop = stop; 
		_scheduleItem = scheduleItem; 
	}
	
	public void setStop(BaseStop stop) { 
		_stop = stop; 
	}
	
	public void setScheduleItem(ArrayList<BaseScheduleItem> scheduleItem) { 
		_scheduleItem = scheduleItem;
	}
	
	public BaseStop getStop() { 
		return _stop; 
	}
	
	public ArrayList<BaseScheduleItem> getScheduleItemList() { 
		return _scheduleItem;
	}
	
	public void addItem (BaseScheduleItem item)
	{
		_scheduleItem.add(item);
	}
	
}
