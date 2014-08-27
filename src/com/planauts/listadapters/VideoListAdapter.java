package com.planauts.listadapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
		tvTitle.setText(getItem(position).title());
		
		return convertView;
	}

}
