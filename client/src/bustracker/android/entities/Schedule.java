package bustracker.android.entities;

import java.util.ArrayList;

import bustracker.common.entities.BaseSchedule;
import bustracker.common.entities.BaseScheduleItem;

import android.util.Log;

public class Schedule {

	private Stop                     _stop; 
	private ArrayList<ScheduleItem>  _scheduleItemList;
	
	// contains "server real-time", "server schedule", "local schedule" 
	private String                   _infoSrc = null;
	
	public Schedule() { 
		_stop = null; 
		_scheduleItemList = new ArrayList<ScheduleItem>(); 
	}
	
	public Schedule( Stop stop )
	{
		_stop = stop; 
		_scheduleItemList = new ArrayList<ScheduleItem>();
	}
	
	public Schedule( Stop stop, ArrayList<ScheduleItem> scheduleItemList, String infoSrc ) 
	{ 
		_stop = stop; 
		_scheduleItemList = scheduleItemList; 
		_infoSrc = infoSrc;
	}

	public Schedule( BaseSchedule baseSchedule )
	{
		_scheduleItemList = new ArrayList<ScheduleItem>();
		assert (baseSchedule.getScheduleItemList() != null);
		for (BaseScheduleItem baseItem : baseSchedule.getScheduleItemList())
			_scheduleItemList.add( new ScheduleItem (baseItem) );
		
		String stopName = baseSchedule.getStop();
		assert (stopName != null);
		_stop = new Stop (stopName, "", "", 0, 0);
	}

	public void     setStop( Stop stop )          { _stop = stop; }
	public Stop     getStop()                     { return _stop; }
	
	public void     setInfoSrc (String infoSrc)   { _infoSrc = infoSrc; }
	public String   getInfoSrc ()                 { return _infoSrc; }
	
	public void     setScheduleItemList( ArrayList<ScheduleItem> scheduleItemList ) 
	{ 
		_scheduleItemList = scheduleItemList;
	}
	public ArrayList<ScheduleItem> getScheduleItemList() 
	{ 
		return _scheduleItemList;
	}
	
	public void log (String TAG)
	{
		Log.i (TAG, "Schedule: \n" + _stop.getName() + "\n");
		for (ScheduleItem item : _scheduleItemList)
			Log.i (TAG, "bus " + item.getBus().getName() +
					    ", minutes " + item.getMinutes() + "\n");
	}
}
