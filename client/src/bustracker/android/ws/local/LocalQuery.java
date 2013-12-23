package bustracker.android.ws.local;

import java.util.ArrayList;

import bustracker.android.dblayout.LocalDatabaseConnector;
import bustracker.android.entities.*;
import bustracker.android.exceptions.TrackerException;
import bustracker.android.ws.TimeQueryInterface;
import bustracker.common.dblayout.DbConnectorInterface;
import bustracker.common.entities.BaseSchedule;

import android.content.Context;
//import android.util.Log;



public class LocalQuery implements TimeQueryInterface {
	//private final String TAG = "LocalQuery"; 

	@Override
	public Schedule getSchedule(Context context, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException {
		
		// to arrays
		ArrayList<String> busNames = new ArrayList<String>();
		ArrayList<String> busDirs  = new ArrayList<String>();
		for (int i = 0; i < buses.size(); i++) 
		{
			busNames.add(buses.get(i).getName());
			busDirs.add(buses.get(i).getDirection());
		}
		
		// request
		DbConnectorInterface db = new LocalDatabaseConnector(context);
		BaseSchedule baseSchedule = db.getSchedule (stop.getName(), busNames, busDirs);
		
        // from BaseSchedule to Schedule
		Schedule schedule = new Schedule (baseSchedule);
		schedule.setInfoSrc("local schedule");
		
		return schedule;
	}

}
