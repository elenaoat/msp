package com.taskmanager;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewTaskActivity extends FragmentActivity implements
		OnClickListener {
	private DatabaseAdapter dbAdapter;
	private EditText etTitle, etBody, etTime;
	private Button saveBtn, cancelBtn;
	private TimePicker tp, tt;
	private DateTime from, to;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtask_activity);

		
		tp = (TimePicker) findViewById(R.id.time_picker_from);
		tt = (TimePicker) findViewById(R.id.time_picker_to);
		tp.setIs24HourView(true);
		tt.setIs24HourView(true);
/*		tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int min) {
				updateDisplay(hourOfDay, min, view);

			}
		});
		tt.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int min) {
				updateDisplay(hourOfDay, min, view);

			}
		});
*/
	//	setCurrentTime();
		dbAdapter = new DatabaseAdapter(getApplicationContext());

		etTime = (EditText) findViewById(R.id.etTime);
		etTitle = (EditText) findViewById(R.id.etTitle);
		etBody = (EditText) findViewById(R.id.etNote);

		saveBtn = (Button) findViewById(R.id.save_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);

		saveBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

	}

	public void setCurrentTime() {

		
		final Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		roundMinute(hour, minute, from);
		roundMinute(hour + 1, minute, to);
		tp.setCurrentHour(from.getHour());
		tp.setCurrentMinute(from.getMinute());
		tt.setCurrentHour(to.getHour());
		tt.setCurrentMinute(to.getMinute());
	}

	public void roundMinute(int hourOfDay, int min, DateTime f) {
		f.setMinute(min);
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

	/*public void updateDisplay(int hourOfDay, int min, TimePicker time_picker) {
		DateTime t = new DateTime(hourOfDay, min);
		roundMinute(hourOfDay, min, t);
		time_picker.setCurrentHour(t.getHour());
		time_picker.setCurrentMinute(t.getMinute());
	}*/

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
		return true;
	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			// Button tp = (Button) findViewById(R.id.from_time_picker);
			// Do something with the time chosen by the user
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == saveBtn) {
			String title = etTitle.getText().toString();
			String body = etBody.getText().toString();
			String time = etTime.getText().toString();
			dbAdapter.Open();

			long inserted = dbAdapter.InsertNote(time, title, body);

			if (inserted > 0) {
				DisplayToast("Successfully Saved " + inserted);
				etTitle.setText("");
				etBody.setText("");
			} else {
				DisplayToast("Insertion failed");
			}

			dbAdapter.Close();
		} else if (view == cancelBtn) {

		}
	}

	public void DisplayToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}
}
