package com.taskmanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends Activity implements OnClickListener {

	DatabaseAdapter dbAdapter;
	EditText etTitle, etBody;
	private List<Slot> hours;
	private List<Task> t_array;
	private ListView listView;
	private TaskListAdapter adapter;
	private TextView currentDate;
	private String currentDateTimeString;
	public final static String DATE = "com.taskmanager.DATE";
	public final static String TIME = "com.taskmanager.TIME";
	public final static String NAME = "com.taskmanager.NAME";
	public final static String DESCRIPTION = "com.taskmanager.DESCRIPTION";
	//TODO add other fields which are missing, i.e. reccurency 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbAdapter = new DatabaseAdapter(getApplicationContext());

		// get the instance of all buttons
		currentDate = (TextView) findViewById(R.id.dvu_header);

		Button dayBtn = (Button) findViewById(R.id.day_btn);
		Button weekBtn = (Button) findViewById(R.id.week_btn);
		Button monthBtn = (Button) findViewById(R.id.month_btn);

		ImageButton addBtn = (ImageButton) findViewById(R.id.add_event);
		ImageButton showBtn = (ImageButton) findViewById(R.id.show_btn);

		// Current date for display in the day view
		showTodaysDate();
		showTasks();

		// adding action listener of buttons
		dayBtn.setOnClickListener(this);
		weekBtn.setOnClickListener(this);
		monthBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		showBtn.setOnClickListener(this);
	}

	public void showTodaysDate() {
		// Current date for display in the day view
		Intent in = getIntent();
		String dateString = in.getStringExtra("currentDay");

		currentDateTimeString = DateFormat.getDateInstance(DateFormat.LONG)
				.format(new Date());
		if (dateString == null) {
			currentDate.setText(currentDateTimeString);

		} else {
			currentDate.setText(dateString);

		}

	}

	public void showTasks() {

		t_array = new ArrayList<Task>();

		dbAdapter.Open();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Cursor curs = dbAdapter.getEventByDate(dateFormat.format(c.getTime()));

		if (curs.moveToFirst()) {
			do {

				// should the substring parameters be hard-coded?
				// name,description,eventStartDayTime,eventEndDayTime
				int id = curs.getInt(curs.getColumnIndex("id"));
				String name = curs.getString(curs.getColumnIndex("name"));
				String description = curs.getString(curs
						.getColumnIndex("description"));
				String eventStartDayTime = curs.getString(curs
						.getColumnIndex("eventStartDayTime"));
				String eventEndDayTime = curs.getString(curs
						.getColumnIndex("eventEndDayTime"));

				t_array.add(new Task(id, name, description, eventStartDayTime,
						eventEndDayTime));

			} while (curs.moveToNext());
		}

		dbAdapter.Close();

		hours = new ArrayList<Slot>();
		for (int i = 0; i < 24; i++) {
			Slot slot;
		//	StringBuilder builder = new StringBuilder();
			if (i <= 9) {
		/*		builder.append("0");
				builder.append(i);
				builder.append(":00");*/
				slot = new Slot(-1, Integer.toString(i), "");
			} else {
			/*	builder.append(i);
				builder.append(":00");*/
				slot = new Slot(-1, Integer.toString(i), "");
			}
			hours.add(slot);
		}
		
		//check if there are any events in the database
		if (t_array.size()!=0)
		for (int i=0; i<24; i++){
			try{
				if ((t_array.get(i).eventStartDayTime.substring(12, 13)).equals(hours.get(i).time)){
					hours.get(i).name = t_array.get(i).name;
					hours.get(i).id = t_array.get(i).id;
				}
			} catch(ArrayIndexOutOfBoundsException e){
				Log.v("MainActivity", "Wrong format for datetime");
			}
		
 
		}
		adapter = new TaskListAdapter(this, R.layout.custom_simple, hours);

		listView = (ListView) findViewById(R.id.hour_slots);
		listView.setAdapter(adapter);

		
		listView.setOnItemClickListener(new OnItemClickListener() {
		  
		  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  if (hours.get(position).id == -1){
			  
				Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String date = dateFormat.format(Calendar.getInstance().getTime());
				String time = hours.get(position).time;
				intent.putExtra(DATE, date);
				intent.putExtra(TIME, time);				
				startActivity(intent);
				
		  }
		  	  
		  //Toast.makeText(getApplicationContext(), hours.get(position).name, Toast.LENGTH_SHORT).show(); 
		 } 
		  
		 });
		 

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

		dbAdapter.Close();
	}

	// end of initialization
	public void initializeConfig() {

		dbAdapter.Open();
		dbAdapter.initializeGConfig();
		dbAdapter.Close();

	}

	@Override
	public void onClick(View v) {

		String message = "";
		// action of adding a task
		if (v.getId() == R.id.add_event) {

			Intent intent = new Intent(this, NewTaskActivity.class);
			startActivity(intent);
			// adapter.notifyDataSetChanged();

		} else if (v.getId() == R.id.show_btn) {
			dbAdapter.Open();

			/*
			 * please don't clean this up
			 * 
			 * 
			 * dbAdapter.createEvent("task1", "create event",
			 * "2013-04-09 12:00","2013-04-09 13:00",
			 * "","15","Bar","daily","2013-04-17 12:00");
			 * dbAdapter.createEvent("task2", "create event",
			 * "2013-05-09 17:00","2013-05-09 18:00",
			 * "","15","Bar","weekly","2013-06-17 12:00");
			 * 
			 * dbAdapter.createEvent("task1", "create event",
			 * "2014-04-09 12:00","2014-04-09 13:00",
			 * "","15","Bar","monthly","2015-04-07 12:00");
			 */
			Cursor c = dbAdapter.getEventByDate("2013-04-09");

			if (c.moveToFirst()) {
				do {
					// Call displayItem method in below
					displayItem(c);
				} while (c.moveToNext());
			} else {
				displayToast("No item found");
			}

			dbAdapter.Close();
		} else if (v.getId() == R.id.day_btn) {
			message = "You are in the day view";
			displayToast(message);
		} else if (v.getId() == R.id.week_btn) {
			Intent weekIntent = new Intent(getBaseContext(),
					WeekViewActivity.class);
			// message = "You are entered in the Week View";
			// weekIntent.putExtra("message", message);
			startActivity(weekIntent);
		} else if (v.getId() == R.id.month_btn) {
			Intent monthIntent = new Intent(getBaseContext(),
					MonthViewActivity.class);
			startActivity(monthIntent);
		}
	}

	public void displayItem(Cursor c) {
		String message = "Id: " + c.getString(0) + "\n" + "Name: "
				+ c.getString(1) + "\n" + "Description: " + c.getString(2)
				+ "\n" + "EventStartTime: " + c.getString(3) + "\n"
				+ "EventEndTime: " + c.getString(4);
		displayToast(message);

	}

	public void displayToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}
}

// TODO LIST
/*
 * BUG when going to a day from month view and then selecting day view, it says
 * you are already in the day view There should be some home view and day view
 * separately Update data in the view once there is new task inserted
 */
