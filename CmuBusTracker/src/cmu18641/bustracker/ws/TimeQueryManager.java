package cmu18641.bustracker.ws;

import java.util.ArrayList;
import java.util.Random;

import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.entities.TimeQueryInterface;
import cmu18641.bustracker.exceptions.TrackerException;
import android.text.format.Time;

public class TimeQueryManager implements TimeQueryInterface {

	@Override
	public Schedule getSchedule(Stop stop, ArrayList<Bus> buses)
			throws TrackerException {
		// TODO Auto-generated method stub
		
		// array of buses
		String names[] = {"A Route Shuttle", "B Route Shuttle", "AB Route Shuttle", 
				"Bakery Square Shuttle", "PTC Shuttle", "61A", "61B", "61C", "61D", "67", "83" };

		ArrayList<Bus> busList = new ArrayList<Bus>(); 
		
		Random generator = new Random(); 
		String direction; 
		Bus temp; 
		
		for(int i = 0; i < names.length; i++){
			 direction =  (generator.nextInt(2) != 0) ? "west": "east";
			 temp = new Bus(names[i], direction);		 
			 busList.add(temp);       
		}
		
		
		// array of times 
		Time timelist[] = {new Time()};
		
		
		// package
		Schedule schedule = new Schedule(); 
		ArrayList<ScheduleItem> scheduleItemList = new ArrayList<ScheduleItem>(); 
		ScheduleItem scheduleItem = new ScheduleItem(); 
		schedule.setStop(stop); 
		Time time = new Time();
		
		for(int i = 0; i < names.length; i++) {
			time.set(generator.nextInt(60), generator.nextInt(60), generator.nextInt(60),
					generator.nextInt(30), generator.nextInt(12), 2013);
			
			scheduleItem.setBus(busList.get(i));
			scheduleItem.setTime(time);
			
			scheduleItemList.add(scheduleItem); 
		}
		
		schedule.setScheduleItem(scheduleItemList); 
		
		return schedule;
	}

}
