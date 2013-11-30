package cmu18641.bustracker.ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import cmu18641.bustracker.ws.local.LocalQuery;
import cmu18641.bustracker.ws.remote.Networking;
import cmu18641.bustracker.ws.remote.RemoteQuery;


/*
 *  this class checks for network and redirects the query to remote or local
 *  
 *  returns schedule or null if fails
 */

public class TimeQueryManager {
	private final String TAG = "TimeQueryManager"; 

	public final int numOut = 15; // number of schedule results to display 
	
	
	// logic on filtering buses based on current time
	private void filterByTime (Schedule schedule)
	{
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
	}
	
	
	public Schedule getSchedule (Context context, Stop stop, ArrayList<Bus> buses) 
	{	
		// get the raw schedule
		Schedule schedule = null;
		
		// check network
		boolean availableNetwork = Networking.isNetworkAvailable(context);
		if (!availableNetwork)
			Log.i(TAG, "network is unavailble");
		
		// remote query
		if (availableNetwork)
		{
		    RemoteQuery remoteQuery = new RemoteQuery();
	    	try {
				schedule = remoteQuery.getSchedule (context, stop, buses);
			} catch (TrackerException e) {
		    	Log.e (TAG, "remote query failed: " + e.getMessage());
			}
		}
		
		// local query is executed when no network or if remote failed
		if (!availableNetwork || schedule == null)
		{
		    LocalQuery localQuery = new LocalQuery();
		    try {
				schedule = localQuery.getSchedule (context, stop, buses);
			} catch (TrackerException e) {
		    	Log.e (TAG, "local query failed");
		    	return null;
			}
		}

		// check consistency of returned object
		if ( !schedule.getStop().getName().equals(stop.getName()))
		{
			Log.wtf (TAG, "sent and received stop names differ");
			return null;
		}

		// keep only several relevant buses
		filterByTime (schedule);
		
		return schedule;
	}

}
