package com.planauts.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import com.planauts.bean.PlaylistBean;
import com.planauts.bean.SectionBean;
import com.planauts.common.Constants;
import com.planauts.listadapters.VideoListAdapter;
import com.planauts.scrapper.PlaylistScrapper;
import com.planauts.wsj.R;

import java.util.List;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class PlaylistActivity extends ActionBarActivity implements PlaylistScrapper.Callback{
  public static final String TAG = PlaylistActivity.class.getSimpleName();
  private ListView lvVideos;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_playlist);

    lvVideos = (ListView) findViewById(R.id.lvVideos);

    Bundle bundle = getIntent().getExtras();
    SectionBean sectionBean = bundle.getParcelable("selected");

    SharedPreferences sharedpreferences =
      getSharedPreferences(SettingsActivity.SETTINGS, Context.MODE_PRIVATE);
    int quality_index = sharedpreferences.getInt(SettingsActivity.QUALITY, 1);
    int quality = Constants.VIDEO_RESOLUTION_ID[quality_index];

    PlaylistScrapper playlistScrapper = new PlaylistScrapper(sectionBean.url, quality, this);
    playlistScrapper.execute();

  }

  @Override
  public void success(List<PlaylistBean> playlistBeans) {
    VideoListAdapter videoListAdapter = new VideoListAdapter(getApplicationContext(), playlistBeans);
    lvVideos.setAdapter(videoListAdapter);
    lvVideos.setOnItemClickListener(videoListAdapter);
  }

  @Override
  public void failure() {

  }
}
