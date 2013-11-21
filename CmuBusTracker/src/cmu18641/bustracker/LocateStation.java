package cmu18641.bustracker;

import java.util.ArrayList;
import cmu18641.bustracker.ShakeDetector.OnShakeListener;
import cmu18641.bustracker.adapter.StopAdapter;
import cmu18641.bustracker.entities.Connector;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/*
 * LocateStation.java
 * 
 * Activity to select a bus station from a list view, 
 * or type an address to execute a search for a bus station.
 */

public class LocateStation extends Activity {

	public static final String STOP_SELECTED = "stop_selected";
	public static final String SEARCH_QUERY_ID = "address_id";
	
	private Button searchStationButton;
	private EditText searchAddressEditText; 
	private ListView stationListView; 
	
	private ArrayList<Stop> stationList; 
	private StopAdapter stopAdapter; 
	
	private GestureDetector gestureDetector;
    private OnTouchListener gestureListener;
    
    private ShakeDetector shakeDetector;
    private SensorManager sensorManager;
    private Sensor accelerometer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate_station);
		Log.d("LocateStationActivity", "onCreate()");
		
		// get references to views
		searchAddressEditText = (EditText) findViewById(R.id.enterAddressEditText);
		searchStationButton = (Button) findViewById(R.id.searchAddressButton);
		stationListView = (ListView) findViewById(R.id.stopListView);
		
		try {
   		  	stationList = Connector.globalManager.getStopsByCurrentLocation(LocateStation.this);
   	  	} catch (TrackerException e) {
   		  // log and recover
   		  e.printStackTrace();
   	  	}
		
		// bus adapter is used to map buses to the listview
		stopAdapter = new StopAdapter(this, R.layout.activity_locate_station, stationList);
		
		// bind adapter and set listener
		stationListView.setAdapter(stopAdapter);
		stationListView.setOnItemClickListener(selectStopListener);
		
		// listen for button presses
	    searchStationButton.setOnClickListener(findStationButtonClicked); 
	    
	    // listen for gestures
        gestureDetector = new GestureDetector(this, new SwipeDetector(this));
        gestureListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        
        stationListView.setOnTouchListener(gestureListener);  
        
        // listen for shakes
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(shakeListener); 
	}

	// refresh data when shaken
	OnShakeListener shakeListener = new OnShakeListener() { 
		@Override
        public void onShake() {
			// update listview
			try {
	   		  	stationList = Connector.globalManager.getStopsByCurrentLocation(LocateStation.this);
	   	  	} catch (TrackerException e) {
	   		  // log and recover
	   		  e.printStackTrace();
	   	  	}
			
			stopAdapter.notifyDataSetChanged();
			
			Toast refresh = Toast.makeText(LocateStation.this, "Refreshing List", Toast.LENGTH_SHORT);
			refresh.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 150);
			refresh.show();
        }
	};
	
	// allow schedule to refresh 
	@Override
	protected void onResume() {
		super.onResume();
		
		// register shake listener
		sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
		
		// update listview
		try {
   		  	stationList = Connector.globalManager.getStopsByCurrentLocation(LocateStation.this);
   	  	} catch (TrackerException e) {
   		  // log and recover
   		  e.printStackTrace();
   	  	}
		
		stopAdapter.notifyDataSetChanged();
		Log.d("ViewScheduleActivity", "onResume()");
	}
	
	// unregister shake listener
	@Override
    protected void onPause() {
        sensorManager.unregisterListener(shakeDetector); 
        super.onPause();
    }   
	
	// user is taken to searchStation activity after entering search text
	// and pressing search button
	OnClickListener findStationButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			String searchAddressQuery = searchAddressEditText.getText().toString();
			
			if(!searchAddressQuery.equals("")) { 
				Intent showSearchStation = new Intent(LocateStation.this, SearchStation.class);
				showSearchStation.putExtra(SEARCH_QUERY_ID, searchAddressQuery);
				LocateStation.this.startActivity(showSearchStation);
			}
			else { 
				Builder builder = new AlertDialog.Builder(LocateStation.this);
			    builder.setMessage("Enter an address to search for a stop, or " +
			    						"select a nearby stop from the list");
			    builder.setCancelable(true); 
			    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			            dialog.cancel();
			        }
			    });
			    
			    AlertDialog dialog = builder.create();
			    dialog.show();
			}
		}
			
	};

		
	// user is taken to selectStationAndBus activity after touching a station in list
	OnItemClickListener selectStopListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View parent, int position, long id) {
			Intent showSelectStationAndBus = new Intent(LocateStation.this, SelectStationAndBus.class);
			showSelectStationAndBus.putExtra(STOP_SELECTED, stationList.get(position)); 
			LocateStation.this.startActivity(showSelectStationAndBus);
		}
	   
	};
	
}
