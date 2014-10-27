package com.planauts.listadapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.planauts.activities.VideoPlayer;
import com.planauts.bean.PlaylistBean;
import com.planauts.wsj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by svaithia@uwaterloo.ca on 10/27/14.
 */

public class SpotlightListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
  private Context mContext;
  private List<PlaylistBean> mPlaylist;
  private List<String> mSections;

  public SpotlightListAdapter(Context context, List<String> sections, List<PlaylistBean> playlist) {
    this.mContext = context;
    this.mSections = sections;
    this.mPlaylist = playlist;
  }

  @Override
  public int getCount() {
    return Math.min(mSections.size(), mSections.size());
  }

  @Override
  public PlaylistBean getItem(int position) {
    return mPlaylist.get(position);
  }

  public String getSection(int position) {
    return mSections.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if(convertView==null){
      LayoutInflater layoutInflator = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
      convertView = layoutInflator.inflate(R.layout.spotlight_video_list_item, null);
    }
    TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
    TextView tvPubDate = (TextView) convertView.findViewById(R.id.tvPubDate);
    ImageView ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
    TextView tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);

    PlaylistBean item = getItem(position);
    String section = getSection(position);

    tvTitle.setText(item.title);
    tvPubDate.setText(item.pubDateFormatted());
    tvDuration.setText(item.durationFormatted());

    AQuery aq = new AQuery(ivThumbnail);
    aq.id(R.id.ivThumbnail).image(item.image);

    return convertView;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    ArrayList<PlaylistBean> playlistBeanArrayList = new ArrayList<PlaylistBean>();

    for(int i = position, end = position+getCount(); i<end; i++){
      PlaylistBean entry = getItem(i % (getCount()));
      playlistBeanArrayList.add(entry);
    }

    Intent videoPlaybackActivity = new Intent(mContext, VideoPlayer.class);
    videoPlaybackActivity.putParcelableArrayListExtra(VideoPlayer.INTENT_VIDEOS, playlistBeanArrayList);
    mContext.startActivity(videoPlaybackActivity);
  }

}
