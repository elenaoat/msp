package com.taskmanager;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
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
		String currentDateTimeString = DateFormat.getDateInstance().format(
				new Date());
		currentDate.setText(currentDateTimeString);

		
		
		//initial hour slots without tasks
		Task[] t_array = new Task[] {
				new Task("01", ""), new Task("02", ""), new Task("03", ""),
				new Task("04", ""), new Task("05", ""), new Task("06", ""),
				new Task("07", ""), new Task("08", ""), new Task("09", ""),
				new Task("10", ""), new Task("11", ""), new Task("12", ""),
				new Task("13", ""), new Task("14", ""), new Task("15", ""),
				new Task("16", ""), new Task("17", ""), new Task("18", ""),
				new Task("19", ""), new Task("20", ""), new Task("21", ""),
				new Task("22", ""), new Task("23", ""), new Task("00", "")
		};
		
		dbAdapter.Open();
		Cursor curs = dbAdapter.getAllEventsForDayView();

		//List<String> arrayDB = new ArrayList<String>();
		if (curs.moveToFirst()) {
			do {

				String time = curs.getString(curs.getColumnIndex("datetime")).substring(0, 2);
				String title = curs.getString(curs.getColumnIndex("title"));

				// SHould be changed to (12, 13) in case the date field will be
				// represented as dd-mm-yyyy hh:mm:ss
			//	arrayDB.add(time.substring(0, 2) + "  " + title);
				
				for(int i=0; i<24; i++){
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
			// custom dialog

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
					DisplayItem(c);
				} while (c.moveToNext());
			} else {
				DisplayToast("No item found");
			}

			dbAdapter.Close();
		} else if (v.getId() == R.id.day_btn) {
			message = "You are in the day view";
			DisplayToast(message);
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

	public void DisplayItem(Cursor c) {
		String message = "id: " + c.getString(0) + "\n" + "title: "
				+ c.getString(1) + "\n" + "description: " + c.getString(2);
		DisplayToast(message);

	}

	public void DisplayToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}
}
