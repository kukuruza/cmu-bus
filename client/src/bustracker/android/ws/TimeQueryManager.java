package bustracker.android.ws;

import java.util.ArrayList;

import bustracker.android.activities.SettingsActivity;
import bustracker.android.entities.Bus;
import bustracker.android.entities.Schedule;
import bustracker.android.entities.ScheduleItem;
import bustracker.android.entities.Stop;
import bustracker.android.exceptions.TrackerException;
import bustracker.android.ws.local.LocalQuery;
import bustracker.android.ws.remote.Networking;
import bustracker.android.ws.remote.RemoteQuery;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


/*
 *  this class checks for network and redirects the query to remote or local
 *  
 *  returns schedule or null if fails
 */

public class TimeQueryManager {
	private final String TAG = "TimeQueryManager"; 

	// number of schedule results to display
	public final int numOut = 15;
	
	
	// logic on filtering buses based on current time
	private void keepSomeItems (Schedule schedule)
	{
		ArrayList<ScheduleItem> scheduleItems = schedule.getScheduleItemList();
		
		ArrayList<ScheduleItem> subScheduleItem; 
		if(scheduleItems.size() > numOut)
			subScheduleItem = new ArrayList<ScheduleItem>(scheduleItems.subList(0, numOut)); 
		else
			subScheduleItem = scheduleItems;
		
		schedule.setScheduleItemList(subScheduleItem);
	}
	
	
	// the main function - tries accessing remote and local data
	public Schedule getSchedule (Context context, Stop stop, ArrayList<Bus> buses) 
	{	
		// get the raw schedule
		Schedule schedule = null;
		
		// check preferences
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean prefRemote = sharedPref.getBoolean(SettingsActivity.KEY_REMOTE, true);
		
		// check network
		boolean availableNetwork = Networking.isNetworkAvailable(context);
		if (!availableNetwork)
			Log.i(TAG, "network is NOT availble");
		else
			Log.i(TAG, "network is availble");
			
		
		// remote query
		if (prefRemote && availableNetwork)
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

		// keep only several relevant buses
		keepSomeItems (schedule);
		
		return schedule;
	}

}
