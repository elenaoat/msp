package com.taskmanager;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeekViewActivity extends Activity implements OnClickListener  {
	
	private ImageButton prevWeek;
	private ImageButton nextWeek;
	private LinearLayout lLayout, cLayout;
	private TextView tView;
	private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	int weekNumber,year;
	private Calendar c1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		super.setContentView(R.layout.week_vu);
		
		
		
		Button dayBtn = (Button) findViewById(R.id.day_btn);		
		Button weekBtn = (Button) findViewById(R.id.week_btn);
		Button monthBtn = (Button) findViewById(R.id.month_btn);
		ImageButton addBtn = (ImageButton) findViewById(R.id.add_event);
		
		prevWeek = (ImageButton) this.findViewById(R.id.prevWeek);
		prevWeek.setOnClickListener(this);
		
		nextWeek = (ImageButton) this.findViewById(R.id.nextWeek);
		nextWeek.setOnClickListener(this);
		
		dayBtn.setOnClickListener(this);
		weekBtn.setOnClickListener(this);
		monthBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		
		c1 = Calendar.getInstance();
		weekNumber= c1.get(Calendar.WEEK_OF_YEAR);
		
		weekView(weekNumber);
	}	
	
	public void weekView(int weekNum){
	    //first day of week
		
		c1.set(Calendar.WEEK_OF_YEAR, weekNum);
		//Toast.makeText(getApplicationContext(), ""+c1.get(Calendar.WEEK_OF_YEAR)+"--"+c1.getFirstDayOfWeek() , Toast.LENGTH_SHORT).show();
		c1.get(Calendar.WEEK_OF_YEAR);
		c1.set(Calendar.DAY_OF_WEEK, c1.getFirstDayOfWeek());

	    int year1 = c1.get(Calendar.YEAR);
	    int month1 = c1.get(Calendar.MONTH)+1;
	    int day1 = c1.get(Calendar.DAY_OF_MONTH);

	    //last day of week
	    c1.set(Calendar.DAY_OF_WEEK, 7);
//	    c1.add(Calendar.WEEK_OF_YEAR, 1);

	    int year7 = c1.get(Calendar.YEAR);
	    int month7 = c1.get(Calendar.MONTH) + 1;
	    int day7 = c1.get(Calendar.DAY_OF_MONTH) + 1;
	    tView = new TextView(this);
	    tView = (TextView) findViewById(R.id.weekHeaderTxt);
	    tView.setText("Week " + c1.get(Calendar.WEEK_OF_YEAR) + " : " + day1 + "/" + month1 + "/" + year1 + " - " + day7 + "/" + month7 + "/" + year7);
		lLayout=new LinearLayout(this);
		lLayout = (LinearLayout) findViewById(R.id.weekDays);
		//lLayout.setOrientation(LinearLayout.HORIZONTAL);
		tView = new TextView(this);
		tView.setText("TIMES");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.weight = 11;
		tView.setLayoutParams(params);
		
		lLayout.addView(tView);
		
		int day, month;
		
		for(int i = 0; i < 7; i ++)
		{
			c1.set(Calendar.DAY_OF_WEEK, i+1);
//		    c1.add(Calendar.WEEK_OF_YEAR, 1);

		    month = c1.get(Calendar.MONTH) + 1;
		    day = c1.get(Calendar.DAY_OF_MONTH);
			
			
			tView = new TextView(this);
			tView.setText(weekdays[i]+" " + day + "/" + month);
			params = new LinearLayout.LayoutParams(
				    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 13;
			tView.setLayoutParams(params);
			tView.setBackgroundResource(R.drawable.rect_shape);
			
			lLayout.addView(tView);
		}
		
		//details of week
		lLayout = (LinearLayout) findViewById(R.id.weekDaysDetails);
		
		LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(
			    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		for(int p = 0; p < 24; p++)
		{
			cLayout = new LinearLayout(this);
			cLayout.setOrientation(LinearLayout.HORIZONTAL);
			cParams.weight = 100;
			cLayout.setLayoutParams(cParams);
			
			
			tView = new TextView(this);
			if(p < 10)
				tView.setText("0" + p + ":00");
			else
				tView.setText(p + ":00");

			LinearLayout.LayoutParams tParams = new LinearLayout.LayoutParams(
				    LayoutParams.WRAP_CONTENT, 60);
			tParams.weight = 9;
			tView.setLayoutParams(tParams);
			
			cLayout.addView(tView);
			
			for(int q = 0; q < 7; q++)
			{
				tView = new TextView(this);
				tView.setText("");
				tParams = new LinearLayout.LayoutParams(
					    LayoutParams.WRAP_CONTENT, 60);
				tParams.weight = 13;
				tView.setLayoutParams(tParams);
				tView.setBackgroundResource(R.drawable.round_rect_shape);
				
				cLayout.addView(tView);
			}
			
			lLayout.addView(cLayout);
		}
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
		}if (v == prevWeek){
			if (weekNumber <= 1)
				{
					weekNumber = 52;
					year--;
				}
			else
				{
				weekNumber--;
				}
			lLayout.invalidate();
			weekView(weekNumber);
		}if (v == nextWeek)
		{
			if (weekNumber > 52)
				{
					weekNumber=1;
					year++;
				}
			else
				{
				weekNumber++;
				}
			tView.invalidate();
			lLayout.invalidate();
			weekView(weekNumber);
		}
	}

}
