package com.planauts.listadapters;

import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.planauts.bean.PlaylistBean;
import com.planauts.wsj.R;

public class VideoListAdapter extends BaseAdapter {
	private Context _context;
	private List<PlaylistBean> _listPlaylist;
	
	public VideoListAdapter(Context context, List<PlaylistBean> playList) {
		this._context = context;
		this._listPlaylist = playList;
	}
	
	@Override
	public int getCount() {
		return _listPlaylist.size();
	}

	@Override
	public PlaylistBean getItem(int position) {
		return _listPlaylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater layoutInflator = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflator.inflate(R.layout.video_list_item, null);
		}

		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		TextView tvPubDate = (TextView) convertView.findViewById(R.id.tvPubDate);
		ImageView ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
		TextView tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
		
		PlaylistBean item = getItem(position);
		tvTitle.setText(item.title());
		tvPubDate.setText(item.pubDate());
		
		AQuery aq = new AQuery(ivThumbnail);
		aq.id(R.id.ivThumbnail).image(item.image());
		
		tvDuration.setText(item.durationFormatted());
		
		return convertView;
	}
	

}
