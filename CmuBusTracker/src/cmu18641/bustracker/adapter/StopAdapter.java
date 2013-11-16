package cmu18641.bustracker.adapter;

import java.util.ArrayList;
import java.util.Locale;
import cmu18641.bustracker.R;
import cmu18641.bustracker.entities.Stop;
import android.content.Context;
import android.util.Log;
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
	
	private Context context; 
	private ArrayList<Stop> stopList; 
	private ViewHolder viewHolder; 
	
	public StopAdapter(Context context, int resource, ArrayList<Stop> stopList) {
		super(context, resource, stopList);
		this.context = context; 
		this.stopList = stopList; 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.stop_list_item, null);
			viewHolder = new ViewHolder();
		
			viewHolder.name = (TextView) convertView.findViewById(R.id.stopName);    
			viewHolder.address = (TextView) convertView.findViewById(R.id.stopAddress); 
			viewHolder.walkingDistance = (TextView) convertView.findViewById(R.id.walkingdistance); 
			viewHolder.distance = (TextView) convertView.findViewById(R.id.distance); 
			
			// link the cached views to the convertView
		    convertView.setTag(viewHolder);
			 
		}
		else { 
			viewHolder = (ViewHolder) convertView.getTag();
		}
					
		viewHolder.name.setText(stopList.get(position).getName());
		viewHolder.address.setText(stopList.get(position).getAddress());
		viewHolder.walkingDistance.setText(getWalkingTimeString(stopList.get(position)) + " minutes");
		viewHolder.distance.setText(getDistanceString(stopList.get(position)) + " miles"); 
		
	    Log.v("StopAdapter - getView()", "position= " + position);
	    
		return convertView;
	}
	
	// class for caching the views in a row  
	private class ViewHolder { 
		TextView name;
		TextView address;
		TextView walkingDistance; 
		TextView distance;
	}	
	
	// utility methods to extract and convert stop distance
	private String getDistanceString(Stop stop) { 
		return String.format(Locale.US, MilesStringFormat, stop.getDistance());
	}
	
	private String getWalkingTimeString(Stop stop) { 
		return String.format (Locale.US, "%.1f", stop.getDistance() / AvgSpeedMilesPerMin);
	}

} // end StopAdapter
