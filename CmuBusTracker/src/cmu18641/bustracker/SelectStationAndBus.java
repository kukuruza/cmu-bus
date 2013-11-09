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
	
	private int busSelections[]; 

	Button findNextBusButton; 
	Button findStationButton; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_station_and_bus);
		
		// check for non null bundle and grab stopId
		// if bundle is null, query must be called 
		// to find default station (closest to user)
		
		// then the text in station button must be set accordingly
		findStationButton.setText(""); 
		
		// query must be called to find buses that pass thru the selected station
		// returning an array list of buses 
		ArrayList<Bus> busList = new ArrayList<Bus>(); 
		
		// bus adapter is used to map those buses to the listview
		ArrayAdapter<Bus> busAdapter = new ArrayAdapter<Bus>(this, 
		        R.layout.activity_select_station_and_bus, busList);
		
		// Note: using this built in adapter will require Bus class
		// to implement toString()
		
		// bind adapter
		ListView busListView = (ListView) findViewById(R.id.busListView);
		busListView.setAdapter(busAdapter);
		
		// listen for button presses
		findNextBusButton = (Button) findViewById(R.id.findNextBusButton);
	    findNextBusButton.setOnClickListener(findNextBusButtonClicked); 
	    
	    findStationButton = (Button) findViewById(R.id.findStationButton);
        findStationButton.setOnClickListener(findStationButtonClicked); 
        
        // listen for bus items selected
		busListView.setOnItemClickListener(selectBusListener);
	}

	
	// responds to event generated when user clicks the findNextBusButton
	OnClickListener findNextBusButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent showSchedule = new Intent(SelectStationAndBus.this, ViewSchedule.class);
			
			// use bundle to with this intent to pass rowId of buses selected
			SelectStationAndBus.this.startActivity(showSchedule);
		}
				
	};
		

	// responds to event generated when user clicks the findStationButton
	OnClickListener findStationButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent showLocateStation = new Intent(SelectStationAndBus.this, LocateStation.class);
			SelectStationAndBus.this.startActivity(showLocateStation);
		}
					
	};
			
	
	// event listener that responds to the user touching a mortageCalc in list
	OnItemClickListener selectBusListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
			// check the box and add rowId to array
		}
		   
	}; 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_station_and_bus, menu);
		return true;
	}

}
