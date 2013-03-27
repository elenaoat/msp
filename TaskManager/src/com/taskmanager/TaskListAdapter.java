package com.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskListAdapter extends ArrayAdapter<Task>{

	Context context;
	int layoutResId;
	Task data[] = null;
	
	public TaskListAdapter(Context context, int layoutResId, Task[] data){
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
			holder.text = (TextView)row.findViewById(R.id.text);
			holder.task = (TextView)row.findViewById(R.id.task); 
			
			row.setTag(holder);
		} else {
			holder = (TaskHolder)row.getTag();
		}
		
		Task t = data[position];
		holder.task.setText(t.task);
		holder.text.setText(t.time);
		
		return row;
	}

	
	static class TaskHolder
	{
		TextView text;
		TextView task;
	}
}
