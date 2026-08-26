package com.android.setup;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class TestFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.test_preferences, rootKey);
    }
}