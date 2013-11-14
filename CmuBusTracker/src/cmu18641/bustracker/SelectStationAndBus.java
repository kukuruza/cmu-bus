package cmu18641.bustracker;

import java.util.ArrayList;
import cmu18641.bustracker.adapter.BusAdapter;
import cmu18641.bustracker.entities.Bus;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
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
	
	public static final String BUS_ARRAY_ID = "rowarray_id";
	public static final String STOP_ID = "row_id";
	
	private ArrayList<Integer> busSelections;
	private long stopID = -1; 
	private BusAdapter busAdapter; 

	private Button findNextBusButton; 
	private Button findStationButton; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_station_and_bus);
		
		Log.v("SelectStationAndBus", "OnCreate()");
		
		findNextBusButton = (Button) findViewById(R.id.findNextBusButton);
		findStationButton = (Button) findViewById(R.id.findStationButton);
		busSelections = new ArrayList<Integer>(); 
		
		// grab selected stop if entering from LocateStation/SearchStation activity
		Bundle extras = getIntent().getExtras(); 
		if(extras != null) { 
			stopID = extras.getLong(LocateStation.STOP_ID); 
		}
		else { 
			// if bundle is null, query must be called 
			// to find default station (closest to user)
		}
		
		// set button text dynamically
		findStationButton.setText("Default Station Goes Here"); 
		
		// query must be called to find buses that pass thru the selected station
		// returning an array list of buses 
		ArrayList<Bus> busList = new ArrayList<Bus>(); 
		
		// populate busList with test data
		String names[] = new String[50];
		
		 for(int i = 0; i< 50; i++){
			 names[i] = "BusName" + i; 
			 Bus temp = new Bus(names[i]);		 
			 busList.add(temp);       
		 }
		
		// bus adapter is used to map those buses to the listview
		busAdapter = new BusAdapter(this, 
		        R.layout.activity_select_station_and_bus, busList);
		
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
				showSchedule.putExtra(BUS_ARRAY_ID, busSelections); 
				showSchedule.putExtra(STOP_ID, stopID);
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
			
			Log.v("SelectStationAndBus - findNextBusButtonClicked", "OnClick()");
			
			
		}
				
	};
		

	// user is taken to locateStation activity after pressing findStationButton
	OnClickListener findStationButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			findStationButton.setText("Clicked"); 
			Intent showLocateStation = new Intent(SelectStationAndBus.this, LocateStation.class);
			SelectStationAndBus.this.startActivity(showLocateStation);
			Log.v("SelectStationAndBus - findStationButtonClicked", "OnClick()");
		}
					
	};
			
	
	// bus is added to selection list when pressed in list view
	// bus is removed from selection list when pressed again in list view
	OnItemClickListener selectBusListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View parent, int position, long id) {
			CheckBox checkbox = (CheckBox) parent.findViewById(R.id.checkBox);

			// pressed again -> deselect
			if(busSelections.contains(position)) { 
				busSelections.remove((Object)position); 
				checkbox.setChecked(false);   
				busAdapter.setCheckBoxState(position, false);
				
			}
			// first press -> select
			else { 
				busSelections.add(position); 
				checkbox.setChecked(true); 
				busAdapter.setCheckBoxState(position, true);
			}
			
			Log.v("onItemClick", busSelections.toString()); 

		}
		   
	}; 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_station_and_bus, menu);
		return true;
	}

}
