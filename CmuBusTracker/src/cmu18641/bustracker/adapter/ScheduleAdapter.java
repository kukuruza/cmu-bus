package cmu18641.bustracker.adapter;

import java.util.ArrayList;
import cmu18641.bustracker.R;
import cmu18641.bustracker.entities.ScheduleItem;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
 * ScheduleAdapter.java
 * 
 * Maps a scheduleItem to textviews on viewSchedule activity
 */

public class ScheduleAdapter extends ArrayAdapter<ScheduleItem> {
	
	Context context; 
	ArrayList<ScheduleItem> scheduleItemList; 
	ViewHolder viewHolder; 
	
	public ScheduleAdapter(Context context, int resource, ArrayList<ScheduleItem> scheduleItemList) {
		super(context, resource, scheduleItemList );
		this.scheduleItemList = scheduleItemList; 
		this.context = context; 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) { 
			LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.schedule_list_item, null);
			viewHolder = new ViewHolder();
		
			viewHolder.name = (TextView) convertView.findViewById(R.id.scheduleitem_busname); 
			viewHolder.direction = (TextView) convertView.findViewById(R.id.scheduleitem_busdirection); 
			viewHolder.arrivalTime = (TextView) convertView.findViewById(R.id.scheduleitem_busarrivaltime);
			
			// link the cached views to the convertView
		    convertView.setTag(viewHolder);
		}
		else { 
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.name.setText(scheduleItemList.get(position).getBus().getName());
		viewHolder.direction.setText(scheduleItemList.get(position).getBus().getDirection()); 
		
		// prepare arrival time string
		int arrivalMinutes = scheduleItemList.get(position).getTime().minute; 
		int arrivalHours = scheduleItemList.get(position).getTime().hour; 
		String stamp = "AM";
		
		if(arrivalHours >= 12) { 
			stamp = "PM"; 
		}
		
		if(arrivalHours > 12) { 
			arrivalHours = arrivalHours - 12; 
		}
		
		
		
		viewHolder.arrivalTime.setText(String.format(arrivalHours + ":%02d " + stamp, arrivalMinutes));
		
		Log.d("ScheduleAdapter - getView()", "position= " + position);
			
		return convertView;
	}
	
	private class ViewHolder { 
		TextView name; 
		TextView direction; 
		TextView arrivalTime; 
	}
}
