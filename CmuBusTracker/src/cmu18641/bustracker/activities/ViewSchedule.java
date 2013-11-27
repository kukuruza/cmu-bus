package cmu18641.bustracker.activities;

import java.util.ArrayList;
import cmu18641.bustracker.R;
import cmu18641.bustracker.activities.ShakeDetector.OnShakeListener;
import cmu18641.bustracker.adapter.ScheduleAdapter;
import cmu18641.bustracker.entities.Bus;
import cmu18641.bustracker.entities.Schedule;
import cmu18641.bustracker.entities.ScheduleItem;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import cmu18641.bustracker.ws.TimeQueryManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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

	private Schedule _schedule; 
	private Stop _selectedStop; 
	private ArrayList<Bus> _selectedBuses; 
	private ScheduleAdapter _scheduleAdapter; 
	private ArrayList<ScheduleItem> _scheduleItemList;
	
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
			_selectedStop = data.getParcelable(LocateStation.STOP_SELECTED); 
			_selectedBuses = data.getParcelableArrayList(SelectStationAndBus.BUSES_SELECTED);
		}
		else { 
			// if bundle is null, return to previous activity
			finish(); 
		}
		
		fetchListViewData(); 
		
		
		// listen for gestures
        gestureDetector = new GestureDetector(this, new SwipeDetector(this));
        gestureListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
		
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
			_scheduleAdapter.notifyDataSetChanged();
			
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
		
		Log.d("ViewScheduleActivity", "onResume()");
	}
	
	// unregister shake listener
	@Override
    protected void onPause() {
        sensorManager.unregisterListener(shakeDetector); 
        super.onPause();
    }   
	
	// fetch new data for page
	private void fetchListViewData()
	{
		class ExecuteTimeQuery extends AsyncTask<Void, Void, Void> {
			
			Schedule schedule;
			
			@Override
			protected Void doInBackground(Void... params) {
				try {
					TimeQueryManager timeQueryManager = new TimeQueryManager();
					schedule = timeQueryManager.getSchedule(getApplicationContext(), _selectedStop, _selectedBuses); 
				} catch (TrackerException e) {
					// log and recover
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				_schedule = schedule;
				_scheduleItemList = _schedule.getScheduleItemList(); 
				
				if(_scheduleAdapter != null) { 
					_scheduleAdapter.clear(); 
					_scheduleAdapter.addAll(_scheduleItemList); 
					_scheduleAdapter.notifyDataSetChanged();
				}
							
				// reset header textViews
				stopNameTextView.setText(_selectedStop.getName()); 
				stopDistanceTextView.setText(_selectedStop.getDistanceString() + " miles");            
				stopWalkingDistanceTextView.setText(_selectedStop.getWalkingTimeString() + " minutes"); 

				if(!_scheduleItemList.isEmpty()) { 
					
					// schedule adapter is used to map the scheduleitems to the listview
					_scheduleAdapter = new ScheduleAdapter(getApplicationContext(), R.layout.activity_select_station_and_bus, _scheduleItemList);
						
					// bind adapter and listener
					ListView timeListView = (ListView) findViewById(R.id.scheduleListView);
					timeListView.setAdapter(_scheduleAdapter);
					timeListView.setOnTouchListener(gestureListener);  
				}
				else { 
					Builder builder = new AlertDialog.Builder(ViewSchedule.this);
				    builder.setMessage("Sorry, no more buses for this stop are coming today. :(");
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
		}
		
		ExecuteTimeQuery exec = new ExecuteTimeQuery();
		exec.execute();
	}
}
