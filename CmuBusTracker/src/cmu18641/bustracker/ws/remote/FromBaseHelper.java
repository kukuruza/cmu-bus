package cmu18641.bustracker.ws.remote;

import android.location.Location;
import android.text.format.Time;
import cmu18641.bustracker.common.*;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;

public class FromBaseHelper {

	static Schedule fromRemote (BaseSchedule baseSchedule)
	{
		Schedule schedule = new Schedule();

		// reset stop
		Location loc = new Location ((String)null);
		loc.setLatitude(baseSchedule.getStop().latitude);
		loc.setLongitude(baseSchedule.getStop().longitude);
		BaseStop baseStop = baseSchedule.getStop();
		Stop stop = new Stop (baseStop.getName(), baseStop.street1, 
				              baseStop.street2, loc, -1);
		schedule.setStop(stop);
		
        // reset ScheduleItem-s
		for (BaseScheduleItem baseItem : baseSchedule.getScheduleItemList())
		{
		    ScheduleItem item = new ScheduleItem ();
		    Bus bus = new Bus (item.getBus().getName(), item.getBus().getDirection());
		    item.setBus(bus);
		    item.setTime(new Time(baseItem.getTime()));
		}
		
	    return null;
	}
	
}
