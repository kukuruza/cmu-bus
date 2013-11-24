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
import cmu18641.bustracker.ws.local.LocalQuery;


/*
 *  this class checks for network and redirects the query to remote or local
 */

public class TimeQueryManager {
	private final String TAG = "TimeQueryManager"; 

	public final int numOut = 15; // number of schedule results to display 
	
	public Schedule getSchedule (Context context, Stop stop, ArrayList<Bus> buses)
			throws TrackerException {
		
		// get the raw schedule
		Schedule schedule = null;
		//if (Networking.isNetworkAvailable(context))
		//{
		//	RemoteQuery remoteQuery = new RemoteQuery();
		//	schedule = remoteQuery.getSchedule (stop, buses);
		//}
		//else
		//{
		//	Log.i(TAG, "network is unavailble");
		
		    LocalQuery localQuery = new LocalQuery();
		    schedule = localQuery.getSchedule (context, stop, buses);
		//}
		    
		ArrayList<ScheduleItem> scheduleItems = schedule.getScheduleItemList();
		    
		   
		
		
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
		schedule.setScheduleItemList(subScheduleItem);
		//schedule.setStop(stop);
		
		// return schedule
		return schedule;
		
	}

}
