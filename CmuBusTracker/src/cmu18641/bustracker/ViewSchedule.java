package cmu18641.bustracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ViewSchedule extends Activity {

	
	// stop 
	
	// has buses and their arrival times 
	
	/// not sure if we need adapter for this
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_schedule);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_schedule, menu);
		return true;
	}

}
