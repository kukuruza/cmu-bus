package cmu18641.bustracker;

import java.util.ArrayList;
import cmu18641.bustracker.entities.Stop;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * LocateStation.java
 * 
 * Activity to select a bus station from a list view, 
 * or type an address to execute a search for a bus station.
 */

public class LocateStation extends Activity {

	public static final String STOP_ID = "stop_id";
	public static final String SEARCH_QUERY_ID = "address_id";
	private Button searchStationButton;
	private EditText searchAddressEditText; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate_station);
		
		// get references to editTexts
		searchAddressEditText = (EditText) findViewById(R.id.enterAddressEditText);
		searchStationButton = (Button) findViewById(R.id.searchAddressButton);
				
		// must first query for all stops, and sort by distance from user
		
		// query must be called to find all stations
		// should be sorted by distance from user
		ArrayList<Stop> stationList = new ArrayList<Stop>(); 
		
		// bus adapter is used to map those buses to the listview
		ArrayAdapter<Stop> stopAdapter = new ArrayAdapter<Stop>(this, 
		        R.layout.activity_locate_station, stationList);
		
		// Note: using this built in adapter will require Stop class
		// to implement toString()
		
		// bind adapter and set listener
		ListView stationListView = (ListView) findViewById(R.id.stopListView);
		stationListView.setAdapter(stopAdapter);
		stationListView.setOnItemClickListener(selectStopListener);
		
		// listen for button presses
	    searchStationButton = (Button) findViewById(R.id.searchAddressButton);
	    searchStationButton.setOnClickListener(findStationButtonClicked); 
	    
	}

	// user is taken to searchStation activity after entering search text
	// and pressing search button
	OnClickListener findStationButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			String searchAddressQuery = searchAddressEditText.getText().toString();
			
			if(!searchAddressQuery.equals(null)) { 
				Intent showSearchStation = new Intent(LocateStation.this, SearchStation.class);
				showSearchStation.putExtra(SEARCH_QUERY_ID, searchAddressQuery);
				LocateStation.this.startActivity(showSearchStation);
			}
		}
			
	};

		
	// user is taken to selectStationAndBus activity after touching a station in list
	OnItemClickListener selectStopListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent showSelectStationAndBus = new Intent(LocateStation.this, SelectStationAndBus.class);
			showSelectStationAndBus.putExtra(STOP_ID, arg3);
			LocateStation.this.startActivity(showSelectStationAndBus);
		}
	   
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locate_station, menu);
		return true;
	}
	
}
