package com.taskmanager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
//import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	DatabaseAdapter dbAdapter;
	EditText etTitle, etBody;
	private List<Task> t_array;
	private ListView listView;
	private TaskListAdapter adapter;
	private TextView currentDate;

	public void show(){
		
		dbAdapter.Open();
		Cursor c = dbAdapter.getAllConfig();
		
		String message="";
		
		if(c.moveToFirst())
	    {
	        do
	        {
	            //Call displayItem method in below
	           // System.out.println("Property =" + c.getString(0)+ "\nValuetype =" + c.getString(1)+ "\nintegerValue =" + c.getString(3)); 
	        	message = "Property: " + c.getString(0) + "\n" + 
	      	          "ValueType: " + c.getString(1) + "\n" +
	                    "Textvalue : " + c.getString(2) + "\n" +
	                    "IntegerValue : " + c.getString(3);
	      		displayToast(message);
	        } while (c.moveToNext());
	      
	    }
	    else
	    {
	    	displayToast("No item found");
	    }
		
		
		
		dbAdapter.Close();
	}
	// end of initialization
	public void initializeConfig(){

		dbAdapter.Open();
		dbAdapter.initializeGConfig();
		dbAdapter.Close();
		
	} 


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

		String currentDateTimeString = DateFormat.getDateInstance(
				DateFormat.LONG).format(new Date());
		if (dateString == null) {
			currentDate.setText(currentDateTimeString);
		} else {
			currentDate.setText(dateString);
		}
	}

	public void showTasks() {
		t_array = new ArrayList<Task>();

		dbAdapter.Open();
		Cursor curs = dbAdapter.getAllEventsForDayView();

		if (curs.moveToFirst()) {
			do {

				// should the substring parameters be hard-coded?
				String time = curs.getString(curs.getColumnIndex("datetime"))
						.substring(0, 2);
				String title = curs.getString(curs.getColumnIndex("title"));

				//workaround for displaying events that have same date and time
				for (Task t : t_array){
					if (time.equals(t.getTime())){
						time = "    ";
					}
				}
				// SHould be changed to (12, 13) in case the date field will be
				// represented as dd-mm-yyyy hh:mm:ss
				// if ( (Collections.frequency(t_array, )) )
				t_array.add(new Task(time, title));

			} while (curs.moveToNext());
		}
		

		dbAdapter.Close();  
		
		*/

		adapter = new TaskListAdapter(this, R.layout.custom_simple, t_array);

		listView = (ListView) findViewById(R.id.hour_slots);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Toast.makeText(getApplicationContext(),
				// t_array[position].time, Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	public void onClick(View v) {

		String message = "";
		// action of adding a task
		if (v.getId() == R.id.add_event) {

			Intent intent = new Intent(this, NewTaskActivity.class);
			startActivity(intent);
			adapter.notifyDataSetChanged();

		} else if (v.getId() == R.id.show_btn) {
			dbAdapter.Open();

			Cursor c = dbAdapter.getAllEvents();

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
		String message = "id: " + c.getString(0) + "\n" + "title: "
				+ c.getString(1) + "\n" + "description: " + c.getString(2);
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
