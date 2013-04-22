/*AUTHOR: Elena Oat
 * Class that populates the ListView with events that are passed to it
 * inside the ArrayAdapter, whose elements are of class Slot.
 * 
 * */


package com.taskmanager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskListAdapter extends ArrayAdapter<Slot>{

	Context context;
	int layoutResId;
	List <Slot> data = null;
	
	public TaskListAdapter(Context context, int layoutResId, List <Slot> data){
		super(context, layoutResId, data);
		this.layoutResId = layoutResId;
		this.context = context;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		TaskHolder holder = null;
		
		if (row == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResId, parent, false);
			
			holder = new TaskHolder();
			holder.time = (TextView)row.findViewById(R.id.time);
			holder.name = (TextView)row.findViewById(R.id.name); 

			row.setTag(holder);
		} else {
			holder = (TaskHolder)row.getTag();
		}
		
		Slot t = data.get(position);
		//holder.time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
		//need to put substr of this to show only the time, no date
		//pad hour
		
/*		StringBuffer hour = new StringBuffer();
		hour.append(t.time.subSequence(11, 16));
		hour.append(" - ");
		
		hour.append(t.time.subSequence(27, 32));*/
		//hour.append(t.eventTime.subSequence(29, 33));
		StringBuilder strBuilder = new StringBuilder();
		if (data.get(position).getTime() <= 9){
			strBuilder.append("0");
		}
		strBuilder.append(t.time);
		strBuilder.append(":00");
		
		holder.time.setText(strBuilder);
		//holder.name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
		holder.name.setText(t.name);

		return row;
	}

	
	static class TaskHolder
	{
		TextView time;
		TextView name;

	}
}
