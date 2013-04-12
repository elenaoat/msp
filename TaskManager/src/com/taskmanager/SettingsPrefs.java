package com.taskmanager;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;
import android.preference.PreferenceManager;
import java.lang.Integer;

public class SettingsPrefs extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	
   private String TAG= "prefs";
   private DatabaseAdapter dbAdapter;


@Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try {
            getClass().getMethod("getFragmentManager");
            ForwardByFragment();
        } catch (NoSuchMethodException e) { 
            ForwardWithDeps();
        }
      
        PreferenceManager.setDefaultValues(SettingsPrefs.this, R.xml.settings_prefs, false);
        dbAdapter= new DatabaseAdapter(getApplicationContext());
   }

    @SuppressWarnings("deprecation")
    protected void ForwardWithDeps()
    {
        addPreferencesFromResource(R.xml.settings_prefs);
    }

    @TargetApi(11)
    protected void ForwardByFragment(){
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenceFrgmentHelper()).commit();
    }
    @TargetApi(11)
    public static class PreferenceFrgmentHelper extends PreferenceFragment{       
        @Override
        public void onCreate(final Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_prefs); //outer class private members seem to be visible for inner class, and making it static made things so much easier
        }
    }
    
    @SuppressWarnings("deprecation")
	@Override 
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

