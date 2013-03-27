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
		
		if (row == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResId, parent, false);
			
		}
	}
	
	
	static class TaskHolder
	{
		TextView text1;
		TextView task1, task2, task3, task4;
	}
}
