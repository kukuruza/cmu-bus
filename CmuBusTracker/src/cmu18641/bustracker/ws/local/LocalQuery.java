package cmu18641.bustracker.ws.local;

import java.util.ArrayList;

import android.content.Context;
import android.text.format.Time;
import cmu18641.bustracker.dblayout.LocalDatabaseConnector;
import cmu18641.bustracker.entities.*;
import cmu18641.bustracker.exceptions.TrackerException;
import cmu18641.bustracker.ws.TimeQueryInterface;



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
