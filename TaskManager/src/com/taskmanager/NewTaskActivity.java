package com.taskmanager;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewTaskActivity extends Activity {
	private DatabaseAdapter dbAdapter;
	private EditText etTitle, etBody;
	private Button fromBtn, toBtn;

	private DateTime from, to;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtask_activity);

		fromBtn = (Button) findViewById(R.id.from_time_picker);
		toBtn = (Button) findViewById(R.id.to_time_picker);
		etTitle = (EditText) findViewById(R.id.etTitle);
		etBody = (EditText) findViewById(R.id.etNote);

		setCurrentTime();
		dbAdapter = new DatabaseAdapter(getApplicationContext());
	}

	// Button click -> display TimePickerDialog
	public void chooseTime(View v) {

		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		switch (v.getId()) {
		case R.id.from_time_picker:
			roundMinute(hour, minute, from);
			new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
					from), from.getHour(), from.getMinute(), true).show();
			break;

		case R.id.to_time_picker:
			roundMinute(hour, minute, to);
			new TimePickerDialog(this, new CustomOnTimeSetListener((Button) v,
					to), to.getHour(), to.getMinute(), true).show();
			break;
		}

	}

	public void setCurrentTime() {

		final Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		from = new DateTime(0, 0);
		to = new DateTime(0, 0);

		roundMinute(hour, minute, from);
		roundMinute(hour, minute, to);
		fromBtn.setText(Integer.toString(from.getHour()) + ":"
				+ padMinute(from.getMinute()));
		toBtn.setText(Integer.toString(to.getHour()) + ":"
				+ padMinute(to.getMinute()));
	}

	public void roundMinute(int hourOfDay, int min, DateTime f) {
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

	public StringBuffer padMinute(int minute) {
		StringBuffer strBuff = new StringBuffer();
		strBuff.append(minute);
		if (Integer.toString(minute).length() == 1) {
			strBuff.append("0");
		}
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
		}	else {
			dbAdapter.Open();

			long inserted = dbAdapter.InsertNote(
					Integer.toString(from.getHour()) + padMinute(from.getMinute()), title, body);

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
		private DateTime dtime;

		public CustomOnTimeSetListener(Button v, DateTime dtime) {
			this.btn = v;
			this.dtime = dtime;
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			roundMinute(hourOfDay, minute, dtime);
			StringBuffer sb = new StringBuffer();
			sb.append(Integer.toString(dtime.getHour()));
			sb.append(":");
			sb.append(padMinute(dtime.getMinute()));
			btn.setText(sb);

		}

	}

}
