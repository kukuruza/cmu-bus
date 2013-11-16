package cmu18641.bustracker;

import java.util.ArrayList;
import cmu18641.bustracker.adapter.StopAdapter;
import cmu18641.bustracker.entities.Connector;
import cmu18641.bustracker.entities.Stop;
import cmu18641.bustracker.exceptions.TrackerException;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener; 
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.GestureDetector;

/*
 * LocateStation.java
 * 
 * Activity to select a bus station from a list view, 
 * or type an address to execute a search for a bus station.
 */

// onFocuslistener for edit text

public class LocateStation extends Activity {

	public static final String STOP_SELECTED_NAME = "stop_selected";
	public static final String SEARCH_QUERY_ID = "address_id";
	private Button searchStationButton;
	private EditText searchAddressEditText; 
	
	ArrayList<Stop> stationList; 
	StopAdapter stopAdapter; 
	
	private GestureDetector mDetector; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate_station);
		
		Log.v("LocateStation", "onCreate()");
		
		View view = findViewById(R.id.locatestationlayout);
		
		mDetector = new GestureDetector(this, new MyGestureListener());
		
		view.setOnTouchListener(new OnTouchListener(){

			   public boolean onTouch(View v, MotionEvent event) {
				   Log.v("onTouch", "touched"); 
				   mDetector.onTouchEvent(event);
				   return true; 
			   }
		 });
		
		
		// get references to editTexts
		searchAddressEditText = (EditText) findViewById(R.id.enterAddressEditText);
		searchStationButton = (Button) findViewById(R.id.searchAddressButton);
				
		// query must be called to find all stations
		// should be sorted by distance from user
		try {
			stationList = Connector.globalManager.getStopsByCurrentLocation(LocateStation.this);
		} catch (TrackerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
	
		// bus adapter is used to map those buses to the listview
		stopAdapter = new StopAdapter(this, 
		        R.layout.activity_locate_station, stationList);
		
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
			else { 
				Builder builder = new AlertDialog.Builder(LocateStation.this);
			    builder.setMessage("Enter an address to search for a stop, or " +
			    						"select a nearby stop from the list");
			    builder.setCancelable(true); 
			    builder.setPositiveButton(android.R.string.ok,
			            new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			            dialog.cancel();
			        }
			    });
			    
			    AlertDialog dialog = builder.create();
			    dialog.show();
			}
		}
			
	};

		
	// user is taken to selectStationAndBus activity after touching a station in list
	OnItemClickListener selectStopListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View parent, int position, long id) {
			Intent showSelectStationAndBus = new Intent(LocateStation.this, SelectStationAndBus.class);
			Bundle b = new Bundle();
			//.putExtra(STOP_SELECTED, stationList.get(position));
			showSelectStationAndBus.putExtra(STOP_SELECTED_NAME, stationList.get(position)); 
			LocateStation.this.startActivity(showSelectStationAndBus);
		}
	   
	};
	
    //@Override 
    //public boolean onTouchEvent(MotionEvent event){ 
    //	Log.v("gesture", "touch event occured"); 
    //    //this.mDetector.onTouchEvent(event);
    //    //return super.onTouchEvent(event);
    //	return mDetector.onTouchEvent(event); 
    //}
    
    
    private class MyGestureListener extends SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures"; 
        
        @Override
        public boolean onDown(MotionEvent event) { 
            Log.v(DEBUG_TAG,"onDown: " + event.toString()); 
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, 
                float velocityX, float velocityY) {
            Log.v(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            return true;
        }
        
        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
        	Log.v(DEBUG_TAG, "onSingleTapUP"); 
            //currentGestureDetected=ev.toString();
         
          return true;
        }
        @Override
        public void onShowPress(MotionEvent ev) {
            //currentGestureDetected=ev.toString();
        	Log.v(DEBUG_TAG, "onShowPress"); 
   
          
        	
        }
        @Override
        public void onLongPress(MotionEvent ev) {
            //currentGestureDetected=ev.toString();
        	Log.v(DEBUG_TAG, "onLongPress"); 
         
        }
    }
	
/*
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
    
    */
    
    /*

	private void onLeftSwipe() {
		Toast t = Toast.makeText(Home.this, "Left swipe", Toast.LENGTH_LONG);
		t.show();
		go = new Intent("test.apps.FLORA");
		startActivity(go);
	}

	private void onRightSwipe() {
		Toast t = Toast.makeText(Home.this, "Right swipe", Toast.LENGTH_LONG);
		t.show();
		go = new Intent("test.apps.FAUNA");
		startActivity(go);
	}

	private class SwipeGestureDetector extends SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 50;
		private static final int SWIPE_MAX_OFF_PATH = 200;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				Toast t = Toast.makeText(Home.this, "Gesture detected", Toast.LENGTH_SHORT);
				t.show();
				float diffAbs = Math.abs(e1.getY() - e2.getY());
				float diff = e1.getX() - e2.getX();

				if (diffAbs > SWIPE_MAX_OFF_PATH)
					return false;

				// Left swipe
				if (diff > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Home.this.onLeftSwipe();
				} 
				// Right swipe
				else if (-diff > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Home.this.onRightSwipe();
				}
			} catch (Exception e) {
				Log.e("Home", "Error on gestures");
			}
		return false;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locate_station, menu);
		return true;
	}
	*/
	
}
