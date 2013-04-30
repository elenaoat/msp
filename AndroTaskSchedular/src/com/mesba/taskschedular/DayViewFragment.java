package com.mesba.taskschedular;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mesba.dynamicui.R;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class DayViewFragment extends Fragment {
	private View dayView;

	private DatabaseAdapter dbAdapter;
	private TextView currentDate;

	private String currentDateTimeString;
	private String currentDate_YYYY_mm_dd;

	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public Calendar today = Calendar.getInstance();

	private List<Slot> hours;
	private List<Task> t_array;
	private ListView listView;
	private TaskListAdapter adapter;

	public final static String ID = "com.taskmanager.ID";
	public final static String DATE = "com.taskmanager.DATE";
	public final static String HOUR = "com.taskmanager.HOUR";
	public final static String MINUTE = "com.taskmanager.MINUTE";
	public final static String NAME = "com.taskmanager.NAME";
	public final static String DESCRIPTION = "com.taskmanager.DESCRIPTION";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dayView = inflater.inflate(R.layout.layout_day_view, container, false);

		dbAdapter = new DatabaseAdapter(getActivity());

		currentDate = (TextView) dayView.findViewById(R.id.dvu_header);
		showCurrentDate();

		dbAdapter.Open();
		showTasks();
		dbAdapter.Close();

		return dayView;
	}

	/**
	 * This function is responsible for setting the current date in the day view
	 * header
	 */
	public void showCurrentDate() {
		currentDateTimeString = DateFormat.getDateInstance(DateFormat.LONG)
				.format(new Date());
		currentDate_YYYY_mm_dd = new String(dateFormat.format(today.getTime()));

		currentDate.setText(currentDateTimeString);
	}

	public void showTasks() {
		t_array = new ArrayList<Task>();
		Cursor curs = dbAdapter.getEventByDate(dateFormat.format(today
				.getTime()));

		if (curs.moveToFirst()) {
			do {

				// should the substring parameters be hard-coded?
				// name,description,eventStartDayTime,eventEndDayTime
				int id = curs.getInt(curs.getColumnIndex("id"));
				// Log.v("id ", Integer.toString(id));
				String name = curs.getString(curs.getColumnIndex("name"));
				String description = curs.getString(curs
						.getColumnIndex("description"));
				// Log.v("description", description);
				String eventStartDayTime = curs.getString(curs
						.getColumnIndex("eventStartDayTime"));
				// Log.v("event start", eventStartDayTime);
				String eventEndDayTime = curs.getString(curs
						.getColumnIndex("eventEndDayTime"));
				// Log.v("event end", eventEndDayTime);
				t_array.add(new Task(id, name, description, eventStartDayTime,
						eventEndDayTime));

			} while (curs.moveToNext());
		}

		hours = new ArrayList<Slot>();
		for (int i = 0; i < 24; i++) {
			Slot slot;
			slot = new Slot(-1, i, "");
			hours.add(slot);
		}
		// 2013-04-15 06:00
		// check if there are any events in the database
		int size_events = t_array.size();
		for (int i = 0; i < size_events; i++) {
			// Log.v("hour in events",
			// t_array.get(i).eventStartDayTime.substring(11, 13));

			for (int j = 0; j < 24; j++) {
				try {
					// Log.v("hour in slot ", hours.get(j).hourStr());
					if ((t_array.get(i).eventStartDayTime.substring(11, 13))
							.equals(hours.get(j).hourStr())) {
						hours.get(j).setName(t_array.get(i).name + "-" + t_array.get(i).description);
						hours.get(j).setId(t_array.get(i).id);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.v("MainActivity", "Wrong format for datetime");
				}
			}

		}
		adapter = new TaskListAdapter(getActivity(), R.layout.custom_simple,
				hours);

		listView = (ListView) dayView.findViewById(R.id.hour_slots);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (hours.get(position).id == -1) {

					Intent intent = new Intent(getActivity(),
							AddNewTaskActivity.class);
					String date = dateFormat.format(today.getTime());
					int time = hours.get(position).time;
					intent.putExtra(DATE, date);
					intent.putExtra(HOUR, time);
					intent.putExtra("tag", "add");
					startActivity(intent);

				} else if (hours.get(position).getId() != -1) {
					// Send only ID, all the others will be extracted from the
					// DB
					// TODO need to fix this part
					Intent intent = new Intent(getActivity(),
							AddNewTaskActivity.class);
					intent.putExtra("tag", "edit");
					intent.putExtra(ID, hours.get(position).getId());
					startActivity(intent);
				}

			}

		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				Log.v("long clicked", "pos" + " " + pos);

				return true;
			}
		});

		curs.close();
	}

	public void show() {

		dbAdapter.Open();
		Cursor c = dbAdapter.getAllConfig();

		String message = "";

		if (c.moveToFirst()) {
			do {
				// Call displayItem method in below
				// System.out.println("Property =" + c.getString(0)+
				// "\nValuetype =" + c.getString(1)+ "\nintegerValue =" +
				// c.getString(3));
				message = "Property: " + c.getString(0) + "\n" + "ValueType: "
						+ c.getString(1) + "\n" + "Textvalue : "
						+ c.getString(2) + "\n" + "IntegerValue : "
						+ c.getString(3);
				displayToast(message);
			} while (c.moveToNext());

		} else {
			displayToast("No item found");
		}
		c.close();
		dbAdapter.Close();
	}

	public void displayToast(String message) {
		Toast toast = Toast
				.makeText(getActivity(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}

}

// TODO LIST
/*
 * BUG when going to a day from month view and then selecting day view, it says
 * you are already in the day view There should be some home view and day view
 * separately Update data in the view once there is new task inserted Sent
 * minute with hour at the same time
 */