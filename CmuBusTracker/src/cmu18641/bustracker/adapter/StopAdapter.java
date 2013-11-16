package cmu18641.bustracker.adapter;

import java.util.ArrayList;
import java.util.Locale;

import cmu18641.bustracker.R;
import cmu18641.bustracker.entities.Stop;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
 * StopAdapter.java
 * 
 * Maps stop arraylist to textviews on LocateStation activity
 * and SearchStation activity
 */

public class StopAdapter extends ArrayAdapter<Stop> {
	
	final static private float MetersToMiles = (float) 0.000621371;
	final static private String MilesStringFormat = "%.1f";
	final static private float AvgSpeedMilesPerMin = (float)(3) / 60;
	
	Context context; 
	ArrayList<Stop> stopList; 
	
	public StopAdapter(Context context, int resource, ArrayList<Stop> stopList) {
		super(context, resource, stopList);
		this.context = context; 
		this.stopList = stopList; 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Stop getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.stop_list_item, null);
		
		TextView stopNameTextView = (TextView) convertView.findViewById(R.id.stopName);    
		TextView stopAddressTextView = (TextView) convertView.findViewById(R.id.stopAddress); 
		TextView walkingDistanceTextView = (TextView) convertView.findViewById(R.id.walkingdistance); 
		TextView distanceTextView = (TextView) convertView.findViewById(R.id.distance); 
		
	    stopNameTextView.setText(stopList.get(position).getName());
	    stopAddressTextView.setText(stopList.get(position).getAddress());
	    walkingDistanceTextView.setText(getWalkingTimeString(stopList.get(position)));
	    distanceTextView.setText(getDistanceString(stopList.get(position))); 
		
		return convertView;
	}
	
	// utility methods to extract and convert stop distance
	private String getDistanceString(Stop stop) { 
		return String.format(Locale.US, MilesStringFormat, stop.getDistance());
	}
	
	private String getWalkingTimeString(Stop stop) { 
		return String.format (Locale.US, "%f.", stop.getDistance() / AvgSpeedMilesPerMin);
	}

} // end StopAdapter
