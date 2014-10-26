package com.planauts.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

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
  private ProgressBar progressbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_playlist);

    lvVideos = (ListView) findViewById(R.id.lvVideos);
    progressbar = (ProgressBar) findViewById(R.id.progressbar);

    Bundle bundle = getIntent().getExtras();
    SectionBean sectionBean = bundle.getParcelable("selected");

    getSupportActionBar().setTitle(sectionBean.name);

    SharedPreferences sharedpreferences =
      getSharedPreferences(SettingsActivity.SETTINGS, Context.MODE_PRIVATE);
    int quality_index = sharedpreferences.getInt(SettingsActivity.QUALITY, 1);
    int quality = Constants.VIDEO_RESOLUTION_ID[quality_index];

    PlaylistScrapper playlistScrapper = new PlaylistScrapper(sectionBean.url, quality, this);
    playlistScrapper.execute();

  }

  @Override
  public void success(List<PlaylistBean> playlistBeans) {
    progressbar.setVisibility(View.GONE);
    VideoListAdapter videoListAdapter = new VideoListAdapter(this, playlistBeans);
    lvVideos.setAdapter(videoListAdapter);
    lvVideos.setOnItemClickListener(videoListAdapter);
  }

  @Override
  public void failure() {

  }
}
