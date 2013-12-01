package cmu18641.bustracker.activities;

import helpers.Favorites;

import java.util.ArrayList;

import cmu18641.bustracker.R;
import cmu18641.bustracker.activities.ShakeDetector.OnShakeListener;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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
	public static final String TAG = "LocateStation";
	
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
	
    // go through stops to find the one with given name
    private Stop findStopByName (ArrayList<Stop> stops, String searchStopName)
    {
    	assert (searchStopName != null);
    	for (Stop stop : stops)
    		if (stop.getName().equals(searchStopName))
    			return stop;
    	return null;	
    }
    
    private void addFavoritesList () {
		// get favorite stops
		ArrayList<Stop> stationListFavorites = new ArrayList<Stop> ();
		ArrayList<String> favorites = Favorites.getFavorites (this);
		if (favorites == null)
			return;
		for (String favorite : favorites)
		{
			Stop stop = findStopByName(stationList, favorite);
			if (stop != null)
			{
				stationListFavorites.add(stop);
    		    Log.d (TAG, "found stop with name: " + favorite);
			}
			else
			{
    		    Log.e (TAG, "NOT found stop with name: " + favorite);
    		    stationListFavorites.clear();
    		    break;
			}
		}
		
		// add favorites in front of all
		stationListFavorites.addAll(stationList);
		stationList = stationListFavorites;
    }
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate_station);
		Log.d("LocateStationActivity", "onCreate()");
		
		// get references to views
		searchAddressEditText = (EditText) findViewById(R.id.enterAddressEditText);
		searchStationButton = (Button) findViewById(R.id.searchAddressButton);
		stationListView = (ListView) findViewById(R.id.stopListView);
		
		// get all stops
		try {
   		 	stationList = Connector.globalManager.getStopsByCurrentLocation(LocateStation.this);
   			addFavoritesList();
   	  	} catch (TrackerException e) {
   	  		new SimpleDialogBuilderHelper(LocateStation.this, "Please restart the app", "Ok");	
   	  		Log.e("LocateStation", "exception", e);
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
        shakeDetector = new ShakeDetector(shakeListener, this); 
	}

	
	@Override
	protected void onResume() {
		super.onResume();
			
		// register shake listener
		sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
			
		try {
	   	  	stationList = Connector.globalManager.getStopsByCurrentLocation(LocateStation.this);
	   	    addFavoritesList();
	   	} catch (TrackerException e) {
	   		new SimpleDialogBuilderHelper(LocateStation.this, "Please restart the app", "Ok");	
   	  		Log.e("LocateStation", "exception", e);
	    }
		
		if(stopAdapter != null) { 
			stopAdapter.clear(); 
			stopAdapter.addAll(stationList); 
			stopAdapter.notifyDataSetChanged();
		}
		
		Log.d("LocateStationActivity", "onResume()");
	}
		

	@Override
	protected void onPause() {
		// unregister shake listener
	    sensorManager.unregisterListener(shakeDetector); 
	    super.onPause();
	}   	
	
	
	// refresh data when shaken
	OnShakeListener shakeListener = new OnShakeListener() { 
		@Override
        public void onShake() {

			try {
	   		  	stationList = Connector.globalManager.getStopsByCurrentLocation(LocateStation.this);
	   		    addFavoritesList();
	   	  	} catch (TrackerException e) {
	   	  		new SimpleDialogBuilderHelper(LocateStation.this, "Please restart the app", "Ok");	
	   	  		Log.e("LocateStation", "exception", e);
	   	  	}
			
			
			if(stopAdapter != null) { 
				stopAdapter.clear(); 
				stopAdapter.addAll(stationList); 
				stopAdapter.notifyDataSetChanged();
			}
			
			Toast refresh = Toast.makeText(LocateStation.this, "Refreshing List", Toast.LENGTH_SHORT);
			refresh.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 200);
			refresh.show();
        }
	};
	
	
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

	
	
	/// ---- menu ---- ///
	
	// create the Activity's menu from a menu resource XML file
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.help_menu, menu);
		return true;
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		String message = new String("Select a new station by tapping on one from the list, or if " + 
				"you don't see the one you want, enter a street name below to look up the one you had " + 
				"in mind"); 

		new SimpleDialogBuilderHelper(LocateStation.this, message, "Ok"); 
		return super.onOptionsItemSelected(item);
	} 	
	
}
