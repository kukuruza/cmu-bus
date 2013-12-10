package bustracker.app.ws.local;

import java.util.ArrayList;
import java.util.Calendar;

import bustracker.app.dblayout.LocalDatabaseConnector;
import bustracker.app.entities.*;
import bustracker.app.exceptions.TrackerException;
import bustracker.app.ws.TimeQueryInterface;
import bustracker.common.dblayout.DbTime;
import bustracker.common.entities.BaseSchedule;
import bustracker.common.entities.BaseScheduleItem;

import android.content.Context;
import android.text.format.Time;



public class LocalQuery implements TimeQueryInterface {

	@Override
	public Schedule getSchedule(Context context, Stop stop, ArrayList<Bus> buses) 
			throws TrackerException {
		
		// to arrays
		ArrayList<String> busNames = new ArrayList<String>();
		ArrayList<String> busDirs  = new ArrayList<String>();
		for(int i = 0; i < buses.size(); i++) 
		{
			busNames.add(buses.get(i).getName());
			busDirs.add(buses.get(i).getDirection());
		}
		
		// request
		LocalDatabaseConnector db = new LocalDatabaseConnector(context);
		BaseSchedule baseSchedule = db.getSchedule (stop.getName(), busNames, busDirs);
		
        // from BaseSchedule to Schedule
		Schedule schedule = new Schedule (stop);
		ArrayList<ScheduleItem> scheduleItems = schedule.getScheduleItemList();
		ArrayList<BaseScheduleItem> baseScheduleItems = baseSchedule.getScheduleItemList();
		for (BaseScheduleItem baseItem : baseScheduleItems) 
		{
			// make Bus
			Bus bus = new Bus (baseItem.getBus().getName(), baseItem.getBus().getDirection());
			
			// make Time
			DbTime dbTime = new DbTime ();
			dbTime.setTime((int)baseItem.getTime());
			Time time = new Time();
			time.set(0, dbTime.getMinutes(), dbTime.getHours(), 1, 1, Calendar.getInstance().get(Calendar.YEAR));

			// make ScheduleItem
			ScheduleItem item = new ScheduleItem(); 
			item.setBus (bus);
			item.setTime (time); 
			scheduleItems.add(item);
		}
		
		return new Schedule (stop, scheduleItems, "local schedule");
	}

}
