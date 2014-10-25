package com.planauts.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.planauts.bean.SectionBean;
import com.planauts.wsj.R;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class PlaylistActivity extends ActionBarActivity {
  public static final String TAG = PlaylistActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_playlist);

    Bundle bundle = getIntent().getExtras();
    SectionBean sectionBean = bundle.getParcelable("selected");
    Log.e(TAG, sectionBean.name + " " + sectionBean.url);

  }



}
