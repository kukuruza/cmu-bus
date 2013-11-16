package cmu18641.bustracker;

import java.util.ArrayList;

import cmu18641.bustracker.entities.Stop;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_station);
		
		Bundle extras = getIntent().getExtras(); 
		
		// grab selected string query from locateStation activity
		if(extras != null) { 
			addressSearchQuery = extras.getString(LocateStation.SEARCH_QUERY_ID); 
		}
		else { 
			// if bundle is null, throw an error
		}
		
		// call query to return related stops
		// hook up to query manager
		stationList = new ArrayList<Stop>(); 
				
		// bus adapter is used to map those buses to the listview
		ArrayAdapter<Stop> stopAdapter = new ArrayAdapter<Stop>(this, 
				      R.layout.activity_search_station, stationList);
				
		// Note: using this built in adapter will require Stop class
		// to implement toString()
			
		// bind adapter and listener
		ListView stationListView = (ListView) findViewById(R.id.searchStopListView);
		stationListView.setAdapter(stopAdapter);
		stationListView.setOnItemClickListener(selectStopListener);
		
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_station, menu);
		return true;
	}

}
