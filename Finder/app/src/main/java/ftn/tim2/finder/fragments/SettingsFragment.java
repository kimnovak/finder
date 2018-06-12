package ftn.tim2.finder.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import ftn.tim2.finder.R;
import ftn.tim2.finder.activities.LoginActivity;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.app_preferences);
        Map<String, ?> prefs = getPreferenceManager().getSharedPreferences().getAll();
        for(Object d : prefs.keySet()) {
            Log.d("aa", d + " " + prefs.get(d).toString());
        }

        Preference myPref = findPreference(getString(R.string.account_switch_key));
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
}
