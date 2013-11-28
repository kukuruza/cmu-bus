package cmu18641.bustracker.ws.remote;

import java.util.ArrayList;

import android.location.Location;
import android.text.format.Time;
import android.util.Log;
import cmu18641.bustracker.common.entities.BaseSchedule;
import cmu18641.bustracker.common.entities.BaseScheduleItem;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;

public class FromBaseHelper {
	private final static String TAG = "FromBaseHelper";

	static Schedule fromBase (BaseSchedule baseSchedule)
	{
		Schedule schedule = new Schedule();

		assert (baseSchedule != null);
		
		// set stop
		Location loc = new Location ((String)null);
		loc.setLatitude(0);
		loc.setLongitude(0);
		String stopName = baseSchedule.getStop();
		assert (stopName != null);
		Stop stop = new Stop (stopName, "", "", loc );
		schedule.setStop(stop);
		
        // set ScheduleItem-s
		ArrayList<ScheduleItem> itemList = new ArrayList<ScheduleItem>();
		assert (baseSchedule.getScheduleItemList() != null);
		Log.i (TAG, Integer.toString(baseSchedule.getScheduleItemList().size()));
		for (BaseScheduleItem baseItem : baseSchedule.getScheduleItemList())
		{
		    ScheduleItem item = new ScheduleItem ();
		    // set bus
		    assert (baseItem.getBus() != null);
		    Bus bus = new Bus (baseItem.getBus().getName(), baseItem.getBus().getDirection());
		    item.setBus(bus);
		    // set time
		    int minutes = (int) baseItem.getTime();
		    Time time = new Time();
		    time.setToNow();
		    time.second = 0; 
		    time.monthDay = 1; 
		    time.month = 1;
		    time.minute = minutes % 60;
		    time.hour = minutes / 60;
		    item.setTime(time);
		    // insert item
		    itemList.add(item);
		}
		schedule.setScheduleItemList(itemList);
		
	    return schedule;
	}
}
