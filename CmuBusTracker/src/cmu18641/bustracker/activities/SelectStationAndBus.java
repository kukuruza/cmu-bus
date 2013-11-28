package cmu18641.bustracker.activities;

import java.util.ArrayList;
import cmu18641.bustracker.R;
import cmu18641.bustracker.adapter.BusAdapter;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Connector;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * SelectStationAndBus.java
 * 
 * Primary activity to select a bus station and a bus. 
 */

public class SelectStationAndBus extends Activity {
	
	public static final String BUSES_SELECTED = "buses_selected";
	
	private ArrayList<Bus> busSelections;
	private ArrayList<Bus> busList; 
	private Stop selectedStop; 
	private BusAdapter busAdapter; 

	private Button findNextBusButton; 
	private Button findStationButton; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_station_and_bus);
		Log.d("SelectStationAndBusActivity", "OnCreate()");

		findNextBusButton = (Button) findViewById(R.id.findNextBusButton);
		findStationButton = (Button) findViewById(R.id.findStationButton);
		busSelections = new ArrayList<Bus>(); 
		
		Bundle data = getIntent().getExtras(); 
		if(data != null) { 
			// grab selected stop if entering from LocateStation/SearchStation activity
			Log.d("SelectStationAndBusActivity", "Getting selected stop from bundle");
			selectedStop = data.getParcelable(LocateStation.STOP_SELECTED);
		}
		else { 
			// query must be called to find default station (closest to user)
			try { 
				Log.d("SelectStationAndBusActivity", "Querying to find default station");
				selectedStop = Connector.globalManager.getStopsByCurrentLocation(SelectStationAndBus.this).get(0); 
			} catch(TrackerException te) { 
				// log and recover
				te.printStackTrace(); 
			}
		}
		
		// set button text based on selected stop 
		findStationButton.setText("Stop: " + selectedStop.getName() + " (click to change)"); 
		
		// query must be called to find buses that pass thru the selected station
		try { 
			busList = Connector.globalManager.getBusesByStop(SelectStationAndBus.this, selectedStop); 
		} catch(TrackerException te) { 
			// log and recover
			te.printStackTrace();
		}
		
		// bus adapter maps buses to the listview
		busAdapter = new BusAdapter(this, R.layout.activity_select_station_and_bus, busList);
		
		// bind adapter and listener
		ListView busListView = (ListView) findViewById(R.id.busListView);
		busListView.setAdapter(busAdapter);
		busListView.setOnItemClickListener(selectBusListener);
		
		// listen for button presses
	    findNextBusButton.setOnClickListener(findNextBusButtonClicked); 
        findStationButton.setOnClickListener(findStationButtonClicked); 
	}

	@Override
	protected void onResume() {
		super.onResume(); 
	}
	
	// user is taken to viewSchedule activity after selecting at least
	// one bus and pressing findNextBusButton
	OnClickListener findNextBusButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if(!busSelections.isEmpty()) { 
				Intent showSchedule = new Intent(SelectStationAndBus.this, ViewSchedule.class);
				showSchedule.putParcelableArrayListExtra(BUSES_SELECTED, (ArrayList<? extends Parcelable>) busSelections); 
				showSchedule.putExtra(LocateStation.STOP_SELECTED, selectedStop);
				SelectStationAndBus.this.startActivity(showSchedule);
			}
			else { 
				Builder builder = new AlertDialog.Builder(SelectStationAndBus.this);
			    builder.setMessage("Select a bus to continue");
			    builder.setCancelable(true); 
			    builder.setPositiveButton(android.R.string.ok,
			            new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			            dialog.cancel();
			        }
			    });
			    
			    AlertDialog dialog = builder.create();
			    dialog.show();
			}
			
			Log.d("SelectStationAndBusActivity - findNextBusButtonClicked", "OnClick()");
			
			
		}
				
	};
		

	// user is taken to locateStation activity after pressing findStationButton
	OnClickListener findStationButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent showLocateStation = new Intent(SelectStationAndBus.this, LocateStation.class);
			SelectStationAndBus.this.startActivity(showLocateStation);
			Log.d("SelectStationAndBusActivity - findStationButtonClicked", "OnClick()");
		}
					
	};
			
	
	// bus is added to selection list when pressed in list view
	// bus is removed from selection list when pressed again in list view
	OnItemClickListener selectBusListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View parent, int position, long id) {
			CheckBox checkbox = (CheckBox) parent.findViewById(R.id.checkBox);

			// pressed again -> remove from selected bus list
			if(busSelections.contains(busList.get(position))) { 
				busSelections.remove(busList.get(position)); 
				checkbox.setChecked(false);   
				busAdapter.setCheckBoxState(position, false);
				
			}
			// first press -> add to selected bus list
			else { 
				busSelections.add(busList.get(position)); 
				checkbox.setChecked(true); 
				busAdapter.setCheckBoxState(position, true);
			}
			
			Log.d("SelectStationAndBusActivity", busSelections.toString()); 

		}
		   
	}; 


}