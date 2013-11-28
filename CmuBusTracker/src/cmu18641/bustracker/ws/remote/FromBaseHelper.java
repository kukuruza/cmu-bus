package cmu18641.bustracker.ws.remote;

import java.util.ArrayList;

import android.location.Location;
import android.text.format.Time;
import android.util.Log;
import cmu18641.bustracker.common.*;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;

public class FromBaseHelper {
	private final static String TAG = "FromBaseHelper";

	static Schedule fromBase (BaseSchedule baseSchedule)
	{
		Schedule schedule = new Schedule();

		if (baseSchedule == null)
		{
			Log.e (TAG, "baseSchedule on input was null");
			return null;
		}
		
		// set stop
		Location loc = new Location ((String)null);
		loc.setLatitude(baseSchedule.getStop().latitude);
		loc.setLongitude(baseSchedule.getStop().longitude);
		BaseStop baseStop = baseSchedule.getStop();
		Stop stop = new Stop (baseStop.getName(), baseStop.street1, 
				              baseStop.street2, loc );
		schedule.setStop(stop);
		
        // set ScheduleItem-s
		ArrayList<ScheduleItem> itemList = new ArrayList<ScheduleItem>();
		for (BaseScheduleItem baseItem : baseSchedule.getScheduleItemList())
		{
		    ScheduleItem item = new ScheduleItem ();
		    // set bus
		    Bus bus = new Bus (item.getBus().getName(), item.getBus().getDirection());
		    item.setBus(bus);
		    // set time
		    Time time = new Time();
		    time.set(baseItem.getTime());
		    item.setTime(time);
		    // insert item
		    itemList.add(item);
		}
		schedule.setScheduleItemList(itemList);
		
	    return schedule;
	}

}
