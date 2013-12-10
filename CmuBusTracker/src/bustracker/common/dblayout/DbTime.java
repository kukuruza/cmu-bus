package bustracker.common.dblayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;


// time in format of the DB - minutes since midnight
public class DbTime {
	
	public final static int DeepNight = 10*60;
	
	private int _hours = -1, _minutes = -1;
	private static final SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm a");
	Calendar _calendar = Calendar.getInstance();
	
	public DbTime () { }
	DbTime (int minutesTotal) { setTime(minutesTotal); }
	
	public int getMinutes() { return _minutes; }
	
	public int getHours() { return _hours; }
	
	public void setTime (int minutesTotal)
	{		
		assert (minutesTotal > 0);
		int minutesAfterThisMidnight = minutesTotal % (24 * 60);
	    _hours = minutesAfterThisMidnight / 60;
	    _minutes = minutesAfterThisMidnight % 60;
		_calendar.set (Calendar.HOUR_OF_DAY, _hours);
		_calendar.set (Calendar.MINUTE, _minutes);
	}
	
	public int getMinutesTotal ()
	{
		assert (_hours >= 0 && _minutes >= 0);
		return _hours * 60 + _minutes;
	}
	
	public String toString()
	{
		assert (_hours >= 0 && _minutes >= 0);
		return _sdf.format(_calendar.getTime());
	}
	
	public static int getCurrentDbTime ()
	{
		Calendar calendar = Calendar.getInstance();
		int min = calendar.get(Calendar.MINUTE);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		return hours * 60 + min;
	}

	
	
	// this function decides if it is weekday or saturday or sunday/holiday
	// TODO: implement holiday
	public static int getWeekDay()
	{
    	Calendar calendar = Calendar.getInstance();
	    int day = calendar.get(Calendar.DAY_OF_WEEK);
	    
	    if (day == Calendar.SUNDAY)
	    	return DbStructure.WEEK_SUNDAY_HOLIDAY;
	    else if (day == Calendar.SATURDAY)
	    	return DbStructure.WEEK_SATURDAY;
	    else
	    	return DbStructure.WEEK_WEEKDAY;
	}
	
}