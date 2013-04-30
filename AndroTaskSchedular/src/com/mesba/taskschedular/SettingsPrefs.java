package com.mesba.taskschedular;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.mesba.dynamicui.R;

public class SettingsPrefs extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	
	 private String TAG= "prefs";
	 private DatabaseAdapter dbAdapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_prefs);
		
		 PreferenceManager.setDefaultValues(SettingsPrefs.this, R.xml.settings_prefs, false);
	     dbAdapter= new DatabaseAdapter(getApplicationContext());
	}
	
	@SuppressWarnings("deprecation")
	protected void onResume(){
        super.onResume();
        // Set up a listener whenever a key changes             
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
		// TODO Auto-generated method stub
		
		@SuppressWarnings("deprecation")
		ListPreference listPreference = (ListPreference) findPreference(key);
		String currValue = listPreference.getValue();
		Toast.makeText(getApplicationContext(),currValue.substring(0, 2),Toast.LENGTH_SHORT).show();
		Log.v(TAG, "settings change key = "+key);
		dbAdapter.Open();
		if(key.equals("reminder")){
			dbAdapter.setNotificationB4(Integer.parseInt(currValue.substring(0, 2)));
		}else if(key.equals("frequency")){
			dbAdapter.setNotificationFreq(Integer.parseInt(currValue.substring(0, 2)));
		}else if(key.equals("type")){
			dbAdapter.setNotificationType(currValue);
		}else if(key.equals("repetition")){
			if(currValue.equals("Once")){
				currValue="";
			}
			dbAdapter.setRecurrenceFlag(currValue);
		}
		dbAdapter.Close();
	}
	
  
	
}
