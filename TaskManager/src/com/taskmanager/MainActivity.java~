package com.taskmanager;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class MainActivity extends Activity implements OnClickListener {

	DatabaseAdapter dbAdapter;
	EditText etTitle, etBody, etTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbAdapter = new DatabaseAdapter(getApplicationContext());

		// get the instance of all buttons
		TextView currentDate = (TextView) findViewById(R.id.dvu_header);

		Button dayBtn = (Button) findViewById(R.id.day_btn);
		Button weekBtn = (Button) findViewById(R.id.week_btn);
		Button monthBtn = (Button) findViewById(R.id.month_btn);

		ImageButton addBtn = (ImageButton) findViewById(R.id.add_event);
		ImageButton showBtn = (ImageButton) findViewById(R.id.show_btn);

		// Current date for display in the day view
		Intent in = getIntent();
		String dateString = in.getStringExtra("currentDay");
		
		String currentDateTimeString = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
		if(dateString == null)
		{
			currentDate.setText(currentDateTimeString);
		}
		else
		{
			currentDate.setText(dateString);
		}
				
		
		//initial hour slots without tasks
		final Task[] t_array = new Task[] {
				new Task("00:00", ""), new Task("00:15", ""), new Task("00:30", ""), new Task("00:45", ""),
				new Task("01:00", ""), new Task("01:15", ""), new Task("01:30", ""), new Task("01:45", ""),
				new Task("02:00", ""), new Task("02:15", ""), new Task("02:30", ""), new Task("02:45", ""),
				new Task("03:00", ""), new Task("03:15", ""), new Task("03:30", ""), new Task("03:45", ""),
				new Task("04:00", ""), new Task("04:15", ""), new Task("04:30", ""), new Task("04:45", ""),
				new Task("05:00", ""), new Task("05:15", ""), new Task("05:30", ""), new Task("05:45", ""),
				new Task("06:00", ""), new Task("06:15", ""), new Task("06:30", ""), new Task("06:45", ""),
				new Task("07:00", ""), new Task("07:15", ""), new Task("07:30", ""), new Task("07:45", ""),
				new Task("08:00", ""), new Task("08:15", ""), new Task("08:30", ""), new Task("08:45", ""),
				new Task("09:00", ""), new Task("09:15", ""), new Task("09:30", ""), new Task("09:45", ""),
				new Task("10:00", ""), new Task("10:15", ""), new Task("10:30", ""), new Task("10:45", ""),
				new Task("11:00", ""), new Task("11:15", ""), new Task("11:30", ""), new Task("11:45", ""),
				new Task("12:00", ""), new Task("12:15", ""), new Task("12:30", ""), new Task("12:45", ""),
				new Task("13:00", ""), new Task("13:15", ""), new Task("13:30", ""), new Task("13:45", ""),
				new Task("14:00", ""), new Task("14:15", ""), new Task("14:30", ""), new Task("14:45", ""),
				new Task("15:00", ""), new Task("15:15", ""), new Task("15:30", ""), new Task("15:45", ""),
				new Task("16:00", ""), new Task("16:15", ""), new Task("16:30", ""), new Task("16:45", ""),
				new Task("17:00", ""), new Task("17:15", ""), new Task("17:30", ""), new Task("17:45", ""),
				new Task("18:00", ""), new Task("18:15", ""), new Task("18:30", ""), new Task("18:45", ""),
				new Task("19:00", ""), new Task("19:15", ""), new Task("19:30", ""), new Task("19:45", ""),
				new Task("20:00", ""), new Task("20:15", ""), new Task("20:30", ""), new Task("20:45", ""),
				new Task("21:00", ""), new Task("21:15", ""), new Task("21:30", ""), new Task("21:45", ""),
				new Task("22:00", ""), new Task("22:15", ""), new Task("22:30", ""), new Task("22:45", ""),
				new Task("23:00", ""), new Task("23:15", ""), new Task("23:30", ""), new Task("23:45", ""),
				
		};
		
		dbAdapter.Open();
		Cursor curs = dbAdapter.getAllEventsForDayView();

		//List<String> arrayDB = new ArrayList<String>();
		if (curs.moveToFirst()) {
			do {

				String time = curs.getString(curs.getColumnIndex("datetime")).substring(0, 4);
				String title = curs.getString(curs.getColumnIndex("title"));

				// SHould be changed to (12, 13) in case the date field will be
				// represented as dd-mm-yyyy hh:mm:ss
			//	arrayDB.add(time.substring(0, 2) + "  " + title);
				
				for(int i=0; i<96; i++){
					if (t_array[i].time.equals(time)){
						t_array[i].task = title;
					}
				}
			} while (curs.moveToNext());
		}

		dbAdapter.Close();

		TaskListAdapter adapter = new TaskListAdapter(this,
				R.layout.custom_simple, t_array);

		ListView listView = (ListView) findViewById(R.id.hour_slots);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				//Toast.makeText(getApplicationContext(), t_array[position].time, Toast.LENGTH_SHORT).show();
			}			
		});



		// adding action listener of buttons
		dayBtn.setOnClickListener(this);
		weekBtn.setOnClickListener(this);
		monthBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		showBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		String message = "";
		// action of adding a task
		if (v.getId() == R.id.add_event) {
			Intent intent = new Intent(this, NewTaskActivity.class);
			
			startActivity(intent);
			/*			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.event_dialog);
			dialog.setTitle("Create a new Task");

			// set the custom dialog components - editText and button
			etTime = (EditText) dialog.findViewById(R.id.etTime);
			etTitle = (EditText) dialog.findViewById(R.id.etTitle);
			etBody = (EditText) dialog.findViewById(R.id.etNote);

			Button dialogSaveButton = (Button) dialog
					.findViewById(R.id.save_btn);
			Button dialogCancelButton = (Button) dialog
					.findViewById(R.id.cancel_btn);

			// if button is clicked, save the tasks
			dialogSaveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String title = etTitle.getText().toString();
					String body = etBody.getText().toString();
					String time = etTime.getText().toString();
					dbAdapter.Open();

					long inserted = dbAdapter.InsertNote(time, title, body);

					if (inserted > 0) {
						DisplayToast("Successfully Saved " + inserted);
						// Toast.makeText(getApplicationContext(),
						// "Successfully Saved "+inserted,
						// Toast.LENGTH_SHORT).show();
						etTitle.setText("");
						etBody.setText("");
						dialog.dismiss();
					} else {
						DisplayToast("Insertion failed");
						// Toast.makeText(getApplicationContext(),
						// "Insertion failed", Toast.LENGTH_SHORT).show();
					}

					dbAdapter.Close();
				}
			});

			// if button is clicked, close the custom dialog
			dialogCancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
*/
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
			Intent monthIntent = new Intent(getBaseContext(), MonthViewActivity.class);
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


//TODO LIST
/* 
 * BUG when going to a day from month view and then selecting day view, it says you are already in the day view
There should be some home view and day view separately
* Update data in the view once there is new task inserted

*/
