package com.example.deduce.deduceit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.prefs.PreferenceChangeListener;

/**
 * Created by Bhargav on 15-06-2016.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("word_size")){
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(key,""));
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
        else if(key.equals("username")){
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(key,""));
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
    }
}
