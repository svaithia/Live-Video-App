package com.planauts.activities.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.planauts.activities.SettingsActivity;
import com.planauts.bean.PlaylistBean;
import com.planauts.common.Constants;
import com.planauts.listadapters.SpotlightListAdapter;
import com.planauts.scrapper.PlaylistScrapper;
import com.planauts.wsj.R;

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

    final SpotlightListAdapter spotlightListAdapter = new SpotlightListAdapter(getActivity());
    lvVideos.setAdapter(spotlightListAdapter);
    lvVideos.setOnItemClickListener(spotlightListAdapter);

    // URL 1
    for(int i = 0; i < Constants.SPOTLIGHT_URL.length; i++) {
      final int index = i;
      PlaylistScrapper playlistScrapper = new PlaylistScrapper(Constants.SPOTLIGHT_URL[index], quality, new PlaylistScrapper.Callback() {
        @Override
        public void success(List<PlaylistBean> playlistBeans) {
          progressbar.setVisibility(View.GONE);
          spotlightListAdapter.addSectionHeaderItem(new PlaylistBean(Constants.SPOTLIGHT_URL_ID[index], "", "", "", "", 0), false);
          spotlightListAdapter.addItem(playlistBeans, true);
        }

        @Override
        public void failure() {

        }
      });
      playlistScrapper.execute();
    }

  }
}
