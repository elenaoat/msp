package com.mesba.taskschedular;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mesba.dynamicui.R;

@SuppressLint("NewApi")
public class TaskListViewFragment extends Fragment {
	private View taskListView;
	private DatabaseAdapter dbAdapter;
	private TextView eventList;
	private String[] dateTimeSplitStart;
	private String[] dateTimeSplitEnd;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		taskListView = inflater.inflate(R.layout.layout_tasks_list, container,
				false);

		dbAdapter = new DatabaseAdapter(getActivity());
		
		eventList = (TextView) taskListView.findViewById(R.id.all_tasks);
		
		getAllEvents();
		
		return taskListView;
	}
	
	public void getAllEvents()
	{
		String name, description, start, end = "";
		dbAdapter.Open();
		
		Cursor c = dbAdapter.getAllEvents();
		
		if (c.moveToFirst()) {
			do {
//				c.getString(c.getColumnIndex("id"));
				name = c.getString(c.getColumnIndex("name"));
				description = c.getString(c.getColumnIndex("description"));
				start = c.getString(c.getColumnIndex("eventStartDayTime"));
				end = c.getString(c.getColumnIndex("eventEndDayTime"));
				
				dateTimeSplitStart = start.split(" ");
				dateTimeSplitEnd = end.split(" ");
				eventList.append("Task: \n"+"------\n"+ name + "-" + description + "\nDate: " + dateTimeSplitStart[0]
						+ "\nTime:  " + dateTimeSplitStart[1]+ "-"+ dateTimeSplitEnd[1]+ "\n");
			} while (c.moveToNext());

		}
			
		c.close();
		dbAdapter.Close();
	}
}
