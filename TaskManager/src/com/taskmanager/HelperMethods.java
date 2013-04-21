package com.taskmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;

public class HelperMethods {

	public static CustomTime chooseTime(View v, CustomTime time_DT_from, Activity activity) {

		CustomTime time_save_from = new CustomTime(0, 0);
		new TimePickerDialog(activity, new CustomOnTimeSetListener((Button) v,
				time_save_from), time_DT_from.getHour(),
				time_DT_from.getMinute(), true).show();

		return time_save_from;
	}
	
	
	public static CustomDate chooseDate(View v, CustomDate date_DT_from, Activity activity) {

		CustomDate date_save_from = new CustomDate(0, 0, 0);
		new DatePickerDialog(activity, new CustomOnDateSetListener((Button) v,
				date_save_from), date_DT_from.getYear(),
				date_DT_from.getMonth(), date_DT_from.getDay()).show();

		return date_save_from;
	}
	
	
	public static void save(View view, ArrayList <CustomDate> dates, ArrayList <CustomTime> times) {

		if (dates.get(0) == null) {
			dates.set(0, dates.get(1));
		}
		if (dates.get(2) == null) {
			dates.set(2, dates.get(3));
		}

		if (times.get(0) == null) {
			times.set(0, times.get(1));
		}
		if (times.get(2) == null) {
			times.set(2, times.get(3));
		}




		// Validating input inserted by user: hourFrom < hourTo, Title not empty
/*		if ((time_save_from.getHour() > time_save_to.getHour()
				&& date_save_from.getYear() >= date_save_to.getYear()
				&& date_save_from.getMonth() >= date_save_to.getMonth() && date_save_from
				.getDay() >= date_save_to.getDay())
				|| (time_save_from.getHour() == time_save_to.getHour() && time_save_from
						.getMinute() >= time_save_to.getMinute())
				&& date_save_from.getYear() >= date_save_to.getYear()
				&& date_save_from.getMonth() >= date_save_to.getMonth()
				&& date_save_from.getDay() >= date_save_to.getDay()) {

			displayToast("You have inserted incorrect times");
			// return to MainActivity ???

		} else if (etTitle.getText().toString().equals("")) {
			displayToast("Please insert task title");
			// return to MainActivity ???
		}
		// input OK
		else {
			insertIntoDB();
		}*/

	}
	
}
