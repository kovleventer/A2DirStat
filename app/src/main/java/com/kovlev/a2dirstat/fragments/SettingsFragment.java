package com.kovlev.a2dirstat.fragments;


import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import com.kovlev.a2dirstat.R;

/**
 * Fragment to help handling preferences
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
