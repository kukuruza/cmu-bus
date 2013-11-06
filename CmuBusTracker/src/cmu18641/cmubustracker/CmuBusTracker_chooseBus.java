package cmu18641.cmubustracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CmuBusTracker_chooseBus extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmu_bus_tracker_choose_bus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cmu_bus_tracker_choose_bus, menu);
		return true;
	}

}
