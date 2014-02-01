package com.jach.examencisa.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.jach.examencisa.R;

public class SettingsActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

}
