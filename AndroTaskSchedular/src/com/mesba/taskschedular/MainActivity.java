package com.mesba.taskschedular;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mesba.dynamicui.R;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class MainActivity extends Activity {
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private DatabaseAdapter dbAdapter;

	public final static String DATE = "com.taskmanager.DATE";
	public final static String HOUR = "com.taskmanager.HOUR";

	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public Calendar today = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbAdapter = new DatabaseAdapter(getApplicationContext());
		initializeGlobalConfigWithDefaultValues();

		ActionBar actionbar = getActionBar();
		actionbar.show();

		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// initiating both tabs and set text to it.
		ActionBar.Tab DayTab = actionbar.newTab().setText("Day View");
		ActionBar.Tab WeekTab = actionbar.newTab().setText("Week View");
		ActionBar.Tab MonthTab = actionbar.newTab().setText("Month View");
		ActionBar.Tab TaskListTab = actionbar.newTab().setText("Tasks List");

		// create the two fragments we want to use for display content
		Fragment DayFragment = new DayViewFragment();
		Fragment WeekFragment = new WeekViewFragment();
		Fragment MonthFragment = new MonthViewFragment();
		Fragment TasksFragment = new TaskListViewFragment();

		// set the Tab listener. Now we can listen for clicks.
		DayTab.setTabListener(new MyTabsListener(DayFragment));
		WeekTab.setTabListener(new MyTabsListener(WeekFragment));
		MonthTab.setTabListener(new MyTabsListener(MonthFragment));
		TaskListTab.setTabListener(new MyTabsListener(TasksFragment));

		// add the two tabs to the actionbar
		actionbar.addTab(TaskListTab);
		actionbar.addTab(DayTab);
		actionbar.addTab(WeekTab);
		actionbar.addTab(MonthTab);		

		// set the app icon as an action to go home
		actionbar.setDisplayHomeAsUpEnabled(false);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_add:
			Intent intent = new Intent(getApplicationContext(),
					AddNewTaskActivity.class);
			String date = dateFormat.format(today.getTime());
			int time = 0;
			intent.putExtra(DATE, date);
			intent.putExtra(HOUR, time);
			intent.putExtra("tag", "add");
			startActivity(intent);
			break;
		case R.id.item_today:
			Toast.makeText(this, "Today...", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_settings:
			Intent setting = new Intent(MainActivity.this,
					SettingsPrefs.class);
			startActivity(setting);
			break;
		case R.id.action_about:
			showAbout();
//			Toast.makeText(this, "About us...", Toast.LENGTH_SHORT).show();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * ???
	 */
	public void initializeGlobalConfigWithDefaultValues() {
		dbAdapter.Open();
		dbAdapter.initializeGConfig();
		dbAdapter.Close();
	}

	public void showAbout() {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.layout_about);
		dialog.setTitle("About Task Schedular");

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.append("\n" + "Android custom dialog example!");
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.ic_launcher_task);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(getApplicationContext(), "Reselected!",
					Toast.LENGTH_LONG).show();
		}

		@SuppressLint("NewApi")
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}
	}
}
