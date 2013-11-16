package cmu18641.bustracker.adapter;

import java.util.ArrayList;
import cmu18641.bustracker.R;
import cmu18641.bustracker.entities.Bus;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/*
 * BusAdapter.java
 * 
 * Maps bus arraylist to textviews on selectStationAndBus activity
 */

public class BusAdapter extends ArrayAdapter<Bus> {
	
	private boolean[] checkBoxState;
	private Context context;
	private ViewHolder viewHolder; 
	private ArrayList<Bus> busList; 
	
	public BusAdapter(Context context, int resource, ArrayList<Bus> busList) {
		super(context, resource, busList);
		checkBoxState = new boolean[busList.size()];
		this.context = context; 
		this.busList = busList; 
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup viewGroup) {
		
		if(convertView == null) {
			LayoutInflater inflater = 
					(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.bus_list_item, null);
			viewHolder = new ViewHolder();
		 
		    //cache the views
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		    viewHolder.name = (TextView) convertView.findViewById(R.id.busname);
		    viewHolder.direction = (TextView) convertView.findViewById(R.id.busdirection);
		    
		    // link the cached views to the convertView
		    convertView.setTag(viewHolder);
	 
		}
		else { 
			viewHolder = (ViewHolder) convertView.getTag();
		}
			
		//set the data to be displayed
		viewHolder.name.setText(busList.get(position).getName().toString());
		viewHolder.direction.setText(busList.get(position).getDirection().toString());	    
		viewHolder.checkBox.setChecked(checkBoxState[position]);        

		Log.v("BusAdapter - getView()", "position= " + position);

		//return the view to be displayed
		return convertView;
	}
	
	// set the state of each check box
	public void setCheckBoxState(int position, boolean value) { 
		checkBoxState[position] = value; 
	}
	
    // class for caching the views in a row  
	private class ViewHolder {
		TextView name;
		TextView direction;
		CheckBox checkBox;
	}

} // end BusAdapter
