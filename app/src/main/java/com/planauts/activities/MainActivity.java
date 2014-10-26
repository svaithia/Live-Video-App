package com.planauts.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.planauts.bean.PlaylistBean;
import com.planauts.bean.SectionURLBean;
import com.planauts.common.Constants;
import com.planauts.listadapters.NavDrawerListAdapter;
import com.planauts.listadapters.VideoListAdapter;
import com.planauts.scrapper.PlaylistURLScrapper;
import com.planauts.scrapper.SectionURLScrapper;
import com.planauts.wsj.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  NavDrawerListAdapter navDrawerListAdapter;
  VideoListAdapter videoListAdapter;
  SectionURLScrapper sectionUrlScrapperObj;
  List<String> listDataHeader; //
  HashMap<String, List<String>> listDataChild; //
  private DrawerLayout dlNavDrawer;
  private ExpandableListView elvNav;
  private ListView lvVideos;
  private ActionBarDrawerToggle mDrawerToggle;
  // nav drawer title
  private CharSequence mDrawerTitle;
  // used to store app title
  private CharSequence mTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Amplitude.initialize(this, Constants.AMPLITUDE_API_KEY);
    setContentView(R.layout.activity_main);

    mTitle = mDrawerTitle = getTitle();

    dlNavDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    elvNav = (ExpandableListView) findViewById(R.id.elvSliderMenu);
    lvVideos = (ListView) findViewById(R.id.lvVideos);



    // enabling action bar app icon and behaving it as toggle button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    mDrawerToggle = new ActionBarDrawerToggle(this, dlNavDrawer,
      R.drawable.ic_drawer, R.string.nav_option_open, R.string.nav_option_close) {
      public void onDrawerClosed(View view) {
        getSupportActionBar().setTitle(mTitle);
        invalidateOptionsMenu();
      }

      public void onDrawerOpened(View drawerView) {
        getSupportActionBar().setTitle(mDrawerTitle);
        invalidateOptionsMenu();
      }
    };
    dlNavDrawer.setDrawerListener(mDrawerToggle);

    if (savedInstanceState == null) {
      loadVideosFromUrl(Constants.DEFAULT_CALLING_URL);
    }

    elvNav.setOnChildClickListener(new NavListItemClicked());

    lvVideos.setOnItemClickListener(new VideoListListener());

  }

  private void setUp(){
    prepareListData();
    navDrawerListAdapter = new NavDrawerListAdapter(this, listDataHeader, listDataChild);
    elvNav.setAdapter(navDrawerListAdapter);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Amplitude.startSession();

    if(!isNetworkAvailable()) {
      new AlertDialog.Builder(this)
        .setTitle("No Internet Connection")
        .setMessage("You don't have internet connection. Enable internet and restart this application.")
        .setCancelable(false)
        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            return;
          }
        }).create().show();
    }
    else {
      SharedPreferences myPrefs = this.getSharedPreferences("preference", MODE_PRIVATE);
      boolean wifi_only = myPrefs.getBoolean("wifi", false);

      if (wifi_only && isDataEnabled()){
        new AlertDialog.Builder(this)
          .setTitle("Data Enabled")
          .setMessage("You have data enabled but you said you don't want to use data. Please disable data before using this application.")
          .setCancelable(false)
          .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              return;
            }
          }).create().show();
      }
      else {
        int quality = myPrefs.getInt("quality", 1);
        VIDEO_QUALITY = Constants.VIDEO_RESOLUTION_ID[quality];
        setUp();
      }
    }
  }

  private int VIDEO_QUALITY = 360;

  private void loadVideosFromUrl(String url) {
    PlaylistURLScrapper playlistUrlScrapperObj = new PlaylistURLScrapper(url, VIDEO_QUALITY);
    playlistUrlScrapperObj.fetchXML();
    while (!playlistUrlScrapperObj.parsingComplete()) ;
    List<PlaylistBean> playList = playlistUrlScrapperObj.playlistUrlBeans();
    videoListAdapter = new VideoListAdapter(this, playList);
    lvVideos.setAdapter(videoListAdapter);
  }

  private LinkedHashMap<String, String> returnUrlMapBasedOnUrl(int groupPosition) {
    switch (groupPosition) {
      case 0:
        return sectionUrlScrapperObj.sectionUrlBeans().vod;
      case 1:
        return sectionUrlScrapperObj.sectionUrlBeans().program;
      case 2:
        return sectionUrlScrapperObj.sectionUrlBeans().special;
      default:
        return null;
    }
  }

  /**
   * Called when invalidateOptionsMenu() is triggered
   */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    // if nav drawer is opened, hide the action items
    boolean drawerOpen = dlNavDrawer.isDrawerOpen(elvNav);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    getSupportActionBar().setTitle(mTitle);
  }

  /**
   * When using the ActionBarDrawerToggle, you must call it during
   * onPostCreate() and onConfigurationChanged()...
   */

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Pass any configuration change to the drawer toggls
    mDrawerToggle.onConfigurationChanged(newConfig);
  }



  private void prepareListData() {
    sectionUrlScrapperObj = new SectionURLScrapper();
    sectionUrlScrapperObj.fetchXML();
    while (!sectionUrlScrapperObj.parsingComplete()) ;
    SectionURLBean sectionUrlBeanObj = sectionUrlScrapperObj.sectionUrlBeans();

    listDataHeader = new ArrayList<String>();
    listDataChild = new HashMap<String, List<String>>();

    // Adding child data
    listDataHeader.add("Videos");
    listDataHeader.add("Programs");
    listDataHeader.add("Specials");

    listDataChild.put(listDataHeader.get(0), new ArrayList<String>(sectionUrlBeanObj.vod.keySet())); // Header, Child data
    listDataChild.put(listDataHeader.get(1), new ArrayList<String>(sectionUrlBeanObj.program.keySet()));
    listDataChild.put(listDataHeader.get(2), new ArrayList<String>(sectionUrlBeanObj.special.keySet()));
  }

  private class VideoListListener implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

      int arrSize = videoListAdapter.getCount() - position;
      String[] resourceUrls = new String[arrSize];
      String[] resourceTitles = new String[arrSize];

      for (int i = 0; i < arrSize; i++) {
        PlaylistBean itemClicked = videoListAdapter.getItem(i + position);
        resourceUrls[i] = itemClicked.url;
        resourceTitles[i] = itemClicked.title;
      }

      Intent videoPlaybackActivity = new Intent(getApplicationContext(), VideoPlayer.class);
      videoPlaybackActivity.putExtra("videoPlaylistUrls", resourceUrls);
      videoPlaybackActivity.putExtra("videoPlaylistTitltes", resourceTitles);
      startActivity(videoPlaybackActivity);
    }
  }

  //TODO http://stackoverflow.com/questions/8315855/expandablelistview-keep-selected-child-in-a-pressed-state
  private class NavListItemClicked implements OnChildClickListener {
    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {

      String parentItem = listDataHeader.get(groupPosition);
      String childItem = listDataChild.get(parentItem).get(childPosition);

      if (childItem.equalsIgnoreCase("Schedule") || childItem.equalsIgnoreCase("Descriptions") ||
        childItem.equalsIgnoreCase("Search") || childItem.equalsIgnoreCase("Program List") ||
        childItem.equalsIgnoreCase("Radio") || childItem.equalsIgnoreCase("Property Search")) {
        dlNavDrawer.closeDrawer(elvNav);
        Toast.makeText(getApplicationContext(), "Sorry! Cannot select " + childItem + " for now." +
          "I will implement this feature soon!", Toast.LENGTH_SHORT).show();
        return false;
      }

      LinkedHashMap<String, String> map = returnUrlMapBasedOnUrl(groupPosition);

      String url = map.get(childItem);

      loadVideosFromUrl(url);
      setTitle(childItem);

      parent.setItemChecked(groupPosition + childPosition + 1, true);

      dlNavDrawer.closeDrawer(elvNav);

      return false;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    MenuItem item = menu.findItem(R.id.menu_item_share);
    ShareActionProvider myShareActionProvider = new ShareActionProvider(this);
    MenuItemCompat.setActionProvider(item, myShareActionProvider);

    if(myShareActionProvider != null){
      myShareActionProvider.setShareIntent(setUpShareIntent());
    }
    return true;
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
      = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  private boolean isDataEnabled(){
    boolean mobileDataEnabled = false; // Assume disabled
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    try {
      Class cmClass = Class.forName(cm.getClass().getName());
      Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
      method.setAccessible(true); // Make the method callable
      // get the setting for "mobile data"
      mobileDataEnabled = (Boolean)method.invoke(cm);
    } catch (Exception e) {
      // Some problem accessible private API
      // TODO do whatever error handling you want here
    }
    return mobileDataEnabled;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // toggle nav drawer on selecting action bar app icon/title
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
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

  private Intent setUpShareIntent() {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_main_message));
    return shareIntent;
  }


}
    
