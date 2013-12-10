package bustracker.app.ws.local;

import java.util.ArrayList;

import bustracker.app.dblayout.LocalDatabaseConnector;
import bustracker.app.entities.*;
import bustracker.app.exceptions.TrackerException;
import bustracker.app.ws.TimeQueryInterface;

import android.content.Context;
import android.text.format.Time;



public class LocalQuery implements TimeQueryInterface {

	@Override
	public Schedule getSchedule(Context context, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException {
		
		LocalDatabaseConnector db = new LocalDatabaseConnector(context);
		

		// build a scheduleitem array
		ArrayList<ScheduleItem> scheduleItems = new ArrayList<ScheduleItem>();
		
		for(int i = 0; i < buses.size(); i++) {
			//build a list of times for each bus given associated with the stop 
			ArrayList<Time> times = db.selectScheduleTimes(stop, buses.get(i));
			
			for(int j = 0; j < times.size(); j++) {
				//build a list of ScheduleItems for all buses and times associated with the stop
				ScheduleItem item = new ScheduleItem(); 
				item.setBus(buses.get(i));
				item.setTime(times.get(j)); 
				scheduleItems.add(item);
			}
		}
		
		
		return new Schedule (stop, scheduleItems, "local schedule");
		

	}

}
