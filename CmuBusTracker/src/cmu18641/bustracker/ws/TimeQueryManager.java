package cmu18641.bustracker.ws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import cmu18641.bustracker.dblayout.LocalDatabaseConnector;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import cmu18641.bustracker.ws.remote.Networking;
import cmu18641.bustracker.ws.remote.RemoteQuery;


/*
 *  this class checks for network and redirects the query to remote or local
 */

public class TimeQueryManager {
	private final String TAG = "TimeQueryManager"; 

	public static Schedule getScheduleSubset (Schedule inSchedule, int numOut)
	{
		// TODO: return only 5 closest times
	    return inSchedule;
	}

	public Schedule getSchedule (Context context, Stop stop, ArrayList<Bus> buses)
			throws TrackerException {
		
		LocalDatabaseConnector db = new LocalDatabaseConnector(context);
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
		
		// return a a list of times
		// get the current day, and cast into an integer
		
		
		// build array list of buses
		
		// build a scheduleitem array
		ArrayList<ScheduleItem> scheduleItems = new ArrayList<ScheduleItem>();
		
		for(int i = 0; i < buses.size(); i++) {
			//build a list of times for each bus given associated with the stop 
			ArrayList<Time> times = db.selectScheduleTimes(stop, buses.get(i), currentDay); 
			
			for(int j = 0; j < times.size(); j++) {
				//build a list of ScheduleItems for all buses and times associated with the stop
				ScheduleItem item = new ScheduleItem(); 
				item.setBus(buses.get(i));
				item.setTime(times.get(j)); 
				scheduleItems.add(item);
			}
		}
		
		// build a schedule
		Schedule schedule = new Schedule();
		schedule.setScheduleItemList(scheduleItems);
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
