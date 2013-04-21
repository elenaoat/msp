package com.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditTaskActivity extends Activity {

	private DatabaseAdapter dbAdapter;
	private CustomDate from, to, selected_date_from, selected_date_to;
	private CustomTime from_t, to_t, selected_from, selected_to;
	private String name, description;
	private Button from_date, to_date, from_time, to_time;
	private EditText title, note;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_task);
		
		from_date = (Button) findViewById(R.id.edit_from_date_picker);
		to_date = (Button) findViewById(R.id.edit_to_date_picker);
		from_time = (Button) findViewById(R.id.edit_from_time_picker);
		to_time = (Button) findViewById(R.id.edit_to_time_picker);
		title = (EditText) findViewById(R.id.edit_etTitle);
		note = (EditText) findViewById(R.id.edit_etNote);
		
		
		
		Intent intent = getIntent();
		if (intent.hasExtra("com.taskmanager.ID")) {
			id = intent.getIntExtra("com.taskmanager.ID", 0);
			Log.v("ID obtained from another activity", Integer.toString(id));
		} else {
			Log.v("Error", "obtaining ID");
		}

		dbAdapter = new DatabaseAdapter(getApplicationContext());
		getDataFromDatabase();
		putDataToUI();
	}
	
	//public CustomTime chooseTimeFrom(View v, CustomTime time_DT_from, Activity activity)

	private void getDataFromDatabase() {
		dbAdapter.Open();
		Cursor cursor = dbAdapter.viewEventByID(id);
		cursor.moveToFirst();

		from = new CustomDate(cursor.getString(cursor
				.getColumnIndex("eventStartDayTime")));
		to = new CustomDate(cursor.getString(cursor
				.getColumnIndex("eventEndDayTime")));
		from_t = new CustomTime(cursor.getString(cursor
				.getColumnIndex("eventStartDayTime")));
		to_t = new CustomTime(cursor.getString(cursor
				.getColumnIndex("eventEndDayTime")));
		// Log.v("id ", Integer.toString(id));
		name = cursor.getString(cursor.getColumnIndex("name"));
		description = cursor.getString(cursor
				.getColumnIndex("description"));
		// Log.v("description", description);
		// Log.v("event end", eventEndDayTime);
		dbAdapter.Close();

	}
	private void putDataToUI(){
		from_date.setText(from.getDate());
		to_date.setText(to.getDate());
		from_time.setText(from_t.getTimeStr());
		to_time.setText(to_t.getTimeStr());
		title.setText(name);
		note.setText(description);
	}

	public void chooseTimeFromCustom (View v){
		selected_from = HelperMethods.chooseTime(v, from_t, this);
		Log.v("time from", selected_from.getTimeStr());
	}

	public void chooseTimeToCustom (View v){
		selected_to = HelperMethods.chooseTime(v, to_t, this);
	}
	
	public void chooseDateFromCustom (View v){
		selected_date_from = HelperMethods.chooseDate(v, from, this);
		//Log.v("time from", selected_from.getTimeStr());
	}

	public void chooseDateToCustom (View v){
		selected_date_to = HelperMethods.chooseDate(v, to, this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_task, menu);
		return true;
	}

}
