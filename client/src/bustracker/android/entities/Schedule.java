package bustracker.android.entities;

import java.util.ArrayList;

import android.util.Log;

public class Schedule {

	private Stop _stop; 
	private ArrayList<ScheduleItem> _scheduleItemList;
	
	// contains "server real-time", "server schedule", "local schedule" 
	private String _infoSrc = null;
	
	public Schedule() { 
		_stop = null; 
		_scheduleItemList = new ArrayList<ScheduleItem>(); 
	}
	
	public Schedule (Stop stop)
	{
		this._stop = stop; 
		this._scheduleItemList = new ArrayList<ScheduleItem>();
	}
	
	public Schedule(Stop stop, ArrayList<ScheduleItem> scheduleItemList, String infoSrc) { 
		this._stop = stop; 
		this._scheduleItemList = scheduleItemList; 
		this._infoSrc = infoSrc;
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
	
	public void setInfoSrc (String infoSrc)
	{
		_infoSrc = infoSrc;
	}
	
	public String getInfoSrc ()
	{
		return _infoSrc;
	}
	
	public void log (String TAG)
	{
		Log.i (TAG, "Schedule: \n" + _stop.getName() + "\n");
		for (ScheduleItem item : _scheduleItemList)
			Log.i (TAG, "bus " + item.getBus().getName() +
					    ", minutes " + item.getTime() + "\n");
	}
}
