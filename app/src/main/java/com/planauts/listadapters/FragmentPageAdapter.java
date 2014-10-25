package com.planauts.listadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.planauts.activities.fragments.BrowseFragment;
import com.planauts.activities.fragments.SpotlightFragment;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class FragmentPageAdapter extends FragmentPagerAdapter {
  public FragmentPageAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {
    Fragment fragment = null;
    switch(i){
      case 0: {
        fragment = new SpotlightFragment();
        break;
      }
      case 1: {
        fragment = new BrowseFragment();
        break;
      }

    }
    return fragment;
  }

  @Override
  public int getCount() {
    return 2;
  }
}
