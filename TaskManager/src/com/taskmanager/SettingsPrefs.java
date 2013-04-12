package com.taskmanager;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import android.preference.PreferenceManager;

public class SettingsPrefs extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	
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
		Toast.makeText(getApplicationContext(), currValue,Toast.LENGTH_SHORT).show();
		//Log.d(TAG, "settings change key = "+key);
	}
	
    
}

