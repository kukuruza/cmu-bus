package cmu18641.bustracker;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.Time;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/*
 * ViewSchedule.java
 * 
 * Activity to view list of buses and arrival times for a station
 */

public class ViewSchedule extends Activity {

	private long stopID; 
	private long busSelections[];
	
	private TextView stopNameTextView; 
	private TextView stopDistanceTextView;
	private TextView stopWalkingDistanceTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_schedule);
		
		stopNameTextView = (TextView) findViewById(R.id.stopNameTextView);
		stopDistanceTextView = (TextView) findViewById(R.id.distanceTextView);
		stopWalkingDistanceTextView = (TextView) findViewById(R.id.walkingDistanceTextView);
		
		Bundle extras = getIntent().getExtras(); 
		
		// grab station and selected buses from selectStationAndBus
		if(extras != null) { 
			stopID = extras.getLong(SelectStationAndBus.STOP_ID); 
			busSelections = extras.getLongArray(SelectStationAndBus.BUS_ARRAY_ID);
		}
		else { 
			// if bundle is null, throw an error
		}
		
		// call query to return station info for stopID
		
		// set header textViews
		stopNameTextView.setText(""); 
		stopDistanceTextView.setText(""); 
		stopWalkingDistanceTextView.setText(""); 
		
		// call query to return a list of schedules 
		ArrayList<Time> timeList = new ArrayList<Time>(); 
				
		// schedule adapter is used to map the schedules to the listview
		ArrayAdapter<Time> scheduleAdapter = new ArrayAdapter<Time>(this, 
				     R.layout.activity_select_station_and_bus, timeList);
				
		// bind adapter and listener
		ListView timeListView = (ListView) findViewById(R.id.scheduleListView);
		timeListView.setAdapter(scheduleAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_schedule, menu);
		return true;
	}

}
