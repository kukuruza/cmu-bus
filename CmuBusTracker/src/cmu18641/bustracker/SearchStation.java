package cmu18641.bustracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SearchStation extends Activity {

	// list view of stops (stop adapoter)
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_station);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_station, menu);
		return true;
	}

}
