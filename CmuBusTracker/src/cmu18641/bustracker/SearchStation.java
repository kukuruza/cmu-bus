package cmu18641.bustracker;

import java.util.ArrayList;
import cmu18641.bustracker.adapter.StopAdapter;
import cmu18641.bustracker.entities.Connector;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * SearchStation.java
 * 
 * Activity to view list of stations matching search 
 * input on locateStation activity
 */

public class SearchStation extends Activity {
	
	public static final String STOP_SELECTED = "stop_selected";
	private String addressSearchQuery; 
	
	private ArrayList<Stop> stationList;  
	private StopAdapter stopAdapter; 
	
	private GestureDetector gestureDetector;
    private OnTouchListener gestureListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_station);
		Log.d("SearchStationActivity", "onCreate()");
		
		Bundle data = getIntent().getExtras(); 
		if(data != null) { 
			// grab string query from locateStation activity
			addressSearchQuery = data.getString(LocateStation.SEARCH_QUERY_ID); 
		}
		else { 
			// if bundle is null, return to previous activity
			finish(); 
		}
		
		try { 
			stationList = Connector.globalManager.getStopByAddress(addressSearchQuery);
		} catch (TrackerException te) { 
			// log and recover
			te.printStackTrace();
		}
		
		// listen for gestures
        gestureDetector = new GestureDetector(this, new SwipeDetector(this));
        gestureListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
		
		if(stationList != null) { 
			// stop adapter is used to map stops to the listview
			stopAdapter = new StopAdapter(this, R.layout.activity_search_station, stationList);
			
			// bind adapter and listener
			ListView stationListView = (ListView) findViewById(R.id.searchStopListView);
			stationListView.setAdapter(stopAdapter);
			stationListView.setOnItemClickListener(selectStopListener);
			
			stationListView.setOnTouchListener(gestureListener);   
		}
		else { 
			Builder builder = new AlertDialog.Builder(SearchStation.this);
		    builder.setMessage("No buses found matching \"" + addressSearchQuery +"\"");
		    builder.setCancelable(false); 
		    builder.setPositiveButton(R.string.search_again,
		            new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		            finish(); 
		        }
		    });
		    
		    AlertDialog dialog = builder.create();
		    dialog.show();
		}
	}

	// user is taken to selectionStationAndBus after touching a station in list
	OnItemClickListener selectStopListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View parent, int position, long id) {
			Intent showSelectStationAndBus = new Intent(SearchStation.this, SelectStationAndBus.class);
			showSelectStationAndBus.putExtra(STOP_SELECTED, stationList.get(position));
			SearchStation.this.startActivity(showSelectStationAndBus);
		}
		   
	};

}
