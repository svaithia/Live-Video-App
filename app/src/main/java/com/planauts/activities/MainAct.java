package com.planauts.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import com.amplitude.api.Amplitude;
import com.planauts.bean.SectionURLBean;
import com.planauts.common.Constants;
import com.planauts.listadapters.FragmentPageAdapter;
import com.planauts.scrapper.SectionScrapper;
import com.planauts.wsj.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class MainAct extends ActionBarActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener{
  private static final String TAG = MainAct.class.getSimpleName();
  ActionBar mActionBar;
  ViewPager vpMain;
  FragmentPageAdapter mFragmentPageAdapter;
  private int VIDEO_QUALITY;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main1);

    Amplitude.initialize(this, Constants.AMPLITUDE_API_KEY);

    vpMain = (ViewPager) findViewById(R.id.vpMain);
    mFragmentPageAdapter = new FragmentPageAdapter(getSupportFragmentManager()) ;

    mActionBar = getSupportActionBar();
    vpMain.setAdapter(mFragmentPageAdapter);
    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    mActionBar.addTab(mActionBar.newTab().setText("Spotlight").setTabListener(this));
    mActionBar.addTab(mActionBar.newTab().setText("Browse").setTabListener(this));

    vpMain.setOnPageChangeListener(this);
  }


  @Override
  protected void onResume() {
    super.onResume();
    Amplitude.startSession();
    SharedPreferences myPrefs = this.getSharedPreferences(SettingsActivity.SETTINGS, MODE_PRIVATE);
    boolean wifi_only = myPrefs.getBoolean("wifi", false);

    if(!Constants.isConnected(this, false)){
      new AlertDialog.Builder(this)
        .setTitle("No Internet Connection")
        .setMessage("You don't have internet connection. Enable internet and restart this application.")
        .setCancelable(false)
        .create().show();
    }
    else if(!Constants.isConnected(this, wifi_only)){
      new AlertDialog.Builder(this)
        .setTitle("Data Enabled")
        .setMessage("You have data enabled but you said you don't want to use data. Please disable data before using this application.")
        .setCancelable(false)
        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(MainAct.this, SettingsActivity.class);
            startActivity(intent);
          }
        }).create().show();
    }
    else {
      int quality = myPrefs.getInt("quality", 1);
      VIDEO_QUALITY = Constants.VIDEO_RESOLUTION_ID[quality];
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar actions click
    switch (item.getItemId()) {
      case R.id.menu_item_settings:{
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
      }
      default:{
        return super.onOptionsItemSelected(item);
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    MenuItem item = menu.findItem(R.id.menu_item_share);
    ShareActionProvider myShareActionProvider = new ShareActionProvider(this);
    MenuItemCompat.setActionProvider(item, myShareActionProvider);

    if(myShareActionProvider != null){
      myShareActionProvider.setShareIntent(Constants.setUpShareIntent(getString(R.string.share_main_message)));
    }
    return true;
  }



  @Override
  public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    vpMain.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

  }

  @Override
  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

  }

  @Override
  public void onPageScrolled(int i, float v, int i2) {
  }

  @Override
  public void onPageSelected(int i) {
    mActionBar.setSelectedNavigationItem(i);
  }

  @Override
  public void onPageScrollStateChanged(int i) {

  }




}
