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
	private String date_sent, time_sent;
	private CustomDate date_sent_DT;
	private CustomTime from, to, time_sent_DT;

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

		date_sent = getIntent().getStringExtra("com.taskmanager.DATE");
		time_sent = getIntent().getStringExtra("com.taskmanager.TIME");
		
		Log.v("time", time_sent);
		Log.v("date", date_sent);
		time_sent_DT = new CustomTime(Integer.parseInt(time_sent), 0);
		date_sent_DT = new CustomDate(date_sent);
		
		setReceivedTime();
		setReceivedDate();
		
		dbAdapter = new DatabaseAdapter(getApplicationContext());
	}

	// Button click -> display TimePickerDialog
	public void chooseTime(View v) {

		new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
				time_sent_DT), time_sent_DT.getHour(), time_sent_DT.getMinute(), true).show();
/*		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);*/
		
	/*	switch (v.getId()) {
		case R.id.from_time_picker:
			//roundMinute(time_sent_DT.getHour(), time_sent_DT.getMinute(), from);
			new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
					time_sent_DT), time_sent_DT.getHour(), time_sent_DT.getMinute(), true).show();
			break;

		case R.id.to_time_picker:
			//roundMinute(hour, minute, to);
			new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
					time_sent_DT), time_sent_DT.getHour(), time_sent_DT.getMinute(), true).show();
			break;
		}*/

	}
	
	public void chooseDate(View v) {

		new DatePickerDialog(this, new CustomOnDateSetListener((Button) v, date_sent), date_sent_DT.getYear(),date_sent_DT.getMonth(), date_sent_DT.getDay()).show();
/*		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);*/
		
/*		switch (v.getId()) {
		case R.id.from_date_picker:
			
			new DatePickerDialog(this, new CustomOnDateSetListener((Button) v, date_sent), date_sent_DT.getYear(),date_sent_DT.getMonth(), date_sent_DT.getDay()).show();
			break;

		case R.id.to_date_picker:
			
			new DatePickerDialog(this, new CustomOnDateSetListener((Button) v,
					date_sent), date_sent.substring(0, 3), date_sent.substring(5, 6), date_sent.substring(8, 9) , true).show();
			break;
		}*/

	}

	public void setReceivedTime() {

		/*final Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		from = new DateTime(0, 0);
		to = new DateTime(0, 0);

		roundMinute(hour, minute, from);
		roundMinute(hour, minute, to);*/
		fromTimeBtn.setText(padTime(time_sent_DT.getHour()) + ":"
				+ padTime(time_sent_DT.getMinute()));
		toTimeBtn.setText(padTime(time_sent_DT.getHour()) + ":" + padTime(time_sent_DT.getMinute()));
	}
	public void setReceivedDate(){
		StringBuilder builder = new StringBuilder();
		builder.append(date_sent_DT.getDay_str());
		builder.append("-");
		builder.append(date_sent_DT.getMonth_str());
		builder.append("-");
		builder.append(date_sent_DT.getYear_str());

		fromDateBtn.setText(builder);
		toDateBtn.setText(builder);
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

	public StringBuffer padTime(int m) {
		StringBuffer strBuff = new StringBuffer();
		
		if (Integer.toString(m).length() == 1) {
			strBuff.append(0);
		}
		strBuff.append(m);
		return strBuff;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
		return true;
	}

	public void save(View view) {

		String title = etTitle.getText().toString();
		String body = etBody.getText().toString();

		if ((from.getHour() > to.getHour())
				|| (from.getHour() == to.getHour() && from.getMinute() >= to
						.getMinute())) {
			displayToast("You have inserted incorrect times from: "
					+ Integer.toString(from.getMinute()) + " to: "
					+ Integer.toString(to.getMinute()));
		} else if (title.equals("")) {
			displayToast("Please insert task title");
		} else {
			dbAdapter.Open();

			long inserted = dbAdapter.createBriefEvent(
					title,
					body,
					"2013-04-07 " + padTime(from.getHour()) + ":"
							+ padTime(from.getMinute()),
					"2013-04-07 " + padTime(to.getHour()) + ":"
							+ padTime(to.getMinute()));

			if (inserted > 0) {
				displayToast("Successfully Saved");
				etTitle.setText("");
				etBody.setText("");
			} else {
				displayToast("Insertion failed");
			}

			dbAdapter.Close();
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		}
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
			roundMinute(hourOfDay, minute, dtime);
			StringBuffer sb = new StringBuffer();
			sb.append(padTime(dtime.getHour()));
			sb.append(":");
			sb.append(padTime(dtime.getMinute()));
			btn.setText(sb);

		}

	}
	
	class CustomOnDateSetListener implements DatePickerDialog.OnDateSetListener {
		private Button btn;
		private String date;

		public CustomOnDateSetListener(Button v, String date) {
			this.btn = v;
			this.date = date;
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {

			StringBuilder sb = new StringBuilder();
			sb.append(date.substring(8, 10));
			sb.append("-");
			sb.append(date.substring(5, 7));
			sb.append("-");
			sb.append(date.substring(0, 4));
			
			btn.setText(sb);

		}

	}

}
