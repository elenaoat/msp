package com.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class WeekViewActivity extends Activity implements OnClickListener  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		super.setContentView(R.layout.week_vu);
		
//		Bundle extras = getIntent().getExtras();
//		if(extras !=null)
//		{
//			Toast.makeText(getApplicationContext(), extras.getShort("message"), Toast.LENGTH_SHORT).show();
//		}
		
		
		Button dayBtn = (Button) findViewById(R.id.day_btn);		
		Button weekBtn = (Button) findViewById(R.id.week_btn);
		Button monthBtn = (Button) findViewById(R.id.month_btn);
		ImageButton addBtn = (ImageButton) findViewById(R.id.add_event);
		
		dayBtn.setOnClickListener(this);
		weekBtn.setOnClickListener(this);
		monthBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String message = "";
		if(v.getId()==R.id.add_event)
		{
			message = "Add Event button clicked";
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			
		}
		else if(v.getId()==R.id.day_btn)
		{
			Intent dayIntent = new Intent(getBaseContext(), MainActivity.class);                      
			startActivity(dayIntent);
		}
		else if(v.getId()==R.id.week_btn)
		{
			message = "You are in the week view";
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			
		}
		else if(v.getId()==R.id.month_btn)
		{
			Intent monthIntent = new Intent(getBaseContext(), MonthViewActivity.class);                      
			startActivity(monthIntent);
		}
	}

}
