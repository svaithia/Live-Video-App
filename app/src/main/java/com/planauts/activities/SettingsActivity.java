package com.planauts.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.planauts.common.Constants;
import com.planauts.wsj.R;

/**
 * Created by svaithia@uwaterloo.ca on 10/19/14.
 */
public class SettingsActivity extends ActionBarActivity implements View.OnClickListener{
  private Spinner sQuality;
  private ToggleButton tbWifiSettings;
  private Button bSaveSettings;

  public static final String SETTINGS = "SETTINGS";

  private SharedPreferences sharedpreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings_activity);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    sQuality = (Spinner) findViewById(R.id.sQuality);
    tbWifiSettings = (ToggleButton) findViewById(R.id.tbWifiSettings);
    bSaveSettings = (Button) findViewById(R.id.bSaveSettings);

    bSaveSettings.setOnClickListener(this);


    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
      this, android.R.layout.simple_spinner_item, Constants.VIDEO_RESOLUTION);
    spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
    sQuality.setAdapter(spinnerArrayAdapter);

    sharedpreferences = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

    sQuality.setSelection(sharedpreferences.getInt("quality", 1));
    tbWifiSettings.setChecked(sharedpreferences.getBoolean("wifi", false));
  }


  @Override
  public void onClick(View view) {
    switch(view.getId()){
      case R.id.bSaveSettings:{
        sharedpreferences.edit().putBoolean("wifi", tbWifiSettings.isChecked()).apply();
        sharedpreferences.edit().putInt("quality", sQuality.getSelectedItemPosition()).apply();
        finish();
        break;
      }
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
