package com.planauts.listadapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.androidquery.AQuery;
import com.planauts.activities.VideoPlayer;
import com.planauts.bean.PlaylistBean;
import com.planauts.wsj.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by svaithia@uwaterloo.ca on 10/27/14.
 */

public class SpotlightListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
  private static final int TYPE_ITEM = 0;
  private static final int TYPE_SEPARATOR = 1;

  private Context mContext;
  private ArrayList<PlaylistBean> mData;
  private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

  private LayoutInflater mLayoutInflater;

  public SpotlightListAdapter(Context context) {
    this.mContext = context;
    mLayoutInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    mData = new ArrayList<PlaylistBean>();
  }

  @Override
  public int getCount() {
    return mData.size();
  }

  @Override
  public PlaylistBean getItem(int position) {
    return mData.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }


  public void addItem(final List<PlaylistBean> items, boolean notify) {
    for(PlaylistBean item : items){
      mData.add(item);
    }
    if(notify){
      notifyDataSetChanged();
    }
  }

  public void addSectionHeaderItem(final PlaylistBean item, boolean notify) {
    mData.add(item);
    sectionHeader.add(mData.size() - 1);

    if(notify){
      notifyDataSetChanged();
    }
  }

  @Override
  public int getItemViewType(int position) {
    return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    int rowType = getItemViewType(position);

    if (convertView == null) {
      convertView = mLayoutInflater.inflate(R.layout.spotlight_video_list_item, null);
    }

    switch (rowType) {
      case TYPE_ITEM:
        convertView = mLayoutInflater.inflate(R.layout.spotlight_video_list_item, null);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.tvPubDate);
        TextView tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
        ImageView ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);

        PlaylistBean item = mData.get(position);
        tvTitle.setText(item.title);
        tvPubDate.setText(item.pubDateFormatted());
        tvDuration.setText(item.durationFormatted());


        AQuery aq = new AQuery(ivThumbnail);
        aq.id(R.id.ivThumbnail).image(item.image);

        break;
      case TYPE_SEPARATOR:
        convertView = mLayoutInflater.inflate(R.layout.snippet_item2, null);
        ((TextView) convertView.findViewById(R.id.textSeparator)).setText(mData.get(position).title);
        break;
    }


    return convertView;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    ArrayList<PlaylistBean> playlistBeanArrayList = new ArrayList<PlaylistBean>();

    for(int i = position, end = position+getCount(); i<end; i++){
      PlaylistBean entry = getItem(i % (getCount()));
      playlistBeanArrayList.add(entry);
    }

    JSONObject eventProperties = new JSONObject();
    try {
      eventProperties.put("video", getItem(position).url);
    } catch (JSONException exception) {}
    Amplitude.logEvent("Spotlight List Adapter Clicked Event", eventProperties);


    Intent videoPlaybackActivity = new Intent(mContext, VideoPlayer.class);
    videoPlaybackActivity.putParcelableArrayListExtra(VideoPlayer.INTENT_VIDEOS, playlistBeanArrayList);
    mContext.startActivity(videoPlaybackActivity);
  }

}