package com.planauts.wsj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.Toast;

import com.planauts.bean.PlaylistBean;
import com.planauts.bean.SectionURLBean;
import com.planauts.listadapters.NavDrawerListAdapter;
import com.planauts.listadapters.VideoListAdapter;
import com.planauts.scrapper.PlaylistURLScrapper;
import com.planauts.scrapper.SectionURLScrapper;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ExpandableListView elvNav;
    private ListView lvVideos;
    private ActionBarDrawerToggle mDrawerToggle;

	NavDrawerListAdapter navDrawerListAdapter;
	VideoListAdapter videoListAdapter;
	
    // nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;

	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        elvNav = (ExpandableListView) findViewById(R.id.elvSliderMenu);
 
        prepareListData();
        navDrawerListAdapter = new NavDrawerListAdapter(this, listDataHeader, listDataChild);
        elvNav.setAdapter(navDrawerListAdapter);
        
        lvVideos = (ListView) findViewById(R.id.lvVideos);
        videoListTest();
        lvVideos.setAdapter(videoListAdapter);
        
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        if (savedInstanceState == null) {
            // on first time display view for first nav item
//            displayView(0);
        }
        elvNav.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                     int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), groupPosition + " " + childPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        
        // Listview Group expanded listener
        elvNav.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), groupPosition + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
        
     // Listview Group collasped listener
        elvNav.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), groupPosition + " Collapsed",
                        Toast.LENGTH_SHORT).show();
         
            }
        });
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
        case R.id.action_settings:
            return true;
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
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(elvNav);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
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
//            mDrawerLayout.closeDrawer(mDrawerList);
//        } else {
//            // error in creating fragment
//            Log.e("MainActivity", "Error in creating fragment");
//        }
    }
    
	private void prepareListData(){
		SectionURLScrapper sectionUrlScrapperObj = new SectionURLScrapper();
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
        listDataChild.put(listDataHeader.get(2), new ArrayList<String>(sectionUrlBeanObj.program.keySet()));
	}
	
	private void videoListTest(){
		PlaylistURLScrapper playlistUrlScrapperObj = new PlaylistURLScrapper("",0);
		playlistUrlScrapperObj.fetchXML();
		while(!playlistUrlScrapperObj.parsingComplete());
		List<PlaylistBean> playList = playlistUrlScrapperObj.playlistUrlBeans();
		videoListAdapter = new VideoListAdapter(getApplicationContext(), playList);
	}
}
    
