package cmu18641.bustracker.ws;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import cmu18641.bustracker.ws.remote.Networking;
import cmu18641.bustracker.ws.remote.RemoteQuery;


/*
 *  this class checks for network and redirects the query to remote or local
 */

public class TimeQueryManager {
	private final String TAG = "TimeQueryManager"; 

	public Schedule getSchedule (Context context, Stop stop, ArrayList<Bus> buses)
			throws TrackerException {
		
		Schedule schedule = null;
		if (Networking.isNetworkAvailable(context))
		{
			RemoteQuery remoteQuery = new RemoteQuery();
			schedule = remoteQuery.getSchedule (stop, buses);
		}
		else
			Log.i(TAG, "network is unavailble");
		
		return schedule;
	}

}
