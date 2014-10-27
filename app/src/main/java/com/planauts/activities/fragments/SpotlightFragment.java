package com.planauts.activities.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.planauts.activities.SettingsActivity;
import com.planauts.bean.PlaylistBean;
import com.planauts.common.Constants;
import com.planauts.listadapters.SpotlightListAdapter;
import com.planauts.listadapters.VideoListAdapter;
import com.planauts.scrapper.PlaylistScrapper;
import com.planauts.wsj.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class SpotlightFragment  extends Fragment {
  private ProgressBar progressbar;
  private ListView lvVideos;
  private String TAG = SpotlightFragment.class.getSimpleName();

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_spotlight, container, false);
    progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
    lvVideos = (ListView) view.findViewById(R.id.lvVideos);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    SharedPreferences sharedpreferences =
      getActivity().getSharedPreferences(SettingsActivity.SETTINGS, Context.MODE_PRIVATE);
    int quality_index = sharedpreferences.getInt(SettingsActivity.QUALITY, 1);
    int quality = Constants.VIDEO_RESOLUTION_ID[quality_index];

    // URL 1
    for(int i = 0; i < 1; i++) {
      PlaylistScrapper playlistScrapper = new PlaylistScrapper(Constants.SPOTLIGHT_URL[i], quality, new PlaylistScrapper.Callback() {
        @Override
        public void success(List<PlaylistBean> playlistBeans) {
          Log.e(TAG, "SUCCESS " + playlistBeans.size());
          progressbar.setVisibility(View.GONE);
          VideoListAdapter spotlightListAdapter = new VideoListAdapter(getActivity(), playlistBeans);
//          SpotlightListAdapter spotlightListAdapter = new SpotlightListAdapter(getActivity(), Arrays.asList(Constants.SPOTLIGHT_URL_ID), playlistBeans);
          lvVideos.setAdapter(spotlightListAdapter);
          lvVideos.setOnItemClickListener(spotlightListAdapter);
        }

        @Override
        public void failure() {

        }
      });
      playlistScrapper.execute();
    }

  }
}
