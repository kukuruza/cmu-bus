package cmu18641.bustracker.adapter;

import java.util.ArrayList;
import cmu18641.bustracker.R;
import cmu18641.bustracker.entities.ScheduleItem;
import android.content.Context;
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
	
	public ScheduleAdapter(Context context, int resource, ArrayList<ScheduleItem> scheduleItemList) {
		super(context, resource, scheduleItemList );
		this.scheduleItemList = scheduleItemList; 
		this.context = context; 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ScheduleItem getItem(int position) {
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
		convertView = inflater.inflate(R.layout.schedule_list_item, null);
		
		TextView busNameTextView = (TextView) convertView.findViewById(R.id.scheduleitem_busname); 
		TextView busDirectionTextView = (TextView) convertView.findViewById(R.id.scheduleitem_busdirection); 
		TextView busArrivalTimeTextView = (TextView) convertView.findViewById(R.id.scheduleitem_busarrivaltime);
		
		busNameTextView.setText(scheduleItemList.get(position).getBus().getName());
		busDirectionTextView.setText(scheduleItemList.get(position).getBus().getDirection()); 
		busArrivalTimeTextView.setText(scheduleItemList.get(position).getTime().format2445());
		
		return convertView;
	}

}
