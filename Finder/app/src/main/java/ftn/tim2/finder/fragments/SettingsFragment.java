package ftn.tim2.finder.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Map;

import ftn.tim2.finder.R;



public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);
        Map<String, ?> prefs = getPreferenceManager().getSharedPreferences().getAll();
        for(Object d : prefs.keySet()) {
            Log.d("aa", d + " " + prefs.get(d).toString());
        }
    }
}
