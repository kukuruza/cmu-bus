package cmu18641.bustracker;

import java.util.ArrayList;
import cmu18641.bustracker.adapter.ScheduleAdapter;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Connector;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

/*
 * ViewSchedule.java
 * 
 * Activity to view list of buses and arrival times for a station
 */

public class ViewSchedule extends Activity {

	private Schedule schedule; 
	private Stop selectedStop; 
	private ArrayList<Bus> selectedBuses; 
	private ScheduleAdapter scheduleAdapter; 
	ArrayList<ScheduleItem> scheduleItemList;
	
	private TextView stopNameTextView; 
	private TextView stopDistanceTextView;
	private TextView stopWalkingDistanceTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_schedule);
		
		stopNameTextView = (TextView) findViewById(R.id.viewschedule_stopNameTextView);
		stopDistanceTextView = (TextView) findViewById(R.id.viewschedule_distanceTextView);
		stopWalkingDistanceTextView = (TextView) findViewById(R.id.viewschedule_walkingDistanceTextView);
		
		Intent intent = getIntent(); 
		Bundle data = getIntent().getExtras(); 
		if(data != null) { 
			// grab station and selected buses from selectStationAndBus
			selectedStop = data.getParcelable(LocateStation.STOP_SELECTED); 
			selectedBuses = intent.getParcelableArrayListExtra(SelectStationAndBus.BUSES_SELECTED);
		}
		else { 
			// if intent is null, throw an error
			// should never enter this activity with null intent
		}
		
		// call query to return a schedule
		try {
			schedule = Connector.globalManager.getSchedule(selectedStop, selectedBuses);
		} catch (TrackerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scheduleItemList = schedule.getScheduleItemList(); 
		
		// set header textViews
		stopNameTextView.setText(selectedStop.getName()); 
		stopDistanceTextView.setText("5 miles"); 
		stopWalkingDistanceTextView.setText("10 minutes"); 
				
		// schedule adapter is used to map the scheduleitems to the listview
		scheduleAdapter = new ScheduleAdapter(this, 
				     R.layout.activity_select_station_and_bus, scheduleItemList);
				
		// bind adapter and listener
		ListView timeListView = (ListView) findViewById(R.id.scheduleListView);
		timeListView.setAdapter(scheduleAdapter);
	}


}
