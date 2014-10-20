package com.planauts.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.planauts.common.Constants;
import com.planauts.wsj.R;

/**
 * Created by svaithia@uwaterloo.ca on 10/19/14.
 */
public class SettingsActivity extends ActionBarActivity{
  private Spinner sQuality;
  private ToggleButton tbWifiSettings;
  public static final String SETTINGS = "SETTINGS";

  private SharedPreferences sharedpreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings_activity);

    sQuality = (Spinner) findViewById(R.id.sQuality);
    tbWifiSettings = (ToggleButton) findViewById(R.id.tbWifiSettings);


    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
      this, android.R.layout.simple_spinner_item, Constants.VIDEO_RESOLUTION);
    spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
    sQuality.setAdapter(spinnerArrayAdapter);

    sharedpreferences = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

    if (sharedpreferences.contains("Quality")){

    }
    tbWifiSettings.setChecked(sharedpreferences.getBoolean("WiFi", false));

  }
}
