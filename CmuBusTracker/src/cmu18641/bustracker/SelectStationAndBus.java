package cmu18641.bustracker;

import java.util.ArrayList;
import cmu18641.bustracker.entities.Bus;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * SelectStationAndBus.java
 * 
 * Primary activity to select a bus station and a bus. 
 */

public class SelectStationAndBus extends Activity {
	
	public static final String BUS_ARRAY_ID = "rowarray_id";
	public static final String STOP_ID = "row_id";
	private ArrayList<Long> busSelections;
	private long stopID;

	private Button findNextBusButton; 
	private Button findStationButton; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_station_and_bus);
		
		busSelections = new ArrayList<Long>(); 
		Bundle extras = getIntent().getExtras(); 
		
		// grab selected stop if entering from LocateStation/SearchStation activity
		if(extras != null) { 
			stopID = extras.getLong(LocateStation.STOP_ID); 
		}
		else { 
			// if bundle is null, query must be called 
			// to find default station (closest to user)
		}
		
		// set button text dynamically
		findStationButton.setText(""); 
		
		// query must be called to find buses that pass thru the selected station
		// returning an array list of buses 
		ArrayList<Bus> busList = new ArrayList<Bus>(); 
		
		// bus adapter is used to map those buses to the listview
		ArrayAdapter<Bus> busAdapter = new ArrayAdapter<Bus>(this, 
		        R.layout.activity_select_station_and_bus, busList);
		
		// Note: using this built in adapter will require Bus class
		// to implement toString()
		
		// bind adapter and listener
		ListView busListView = (ListView) findViewById(R.id.busListView);
		busListView.setAdapter(busAdapter);
		busListView.setOnItemClickListener(selectBusListener);
		
		// listen for button presses
		findNextBusButton = (Button) findViewById(R.id.findNextBusButton);
	    findNextBusButton.setOnClickListener(findNextBusButtonClicked); 
	    
	    findStationButton = (Button) findViewById(R.id.findStationButton);
        findStationButton.setOnClickListener(findStationButtonClicked); 
		
	}

	
	// user is taken to viewSchedule activity after selecting at least
	// one bus and pressing findNextBusButton
	OnClickListener findNextBusButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if(!busSelections.isEmpty()) { 
				Intent showSchedule = new Intent(SelectStationAndBus.this, ViewSchedule.class);
				showSchedule.putExtra(BUS_ARRAY_ID, busSelections.toArray()); 
				showSchedule.putExtra(STOP_ID, stopID);
				SelectStationAndBus.this.startActivity(showSchedule);
			}
			else { 
				// show dialog
			}
		}
				
	};
		

	// user is taken to locateStation activity after pressing findStationButton
	OnClickListener findStationButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent showLocateStation = new Intent(SelectStationAndBus.this, LocateStation.class);
			SelectStationAndBus.this.startActivity(showLocateStation);
		}
					
	};
			
	
	// bus is added to selection list when pressed in list view
	// bus is removed from selection list when pressed again in list view
	OnItemClickListener selectBusListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			
			// pressed again -> deselect
			if(busSelections.contains(arg3)) { 
				busSelections.remove(arg3); 
			}
			// first press -> select
			else { 
				busSelections.add(arg3); 
			}
			
		}
		   
	}; 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_station_and_bus, menu);
		return true;
	}

}
