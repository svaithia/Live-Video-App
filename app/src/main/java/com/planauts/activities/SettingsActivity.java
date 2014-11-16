package com.planauts.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.amplitude.api.Amplitude;
import com.planauts.common.Constants;
import com.planauts.wsj.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by svaithia@uwaterloo.ca on 10/19/14.
 */
public class SettingsActivity extends ActionBarActivity implements View.OnClickListener{
  private Spinner sQuality;
  private ToggleButton tbWifiSettings;
  private Button bSaveSettings;
  private EditText etShareMessage;

  public static final String SETTINGS = "SETTINGS";
  public static final String QUALITY = "quality";
  public static final String WIFI_ONLY = "wifi";
  public static final String DEFAULT_SHARE = "share";

  private SharedPreferences sharedpreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings_activity);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    sQuality = (Spinner) findViewById(R.id.sQuality);
    tbWifiSettings = (ToggleButton) findViewById(R.id.tbWifiSettings);
    bSaveSettings = (Button) findViewById(R.id.bSaveSettings);
    etShareMessage = (EditText) findViewById(R.id.etShareMessage);

    bSaveSettings.setOnClickListener(this);


    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
      this, android.R.layout.simple_spinner_item, Constants.VIDEO_RESOLUTION);
    spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
    sQuality.setAdapter(spinnerArrayAdapter);

    sharedpreferences = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

    sQuality.setSelection(sharedpreferences.getInt(QUALITY, 1));
    tbWifiSettings.setChecked(sharedpreferences.getBoolean(WIFI_ONLY, false));
    etShareMessage.setText(sharedpreferences.getString(DEFAULT_SHARE, getString(R.string.share_default_message)));

    JSONObject eventProperties = new JSONObject();
    try {
      eventProperties.put(WIFI_ONLY, tbWifiSettings.isChecked());
      eventProperties.put(QUALITY, sQuality.getSelectedItemPosition());
      eventProperties.put(DEFAULT_SHARE, etShareMessage.getText().toString());
    } catch (JSONException exception) {
    }

    Amplitude.logEvent("Settings Open", eventProperties);


  }

  @Override
  public void onClick(View view) {
    switch(view.getId()){
      case R.id.bSaveSettings:{
        sharedpreferences.edit().putBoolean(WIFI_ONLY, tbWifiSettings.isChecked()).apply();
        sharedpreferences.edit().putInt(QUALITY, sQuality.getSelectedItemPosition()).apply();
        sharedpreferences.edit().putString(DEFAULT_SHARE, etShareMessage.getText().toString()).apply();

        JSONObject eventProperties = new JSONObject();
        try {
          eventProperties.put(WIFI_ONLY, tbWifiSettings.isChecked());
          eventProperties.put(QUALITY, sQuality.getSelectedItemPosition());
          eventProperties.put(DEFAULT_SHARE, etShareMessage.getText().toString());
        } catch (JSONException exception) {
        }

        Amplitude.logEvent("Settings Saved", eventProperties);
        finish();
        break;
      }
    }
  }

  @Override
  protected void onResume() {
    super.onPostResume();
    Amplitude.startSession();
  }

  @Override
  protected void onPause() {
    super.onPause();
    Amplitude.endSession();
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
