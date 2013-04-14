package com.taskmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

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

		new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
				time_save_from), time_DT_from.getHour(), time_DT_from.getMinute(), true)
				.show();

	}

	public void chooseTimeTo(View v) {

		new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
				time_save_to), time_DT_to.getHour(), time_DT_to.getMinute(), true)
				.show();

	}

	public void chooseDateTo(View v) {

		CustomOnDateSetListener customDateListener = new CustomOnDateSetListener(
				(Button) v, date_save_to);
		new DatePickerDialog(this, customDateListener, date_DT_to.getYear(),
				date_DT_to.getMonth(), date_DT_to.getDay()).show();
	}

	public void chooseDateFrom(View v) {

		CustomOnDateSetListener customDateListener = new CustomOnDateSetListener(
				(Button) v, date_save_from);
		new DatePickerDialog(this, customDateListener, date_DT_from.getYear(),
				date_DT_from.getMonth(), date_DT_from.getDay()).show();
	}

	public void setReceivedTime() {
		/*
		 * final Calendar cal = Calendar.getInstance(); int hour =
		 * cal.get(Calendar.HOUR_OF_DAY); int minute = cal.get(Calendar.MINUTE);
		 * 
		 * from = new DateTime(0, 0); to = new DateTime(0, 0);
		 * 
		 * roundMinute(hour, minute, from); roundMinute(hour, minute, to);
		 */

		// fromTimeBtn.setText(padTime(time_DT.getHour()) + ":"
		// + padTime(time_sent_DT.getMinute()));

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

	/*
	 * public StringBuffer padTime(int m) { StringBuffer strBuff = new
	 * StringBuffer();
	 * 
	 * if (Integer.toString(m).length() == 1) { strBuff.append(0); }
	 * strBuff.append(m); return strBuff; }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
		return true;
	}

	public void save(View view) {

		/*
		 * if ((from.getHour() > to.getHour()) || (from.getHour() ==
		 * to.getHour() && from.getMinute() >= to .getMinute())) {
		 * displayToast("You have inserted incorrect times from: " +
		 * Integer.toString(from.getMinute()) + " to: " +
		 * Integer.toString(to.getMinute())); } else if (title.equals("")) {
		 * displayToast("Please insert task title"); } else {
		 */

		dbAdapter.Open();

		/*
		 * long inserted = dbAdapter.createBriefEvent( title, body,
		 * date_to_save, date_to_save);
		 */

		/*workaround in case the date/time pickers werent selected at all*/
		if (date_save_from==null){
			date_save_from = date_DT_from;
		}
		if (date_save_to==null){
			date_save_to = date_DT_to;
		}

		if (time_save_from==null){
			time_save_from = time_DT_from;
		}

		if (time_save_to==null){
			time_save_to = time_DT_to;
		}

		long inserted = dbAdapter.createBriefEvent(
				etTitle.getText().toString(),
				etBody.getText().toString(),
				date_save_from.getDateForDB() + " "
						+ time_save_from.getTimeStr(),
				date_save_to.getDateForDB() + " "
						+ time_save_to.getTimeStr());

		/*
		 * "2013-04-07 " + padTime(from.getHour()) + ":" +
		 * padTime(from.getMinute()), "2013-04-07 " + padTime(to.getHour()) +
		 * ":" + padTime(to.getMinute()));
		 */

		if (inserted > 0) {
			displayToast("Successfully Saved");
			etTitle.setText("");
			etBody.setText("");
		} else {
			displayToast("Insertion failed");
		}

		dbAdapter.Close();

		if (date_save_from != null) {
			Log.v("get date", date_save_from.getDate());
		}
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		// }
	}

	public void cancel(View view) {

	}

	public void displayToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}

	class CustomOnTimeSetListener implements TimePickerDialog.OnTimeSetListener {
		private Button btn;
		private CustomTime dtime;

		public CustomOnTimeSetListener(Button v, CustomTime dtime) {
			this.btn = v;
			this.dtime = dtime;
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dtime = new CustomTime(hourOfDay, minute);
			/*
			 * roundMinute(hourOfDay, minute, dtime); StringBuffer sb = new
			 * StringBuffer(); sb.append(padTime(dtime.getHour()));
			 * sb.append(":"); sb.append(padTime(dtime.getMinute()));
			 */
			btn.setText(dtime.getTimeStr());
		}

	}

	class CustomOnDateSetListener implements DatePickerDialog.OnDateSetListener {
		private Button btn;
		private CustomDate date;

		public CustomOnDateSetListener(Button v, CustomDate date) {
			this.btn = v;
			this.date = date;
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {

			date = new CustomDate(day, month, year);
			Log.v("date inside the method", date.getDate());
			btn.setText(date.getDate());

		}

	}

}
