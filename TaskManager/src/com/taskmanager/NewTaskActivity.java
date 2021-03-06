/*AUTHOR: Elena Oat
 * This activity is started whenever a user taps on an empty line in the ListView on the main screen.
 * The time and date are sent in the intent when this activity is started. 
 * */


package com.taskmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewTaskActivity extends Activity {
	private DatabaseAdapter dbAdapter;
	private EditText etTitle, etBody;
	private Button fromTimeBtn, toTimeBtn, fromDateBtn, toDateBtn;
	private String date_sent;
	private int time_sent;
	public CustomDate date_DT_from, date_DT_to, date_save_from, date_save_to;
	public CustomTime time_DT_from, time_DT_to, time_save_from, time_save_to;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtask_activity);

		fromTimeBtn = (Button) findViewById(R.id.from_time_picker);
		toTimeBtn = (Button) findViewById(R.id.to_time_picker);
		fromDateBtn = (Button) findViewById(R.id.from_date_picker);
		toDateBtn = (Button) findViewById(R.id.to_date_picker);

		etTitle = (EditText) findViewById(R.id.etTitle);
		etBody = (EditText) findViewById(R.id.etNote);

		Intent intent = getIntent();
		// check if the extra DATE was sent with the intent
		if (intent.hasExtra("com.taskmanager.DATE")) {
			date_sent = intent.getStringExtra("com.taskmanager.DATE");
			// Log.v("date in new task activ", date_sent);
		}
		// check if the extra TIME was sent with the intent
		if (intent.hasExtra("com.taskmanager.HOUR")) {
			time_sent = intent.getIntExtra("com.taskmanager.HOUR", 0);
			// Log.v("time in new task activ",
			// Integer.toString(intent.getIntExtra("com.taskmanager.HOUR", 0)));
		}

		// times and dates for "From" and "To" are equal
		time_DT_from = new CustomTime(time_sent, 0);
		date_DT_from = new CustomDate(date_sent);

		time_DT_to = new CustomTime(time_sent, 0);
		date_DT_to = new CustomDate(date_sent);

		setReceivedTime();
		setReceivedDate();

		dbAdapter = new DatabaseAdapter(getApplicationContext());
	}

	// Button click -> display TimePickerDialog
	public void chooseTimeFrom(View v) {

		time_save_from = new CustomTime(0, 0);
		new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
				time_save_from), time_DT_from.getHour(),
				time_DT_from.getMinute(), true).show();

	}

	public void chooseTimeTo(View v) {
		time_save_to = new CustomTime(0, 0);
		new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
				time_save_to), time_DT_to.getHour(), time_DT_to.getMinute(),
				true).show();

	}

	public void chooseDateTo(View v) {

		date_save_to = new CustomDate(0, 0, 0);
		CustomOnDateSetListener customDateListener = new CustomOnDateSetListener(
				(Button) v, date_save_to);
		new DatePickerDialog(this, customDateListener, date_DT_to.getYear(),
				date_DT_to.getMonth(), date_DT_to.getDay()).show();
	}

	public void chooseDateFrom(View v) {
		date_save_from = new CustomDate(0, 0, 0);
		CustomOnDateSetListener customDateListener = new CustomOnDateSetListener(
				(Button) v, date_save_from);
		new DatePickerDialog(this, customDateListener, date_DT_from.getYear(),
				date_DT_from.getMonth(), date_DT_from.getDay()).show();
	}

	public void setReceivedTime() {
		fromTimeBtn.setText(time_DT_from.getTimeStr());
		toTimeBtn.setText(time_DT_to.getTimeStr());
	}

	public void setReceivedDate() {

		fromDateBtn.setText(date_DT_from.getDate());
		toDateBtn.setText(date_DT_to.getDate());
	}

	public void roundMinute(int hourOfDay, int min, CustomTime f) {
		f.setHour(hourOfDay);
		if (min <= 7) {
			f.setMinute(0);
		} else if (min > 7 && min <= 22) {
			f.setMinute(15);
		} else if (min > 22 && min <= 37) {
			f.setMinute(30);
		} else if (min > 37 && min <= 52) {
			f.setMinute(45);
		} else {
			f.setMinute(0);
			f.setHour(hourOfDay + 1);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
		return true;
	}

	public void save(View view) {

		if (date_save_from == null) {
			date_save_from = date_DT_from;
		}
		if (date_save_to == null) {
			date_save_to = date_DT_to;
		}

		if (time_save_from == null) {
			time_save_from = time_DT_from;
		}

		if (time_save_to == null) {
			time_save_to = time_DT_to;
		}

		// Validating input inserted by user: hourFrom < hourTo, Title not empty
		if ((time_save_from.getHour() > time_save_to.getHour()
				&& date_save_from.getYear() >= date_save_to.getYear()
				&& date_save_from.getMonth() >= date_save_to.getMonth() && date_save_from
				.getDay() >= date_save_to.getDay())
				|| (time_save_from.getHour() == time_save_to.getHour() && time_save_from
						.getMinute() >= time_save_to.getMinute())
				&& date_save_from.getYear() >= date_save_to.getYear()
				&& date_save_from.getMonth() >= date_save_to.getMonth()
				&& date_save_from.getDay() >= date_save_to.getDay()) {

			HelperMethods.displayToast("You have inserted incorrect times", this);
			// return to MainActivity ???

		} else if (etTitle.getText().toString().equals("")) {
			HelperMethods.displayToast("Please insert task title", this);
			// return to MainActivity ???
		}
		// input OK
		else {
			insertIntoDB();
		}

	}

	/*Insert data chosen by user into database*/
	public void insertIntoDB() {
		dbAdapter.Open();

		Log.v("if its not null", date_save_from.getDate());
		if (date_save_from == null) {
			Log.v("date is null too", "");
		}

		Log.v("date for DB", date_save_from.getDateForDB());
		Log.v("time for DB", time_save_from.getTimeStr());
		/* workaround in case the date/time pickers werent selected at all */

		
		long inserted = dbAdapter.createEvent(
				etTitle.getText().toString(),
				etBody.getText().toString(),
				date_save_from.getDateForDB() + " "
						+ time_save_from.getTimeStr(),
				date_save_to.getDateForDB() + " " + time_save_to.getTimeStr(), "", "", "", "", "");

		if (inserted > 0) {
			HelperMethods.displayToast("Successfully Saved", this);
			etTitle.setText("");
			etBody.setText("");
		} else {
			HelperMethods.displayToast("Insertion failed", this);
		}

		dbAdapter.Close();

		if (date_save_from != null) {
			Log.v("get date", date_save_from.getDate());
		}
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);

	}

	public void cancel(View view) {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}




}
