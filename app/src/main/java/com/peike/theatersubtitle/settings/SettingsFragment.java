package com.peike.theatersubtitle.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

import com.peike.theatersubtitle.R;

public class SettingsFragment extends PreferenceFragment implements SettingsActivity.FragView {

    private Preference storagePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_prefs);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        storagePreference = findPreference("pref_storage");
        setupClickListener();
    }

    @Override
    public void setSummary(String summary) {
        storagePreference.setSummary(summary);
    }

    private void setupClickListener() {
        storagePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ((SettingsActivity) getActivity()).onStoragePreferenceClicked();
                return true;
            }
        });
    }
}
