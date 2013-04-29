package com.mesba.taskschedular;

import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

public class CustomOnDateSetListener implements DatePickerDialog.OnDateSetListener {
	private Button btn;
	private CustomDate date;

	public CustomOnDateSetListener(Button v, CustomDate date) {
		this.btn = v;
		this.date = date;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {

		
		date.setDay(day);
		date.setMonth(month);
		date.setYear(year);
		Log.v("date inside the method", Integer.toString(date.getYear()));
		btn.setText(date.getDate());

	}

}
