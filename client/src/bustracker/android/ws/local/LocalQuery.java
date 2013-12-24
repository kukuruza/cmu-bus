package bustracker.android.ws.local;

import java.util.ArrayList;

import bustracker.android.dblayout.LocalDatabaseConnector;
import bustracker.android.entities.*;
import bustracker.android.exceptions.TrackerException;
import bustracker.android.ws.TimeQueryInterface;
import bustracker.common.dblayout.DbConnectorInterface;
import bustracker.common.entities.BaseSchedule;

import android.content.Context;



public class LocalQuery implements TimeQueryInterface {
	private final String TAG = "LocalQuery"; 

	@Override
	public Schedule getSchedule(Context context, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException {
		
		// to arrays
		ArrayList<String> busNames = new ArrayList<String>();
		ArrayList<String> busDirs  = new ArrayList<String>();
		for (Bus bus : buses) 
		{
			busNames.add( bus.getName() );
			busDirs.add( bus.getDirection() );
		}
		
		// request
		DbConnectorInterface db = new LocalDatabaseConnector(context);
		BaseSchedule baseSchedule = db.getSchedule( stop.getName(), busNames, busDirs );
		
        // from BaseSchedule to Schedule
		Schedule schedule = new Schedule (baseSchedule);
		schedule.setInfoSrc("local schedule");
		
		// add missing info about bus stop (for completeness) to schedule
		if ( !schedule.getStop().getName().equals( stop.getName() ))
			throw new TrackerException(TrackerException.BAD_LOCAL_RESULT, TAG, 
					                   "sent and received stop names differ"); 
		schedule.setStop(stop);
		
		return schedule;
	}

}
