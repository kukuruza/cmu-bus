package cmu18641.bustracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LocateStation extends Activity {

	// listview of stops ( stop adapter, name, address, distance)
	
	// editText to do a search new stop
	
	// button to execute search 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate_station);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locate_station, menu);
		return true;
	}

}
