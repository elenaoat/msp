package com.mesba.taskschedular;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mesba.dynamicui.R;

public class AddNewTaskActivity extends Activity {
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private DatabaseAdapter dbAdapter;
	private EditText etTitle, etBody;
	private Button fromTimeBtn, toTimeBtn, fromDateBtn, toDateBtn, recDateBtn;
	private String date_sent;
	private int time_sent, id;
	public CustomDate date_DT_from, date_DT_to, date_DT_rec, date_save_from,
			date_save_to, date_save_rec;
	public CustomTime time_DT_from, time_DT_to, time_save_from, time_save_to;

	private String tag, name, description;

	private Spinner spinner;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_new_task);

		dbAdapter = new DatabaseAdapter(getApplicationContext());

		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		fromTimeBtn = (Button) findViewById(R.id.from_time_picker);
		toTimeBtn = (Button) findViewById(R.id.to_time_picker);
		fromDateBtn = (Button) findViewById(R.id.from_date_picker);
		toDateBtn = (Button) findViewById(R.id.to_date_picker);
		recDateBtn = (Button) findViewById(R.id.rec_date_picker);

		recDateBtn.setText("Click if not 'none'");

		etTitle = (EditText) findViewById(R.id.etTitle);
		etBody = (EditText) findViewById(R.id.etNote);

		Intent intent = getIntent();
		tag = intent.getStringExtra("tag");

		if (tag.equals("add")) {
			if (intent.hasExtra("com.taskmanager.DATE")) {
				date_sent = intent.getStringExtra("com.taskmanager.DATE");
			}
			if (intent.hasExtra("com.taskmanager.HOUR")) {
				time_sent = intent.getIntExtra("com.taskmanager.HOUR", 0);
			}
			// times and dates for "From" and "To" are equal
			time_DT_from = new CustomTime(time_sent, 0);
			date_DT_from = new CustomDate(date_sent);

			time_DT_to = new CustomTime(time_sent + 1, 0);
			// time_DT_to = HelperMethods.addHour(time_DT_from);

			date_DT_rec = new CustomDate(date_sent);

			date_DT_to = new CustomDate(date_sent);
		} else if (intent.getStringExtra("tag").equals("edit")) {
			if (intent.hasExtra("com.taskmanager.ID")) {
				id = intent.getIntExtra("com.taskmanager.ID", 0);
				getDataFromDatabase(id);
				etTitle.setText(name);
				etBody.setText(description);
				// Log.v("ID obtained from another activity",
				// Integer.toString(id));
			} else {
				Log.v("Error", "obtaining ID");
			}
		}

		setReceivedTime();
		setReceivedDate();
		addListenerOnSpinnerItemSelection();
	}

	private void addListenerOnSpinnerItemSelection() {

		spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  setContentView(R.layout.layout_new_task);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_new_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.item_save:
			save();
			break;
		case R.id.item_cancel:
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}


	// Button click -> display TimePickerDialog
	public void chooseTimeFrom(View v) {
		if (time_save_from != null) {
			time_DT_from = time_save_from;
		} else {
			time_save_from = new CustomTime(0, 0);
		}
		new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
				time_save_from), time_DT_from.getHour(),
				time_DT_from.getMinute(), true).show();

	}

	public void chooseTimeTo(View v) {
		if (time_save_to != null) {
			time_DT_to = time_save_to;
		} else {
			time_save_to = new CustomTime(0, 0);
		}
		new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
				time_save_to), time_DT_to.getHour(), time_DT_to.getMinute(),
				true).show();

	}

	public void chooseDateTo(View v) {
		if (date_save_to != null) {
			date_DT_to = date_save_to;
			// Log.v("date DT to", date_DT_to.getDate());
		} else {
			date_save_to = new CustomDate(0, 0, 0);
			// Log.v("date DT to", date_save_to.getDate());

		}

		new DatePickerDialog(this, new CustomOnDateSetListener((Button) v,
				date_save_to), date_DT_to.getYear(), date_DT_to.getMonth(),
				date_DT_to.getDay()).show();
	}

	public void chooseDateRec(View v) {

		date_save_rec = new CustomDate(0, 0, 0);
		CustomOnDateSetListener customDateListener = new CustomOnDateSetListener(
				(Button) v, date_save_rec);
		new DatePickerDialog(this, customDateListener, date_DT_rec.getYear(),
				date_DT_rec.getMonth(), date_DT_rec.getDay()).show();
	}

	public void chooseDateFrom(View v) {
		if (date_save_from != null) {
			date_DT_from = date_save_from;
		} else {
			date_save_from = new CustomDate(0, 0, 0);
		}
		new DatePickerDialog(this, new CustomOnDateSetListener((Button) v,
				date_save_from), date_DT_from.getYear(),
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

	// Needed?
	/*
	 * public void roundMinute(int hourOfDay, int min, CustomTime f) {
	 * f.setHour(hourOfDay); if (min <= 7) { f.setMinute(0); } else if (min > 7
	 * && min <= 22) { f.setMinute(15); } else if (min > 22 && min <= 37) {
	 * f.setMinute(30); } else if (min > 37 && min <= 52) { f.setMinute(45); }
	 * else { f.setMinute(0); f.setHour(hourOfDay + 1); } }
	 */

	public void save() {

		if (date_save_from == null) {
			date_save_from = date_DT_from;
		}
		if (date_save_to == null) {
			date_save_to = date_DT_to;
		}

		if (date_save_rec == null) {
			date_save_rec = date_DT_rec;
		}

		if (time_save_from == null) {
			time_save_from = time_DT_from;
		}

		if (time_save_to == null) {
			time_save_to = time_DT_to;
		}

		
		// if ((time_save_from.getHour() > time_save_to.getHour()
		// && date_save_from.getYear() >= date_save_to.getYear()
		// && date_save_from.getMonth() >= date_save_to.getMonth() &&
		// date_save_from
		// .getDay() >= date_save_to.getDay())
		// || (time_save_from.getHour() == time_save_to.getHour() &&
		// time_save_from
		// .getMinute() >= time_save_to.getMinute())
		// && date_save_from.getYear() >= date_save_to.getYear()
		// && date_save_from.getMonth() >= date_save_to.getMonth()
		// && date_save_from.getDay() >= date_save_to.getDay()) {
		//
		// HelperMethods.displayToast("You have inserted incorrect times",
		// this);
		// // return to MainActivity ???
		//
		// }
		
		// Validating input inserted by user: hourFrom < hourTo, Title not empty
		// Validation logic updated

		int from_yr = date_save_from.getYear();
		int to_yr = date_save_to.getYear();

		int from_mth = date_save_from.getMonth();
		int to_mth = date_save_to.getMonth();

		int from_day = date_save_from.getDay();
		int to_day = date_save_to.getDay();

		int from_hr = time_save_from.getHour();
		int to_hr = time_save_from.getHour();

		int from_min = time_save_from.getMinute();
		int to_min = time_save_from.getMinute();

		if (!((to_yr > from_yr) 
				|| (to_yr == from_yr && to_mth > from_mth)
				|| (to_mth == from_mth && to_day > from_day)
				|| (to_day == from_day && to_hr > from_hr) 
				|| (to_hr == from_hr && to_min > from_min))) {
			HelperMethods.displayToast("You have inserted incorrect times",
					this);
		} else if (etTitle.getText().toString().equals("")) {
			HelperMethods.displayToast("Please insert task title", this);
			// return to MainActivity ???
		}
		// input OK
		else {
			insertIntoDB(tag);
		}

	}

	/* Insert data chosen by user into database */
	public void insertIntoDB(String taskTag) {
		spinner = (Spinner) findViewById(R.id.spinner1);
		dbAdapter.Open();

		Log.v("if its not null", date_save_from.getDate());
		if (date_save_from == null) {
			// Log.v("date is null too", "");
		}

		// Log.v("date for DB", date_save_from.getDateForDB());
		// Log.v("time for DB", time_save_from.getTimeStr());
		/* workaround in case the date/time pickers werent selected at all */

		long inserted = 0;

		if (taskTag.equals("add")) {
			inserted = dbAdapter.createEvent(
					etTitle.getText().toString(),
					etBody.getText().toString(),
					date_save_from.getDateForDB() + " "
							+ time_save_from.getTimeStr(),
					date_save_to.getDateForDB() + " "
							+ time_save_to.getTimeStr(), "", "", "",
					String.valueOf(spinner.getSelectedItem()),
					date_save_rec.getDateForDB() + " 00:00");
			// date_save_rec.getDateForDB());
			Log.v("String.valueOf(spinner.getSelectedItem())",
					String.valueOf(spinner.getSelectedItem()));
			Log.v("date_save_rec.getDateForDB());",
					date_save_rec.getDateForDB());

			if (inserted > 0) {
				HelperMethods.displayToast("Successfully Saved", this);
				etTitle.setText("");
				etBody.setText("");
			} else {
				HelperMethods.displayToast("Insertion failed", this);
			}
		} else if (taskTag.equals("edit")) {
			if (dbAdapter.editEvent(id, etTitle.getText().toString(), etBody
					.getText().toString(), date_save_from.getDateForDB() + " "
					+ time_save_from.getTimeStr(), date_save_to.getDateForDB()
					+ " " + time_save_to.getTimeStr(), "", "", "", "", "")) {
				HelperMethods.displayToast("Successfully Saved", this);

			} else {
				HelperMethods.displayToast("Insertion failed", this);
			}
		}

		dbAdapter.Close();

		if (date_save_from != null) {
			// Log.v("get date", date_save_from.getDate());
		}

		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);

	}

	private void getDataFromDatabase(int eventId) {
		dbAdapter.Open();
		Cursor cursor = dbAdapter.viewEventByID(eventId);
		cursor.moveToFirst();

		date_DT_from = new CustomDate(cursor.getString(cursor
				.getColumnIndex("eventStartDayTime")));
		date_DT_to = new CustomDate(cursor.getString(cursor
				.getColumnIndex("eventEndDayTime")));
		time_DT_from = new CustomTime(cursor.getString(cursor
				.getColumnIndex("eventStartDayTime")));
		time_DT_to = new CustomTime(cursor.getString(cursor
				.getColumnIndex("eventEndDayTime")));
		name = cursor.getString(cursor.getColumnIndex("name"));
		description = cursor.getString(cursor.getColumnIndex("description"));

		dbAdapter.Close();
	}

}
