package cmu18641.bustracker.ws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import cmu18641.bustracker.dblayout.LocalDatabaseConnector;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;


/*
 *  this class checks for network and redirects the query to remote or local
 */

public class TimeQueryManager {
	private final String TAG = "TimeQueryManager"; 

	public final int numOut = 15; // number of schedule results to display 
	
	public Schedule getSchedule (Context context, Stop stop, ArrayList<Bus> buses)
			throws TrackerException {
		
		// determine day of week for query 
		int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		
		//Our database represents a weekday as 0 
		int currentDay = 0; 
		
		//Our database represents Sunday as 2
		if(currentDayOfWeek == 1)
			currentDay = 2;
		//Our database represents Saturday as 1
		else if(currentDayOfWeek == 7)
			currentDay = 1; 
		
		// decide whether to go local or remote
		// if local 
			LocalDatabaseConnector db = new LocalDatabaseConnector(context);
		// else
			// go remote
		

		// build a scheduleitem array
		ArrayList<ScheduleItem> scheduleItems = new ArrayList<ScheduleItem>();
		
		for(int i = 0; i < buses.size(); i++) {
			//build a list of times for each bus given associated with the stop 
			ArrayList<Time> times = db.selectScheduleTimes(stop, buses.get(i), 0); // changed
			
			for(int j = 0; j < times.size(); j++) {
				//build a list of ScheduleItems for all buses and times associated with the stop
				ScheduleItem item = new ScheduleItem(); 
				item.setBus(buses.get(i));
				item.setTime(times.get(j)); 
				scheduleItems.add(item);
			}
		}
		
		// get current time
		Time currentTime = new Time();
		currentTime.setToNow();
		
		currentTime.second = 0; 
		currentTime.monthDay = 1; 
		currentTime.month = 1;
		
		// remove buses that have already passed stop
		Iterator<ScheduleItem> it = scheduleItems.iterator();
		while (it.hasNext()) {
			ScheduleItem item = it.next(); 
			
			int diff = Time.compare(item.getTime(), currentTime); 
			Log.v(TAG, String.format("%d", item.getTime().hour ));
			
		    if(diff <= 0) {
		        it.remove();
		    }
		}
		
		// sort remaining buses by time
		Collections.sort(scheduleItems, new Comparator<ScheduleItem>() {
		       @Override public int compare(ScheduleItem s1, ScheduleItem s2) {
		           return Time.compare(s1.getTime(), s2.getTime());
		       }
		});
		

		// return only top numOut results
		ArrayList<ScheduleItem> subScheduleItem; 
		if(scheduleItems.size() > numOut) { 
			subScheduleItem = new ArrayList<ScheduleItem>(scheduleItems.subList(0, numOut)); 
		}
		else { 
			subScheduleItem = scheduleItems; 
		}
		
		// build a schedule
		Schedule schedule = new Schedule();
		schedule.setScheduleItemList(subScheduleItem);
		schedule.setStop(stop);
		
		// return schedule
		return schedule; 
		
		
		
		
		/*
		//////////////test data
		ArrayList<ScheduleItem> scheduleItemList = new ArrayList<ScheduleItem>(11); 

		// array of buses

		String names[] = {"A Route Shuttle", "B Route Shuttle", "AB Route Shuttle", 
				"Bakery Square Shuttle", "PTC Shuttle", "61A", "61B", "61C", "61D", "67", "83" };

		ArrayList<Bus> busList = new ArrayList<Bus>(); 
		Random generator = new Random(); 
		String direction; 
		Bus temp; 
		Time timeTemp = new Time(); 
		
		for(int i = 0; i < names.length; i++){
			 direction =  (generator.nextInt(2) != 0) ? "west": "east";
			 timeTemp.set(generator.nextInt(60), generator.nextInt(60), generator.nextInt(60),
						generator.nextInt(30), generator.nextInt(12), 2013);
			 
			 temp = new Bus(names[i], direction);		 
			 
			 ScheduleItem scheduleItem = new ScheduleItem(); 
			 
			 scheduleItem.setBus(temp); 
			 scheduleItem.setTime(timeTemp); 
			 
			 scheduleItemList.add(scheduleItem);      
		}
		
		Schedule schedule = new Schedule(); 
		schedule.setScheduleItemList(scheduleItemList); 
	
		
		return schedule; 
		///////////////end test data
		*/
		/*
		Schedule schedule = null;
		if (Networking.isNetworkAvailable(context))
		{
			RemoteQuery remoteQuery = new RemoteQuery();
			schedule = remoteQuery.getSchedule (stop, buses);
		}
		else
		{
			Log.i(TAG, "network is unavailble");
		
			// put call to localQuery here
		}
		
		return schedule;
		*/
	}

}
