package bustracker.android.activities;

import java.util.ArrayList;

import bustracker.android.adapter.StopAdapter;
import bustracker.android.entities.Stop;
import bustracker.android.exceptions.TrackerException;
import bustracker.android.helpers.Connector;
import bustracker.android.helpers.SimpleDialogBuilderHelper;
import bustracker.android.helpers.SwipeDetector;
import cmu18641.bustracker.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class SearchStationActivity extends Activity {
	
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
			addressSearchQuery = data.getString(LocateStationActivity.SEARCH_QUERY_ID); 
		}
		else { 
			// if bundle is null, return to previous activity
			finish(); 
		}
		
		try { 
			stationList = Connector.globalManager.getStopByAddress(SearchStationActivity.this, addressSearchQuery);
		} catch (TrackerException te) { 
			new SimpleDialogBuilderHelper(SearchStationActivity.this, "Please restart the app", "Ok");	
   	  		Log.e("SearchStation", "exception", te);
		}
		
		// listen for gestures
        gestureDetector = new GestureDetector(this, new SwipeDetector(this));
        gestureListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
		
		if(!stationList.isEmpty()) { 
			// stop adapter is used to map stops to the listview
			stopAdapter = new StopAdapter(this, R.layout.activity_search_station, stationList, 0);
			
			// bind adapter and listener
			ListView stationListView = (ListView) findViewById(R.id.searchStopListView);
			stationListView.setAdapter(stopAdapter);
			stationListView.setOnItemClickListener(selectStopListener);
			
			stationListView.setOnTouchListener(gestureListener);   
		}
		else { 
			Builder builder = new AlertDialog.Builder(SearchStationActivity.this);
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
			Intent showSelectStationAndBus = new Intent(SearchStationActivity.this, SelectStationAndBusActivity.class);
			showSelectStationAndBus.putExtra(STOP_SELECTED, stationList.get(position));
			SearchStationActivity.this.startActivity(showSelectStationAndBus);
		}
		   
	};
	
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
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		String message = getString (R.string.searchStationActivityHelp); 
		new SimpleDialogBuilderHelper(SearchStationActivity.this, message, "Ok"); 
		return super.onOptionsItemSelected(item);
	} 	

}
