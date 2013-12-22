package bustracker.android.adapter;

import java.util.ArrayList;

import bustracker.android.entities.Stop;
import cmu18641.bustracker.R;
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
	
	private Context context; 
	private ArrayList<Stop> stopList; 
	private ViewHolder viewHolder; 
	
	private static final String FavoriteLabel = "* ";
	private ArrayList<String> favoriteLabelsList;
	
	public StopAdapter(Context context, int resource, ArrayList<Stop> stopList,
			int numFavorites) {
		super(context, resource, stopList);
		this.context = context; 
		this.stopList = stopList;
		
		// populate favoriteLabelsList list with '* ' and empty strings
		favoriteLabelsList = new ArrayList<String>();
		for (int i = 0; i != stopList.size(); ++i)
			if (i < numFavorites)
    			favoriteLabelsList.add(FavoriteLabel);
			else
				favoriteLabelsList.add("");
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
					
		viewHolder.name.setText(favoriteLabelsList.get(position) + stopList.get(position).getName());
		viewHolder.address.setText(stopList.get(position).getAddress());
		viewHolder.walkingDistance.setText(stopList.get(position).getWalkingTimeString() + " min.");
		viewHolder.distance.setText(stopList.get(position).getDistanceString() + " miles"); 
		
	    Log.d("StopAdapter - getView()", "position= " + position);
	    
		return convertView;
	}
	
	// class for caching the views in a row  
	private class ViewHolder { 
		TextView name;
		TextView address;
		TextView walkingDistance; 
		TextView distance;
	}	

} // end StopAdapter
