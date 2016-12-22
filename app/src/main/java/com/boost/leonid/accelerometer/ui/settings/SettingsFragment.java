package com.boost.leonid.accelerometer.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.boost.leonid.accelerometer.R;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    public static final String KEY_PREF_INTERVAL = "pref_list_interval";
    public static final String KEY_PREF_TIME_WHEN_START = "pref_time_start";
    public static final String KEY_PREF_TIME_DURATION = "pref_task_duration";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        initSummary();
    }

    private void initSummary() {
        Preference intervalPreference = findPreference(KEY_PREF_INTERVAL);
        intervalPreference.setSummary(getPreferenceManager().getSharedPreferences().getString(KEY_PREF_INTERVAL, ""));

        intervalPreference = findPreference(KEY_PREF_TIME_DURATION);
        intervalPreference.setSummary(getPreferenceManager().getSharedPreferences().getString(KEY_PREF_TIME_DURATION, ""));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference intervalPreference;
        switch (s){
            case KEY_PREF_INTERVAL:
                intervalPreference = findPreference(s);
                intervalPreference.setSummary(sharedPreferences.getString(s, ""));
                break;
            case KEY_PREF_TIME_DURATION:
                intervalPreference = findPreference(s);
                intervalPreference.setSummary(sharedPreferences.getString(s, ""));
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }
}
