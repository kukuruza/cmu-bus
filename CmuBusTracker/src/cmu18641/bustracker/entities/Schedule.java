package cmu18641.bustracker.entities;

import java.util.ArrayList;

import android.util.Log;

public class Schedule {

	private Stop _stop; 
	private ArrayList<ScheduleItem> _scheduleItemList; 
	
	public Schedule() { 
		_stop = null; 
		_scheduleItemList = new ArrayList<ScheduleItem>(); 
	}
	
	public Schedule (Stop stop)
	{
		this._stop = stop; 
		this._scheduleItemList = new ArrayList<ScheduleItem>();
	}
	
	public Schedule(Stop stop, ArrayList<ScheduleItem> scheduleItemList) { 
		this._stop = stop; 
		this._scheduleItemList = scheduleItemList; 
	}
	
	public void setStop(Stop stop) { 
		this._stop = stop; 
	}
	
	public void setScheduleItemList(ArrayList<ScheduleItem> scheduleItemList) { 
		this._scheduleItemList = scheduleItemList;
	}
	
	public Stop getStop() { 
		return _stop; 
	}
	
	public ArrayList<ScheduleItem> getScheduleItemList() { 
		return _scheduleItemList;
	}
	
	public void log (String TAG)
	{
		Log.i (TAG, "Schedule: \n" + _stop.getName() + "\n");
		for (ScheduleItem item : _scheduleItemList)
			Log.i (TAG, "bus " + item.getBus().getName() +
					    ", minutes " + item.getTime() + "\n");
	}
}
