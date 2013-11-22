package cmu18641.bustracker.activities;

import java.util.ArrayList;
import cmu18641.bustracker.R;
import cmu18641.bustracker.activities.ShakeDetector.OnShakeListener;
import cmu18641.bustracker.adapter.ScheduleAdapter;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Connector;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ViewSchedule.java
 * 
 * Activity to view list of buses and arrival times for a station
 */

public class ViewSchedule extends Activity {

	private Schedule schedule; 
	private Stop selectedStop; 
	private ArrayList<Bus> selectedBuses; 
	private ScheduleAdapter scheduleAdapter; 
	private ArrayList<ScheduleItem> scheduleItemList;
	
	private TextView stopNameTextView; 
	private TextView stopDistanceTextView;
	private TextView stopWalkingDistanceTextView;
	
	private GestureDetector gestureDetector;
    private OnTouchListener gestureListener;
    
    private ShakeDetector shakeDetector;
    private SensorManager sensorManager;
    private Sensor accelerometer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_schedule);
		Log.d("ViewScheduleActivity", "OnCreate()");
		
		stopNameTextView = (TextView) findViewById(R.id.viewschedule_stopNameTextView);
		stopDistanceTextView = (TextView) findViewById(R.id.viewschedule_distanceTextView);
		stopWalkingDistanceTextView = (TextView) findViewById(R.id.viewschedule_walkingDistanceTextView);
		
		Bundle data = getIntent().getExtras(); 
		if(data != null) { 
			// grab station and selected buses from selectStationAndBus
			selectedStop = data.getParcelable(LocateStation.STOP_SELECTED); 
			selectedBuses = data.getParcelableArrayList(SelectStationAndBus.BUSES_SELECTED);
		}
		else { 
			// if bundle is null, return to previous activity
			finish(); 
		}
		
		fetchListViewData(); 
				
		// schedule adapter is used to map the scheduleitems to the listview
		scheduleAdapter = new ScheduleAdapter(this, R.layout.activity_select_station_and_bus, scheduleItemList);
				
		// bind adapter and listener
		ListView timeListView = (ListView) findViewById(R.id.scheduleListView);
		timeListView.setAdapter(scheduleAdapter);
		
		// listen for gestures
        gestureDetector = new GestureDetector(this, new SwipeDetector(this));
        gestureListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        
        timeListView.setOnTouchListener(gestureListener);  
        
        // listen for shakes
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(shakeListener, this); 
	}
	
	// refresh data when shaking
	OnShakeListener shakeListener = new OnShakeListener() { 
		@Override
        public void onShake() {
			fetchListViewData();  
			scheduleAdapter.notifyDataSetChanged();
			
			Toast refresh = Toast.makeText(ViewSchedule.this, "Refreshing List", Toast.LENGTH_SHORT);
			refresh.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 50);
			refresh.show();
        }
	};
	
	// refresh data when resuming
	@Override
	protected void onResume() {
		super.onResume();
		
		// register shake listener
		sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
		
		// update listview
		fetchListViewData();  
		scheduleAdapter.notifyDataSetChanged();
		
		Log.d("ViewScheduleActivity", "onResume()");
	}
	
	// unregister shake listener
	@Override
    protected void onPause() {
        sensorManager.unregisterListener(shakeDetector); 
        super.onPause();
    }   
	
	// fetch new data for page
	private void fetchListViewData() { 
		try {
			schedule = Connector.globalManager.getSchedule(getApplicationContext(), selectedStop, selectedBuses);
		} catch (TrackerException e) {
			// log and recover
			e.printStackTrace();
		}
					
		scheduleItemList = schedule.getScheduleItemList(); 
					
		// reset header textViews
		stopNameTextView.setText(selectedStop.getName()); 
		stopDistanceTextView.setText(selectedStop.getDistanceString() + " miles");            
		stopWalkingDistanceTextView.setText(selectedStop.getWalkingTimeString() + " minutes"); 
	}
}
