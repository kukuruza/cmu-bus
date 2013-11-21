package cmu18641.bustracker;

import java.util.ArrayList;
import cmu18641.bustracker.adapter.StopAdapter;
import cmu18641.bustracker.entities.Connector;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_station);
		
		Bundle data = getIntent().getExtras(); 
		
		// grab selected string query from locateStation activity
		if(data != null) { 
			addressSearchQuery = data.getString(LocateStation.SEARCH_QUERY_ID); 
		}
		else { 
			// if bundle is null, throw an error
		}
		
		// call query to return related stops
		// hook up to query manager
		try { 
			stationList = Connector.globalManager.getStopByAddress(addressSearchQuery);
		} catch (TrackerException te) { 
			
		}
		
		// stop adapter is used to map those buses to the listview
		stopAdapter = new StopAdapter(this, 
				      R.layout.activity_search_station, stationList);
			
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

}
