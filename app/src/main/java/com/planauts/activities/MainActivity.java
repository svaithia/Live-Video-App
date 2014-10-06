package com.planauts.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
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

public class MainActivity extends Activity {
    private DrawerLayout dlNavDrawer;
    private ExpandableListView elvNav;
    private ListView lvVideos;
    private ActionBarDrawerToggle mDrawerToggle;

	NavDrawerListAdapter navDrawerListAdapter;
	VideoListAdapter videoListAdapter;
	
	SectionURLScrapper sectionUrlScrapperObj;
	
    // nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;

	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Amplitude.initialize(this,Constants.AMPLITUDE_API_KEY);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        
        dlNavDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        elvNav = (ExpandableListView) findViewById(R.id.elvSliderMenu);
        lvVideos = (ListView) findViewById(R.id.lvVideos);
 
        prepareListData();
        navDrawerListAdapter = new NavDrawerListAdapter(this, listDataHeader, listDataChild);
        elvNav.setAdapter(navDrawerListAdapter);
        
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, dlNavDrawer,
                R.drawable.ic_drawer, R.string.nav_option_open, R.string.nav_option_close){
		            public void onDrawerClosed(View view) {
		                getActionBar().setTitle(mTitle);
		                invalidateOptionsMenu();
		            }
		 
		            public void onDrawerOpened(View drawerView) {
		                getActionBar().setTitle(mDrawerTitle);
		                invalidateOptionsMenu();
		            }
        		};
        dlNavDrawer.setDrawerListener(mDrawerToggle);
        
        if (savedInstanceState == null) {
        	loadVideosFromUrl(Constants.DEFAULT_CALLING_URL);
        }
        
        elvNav.setOnChildClickListener(new NavListItemClicked());
//        elvNav.setOnGroupExpandListener(new NavListGroupExpand());
//        elvNav.setOnGroupCollapseListener(new NavListGroupCollapse());
        
        lvVideos.setOnItemClickListener(new VideoListListener());
        
    }
    
    private class VideoListListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			 
			 int arrSize = videoListAdapter.getCount() - position;
			 String[] resourceUrls = new String[arrSize];
			 String[] resourceTitles = new String[arrSize];
			 
			 for(int i = 0; i<arrSize; i++){
				 PlaylistBean itemClicked = videoListAdapter.getItem(i+position);
				 resourceUrls[i] = itemClicked.url();
				 resourceTitles[i] = itemClicked.title();
			 }
			 
			Intent videoPlaybackActivity = new Intent(getApplicationContext(), VideoPlayer.class);
			videoPlaybackActivity.putExtra("videoPlaylistUrls", resourceUrls);
			videoPlaybackActivity.putExtra("videoPlaylistTitltes", resourceTitles);
 		    startActivity(videoPlaybackActivity);
			 
			
		}
    }

    @Override
    protected void onResume() {
        super.onResume();
        Amplitude.startSession();
    }

    //TODO http://stackoverflow.com/questions/8315855/expandablelistview-keep-selected-child-in-a-pressed-state
    private class NavListItemClicked implements OnChildClickListener{
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {

			String parentItem = listDataHeader.get(groupPosition);
			String childItem = listDataChild.get(parentItem).get(childPosition);
			
			if(childItem.equalsIgnoreCase("Schedule") || childItem.equalsIgnoreCase("Descriptions") ||
					childItem.equalsIgnoreCase("Search") || childItem.equalsIgnoreCase("Program List") ||
					childItem.equalsIgnoreCase("Radio") || childItem.equalsIgnoreCase("Property Search")){
				dlNavDrawer.closeDrawer(elvNav);
				Toast.makeText(getApplicationContext(),"Sorry! Cannot select " + childItem + " for now." +
						"I will implement this feature soon!", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			LinkedHashMap<String, String> map = returnUrlMapBasedOnUrl(groupPosition);
			
			String url = map.get(childItem);
			
			loadVideosFromUrl(url);
			setTitle(childItem);
			
		    parent.setItemChecked(groupPosition+childPosition+1, true);
			
			dlNavDrawer.closeDrawer(elvNav);
			
			return false;
		}
    }

    private void loadVideosFromUrl(String url){
    	PlaylistURLScrapper playlistUrlScrapperObj = new PlaylistURLScrapper(url,360);
		playlistUrlScrapperObj.fetchXML();
		while(!playlistUrlScrapperObj.parsingComplete());
		List<PlaylistBean> playList = playlistUrlScrapperObj.playlistUrlBeans();
		videoListAdapter = new VideoListAdapter(getApplicationContext(), playList);
		lvVideos.setAdapter(videoListAdapter);
    }
    
    
    private LinkedHashMap<String, String> returnUrlMapBasedOnUrl(int groupPosition){
    	switch(groupPosition){
		case 0:
			return  sectionUrlScrapperObj.sectionUrlBeans().vod;
		case 1:
			return sectionUrlScrapperObj.sectionUrlBeans().program;
		case 2:
			return sectionUrlScrapperObj.sectionUrlBeans().special;
		default:
			return null;
		}
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    /***
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
        getActionBar().setTitle(mTitle);
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
 
     /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
//            fragment = new HomeFragment();
            break;
        case 1:
//            fragment = new FindPeopleFragment();
            break;
        case 2:
//            fragment = new PhotosFragment();
            break;
        case 3:
//            fragment = new CommunityFragment();
            break;
        case 4:
//            fragment = new PagesFragment();
            break;
        case 5:
//            fragment = new WhatsHotFragment();
            break;
 
        default:
            break;
        }
 
//        if (fragment != null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_container, fragment).commit();
// 
//            // update selected item and title, then close the drawer
//            mDrawerList.setItemChecked(position, true);
//            mDrawerList.setSelection(position);
//            setTitle(navMenuTitles[position]);
//            dlNavDrawer.closeDrawer(mDrawerList);
//        } else {
//            // error in creating fragment
//            Log.e("MainActivity", "Error in creating fragment");
//        }
    }
    
	private void prepareListData(){
		sectionUrlScrapperObj = new SectionURLScrapper();
		sectionUrlScrapperObj.fetchXML();
		while(!sectionUrlScrapperObj.parsingComplete());
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
	

}
    
