package com.planauts.listadapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.planauts.activities.PlaylistActivity;
import com.planauts.bean.SectionBean;
import com.planauts.wsj.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class SectionListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
  private static final int TYPE_ITEM = 0;
  private static final int TYPE_SEPARATOR = 1;

  private ArrayList<SectionBean> mData = new ArrayList<SectionBean>();
  private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

  private LayoutInflater mInflater;
  private Context mContext;

  public SectionListAdapter(Context context) {
    mContext = context;
    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public void addMap(final HashMap<String, List<SectionBean>> sectionURLBean){
    for(String key : sectionURLBean.keySet()){
      addSectionHeaderItem(new SectionBean(key, ""));
      List<SectionBean> value = sectionURLBean.get(key);
      for(SectionBean sectionBean : value){
        addItem(sectionBean);
      }
    }
  }


  public void addItem(final SectionBean item) {
    mData.add(item);
    notifyDataSetChanged();
  }

  public void addSectionHeaderItem(final SectionBean item) {
    mData.add(item);
    sectionHeader.add(mData.size() - 1);
    notifyDataSetChanged();
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
  public int getCount() {
    return mData.size();
  }

  @Override
  public SectionBean getItem(int position) {
    return mData.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;
    int rowType = getItemViewType(position);

    if (convertView == null) {
      holder = new ViewHolder();
      switch (rowType) {
        case TYPE_ITEM:
          convertView = mInflater.inflate(R.layout.snippet_item1, null);
          holder.textView = (TextView) convertView.findViewById(R.id.text);
          break;
        case TYPE_SEPARATOR:
          convertView = mInflater.inflate(R.layout.snippet_item2, null);
          holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
          break;
      }
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.textView.setText(mData.get(position).name);

    return convertView;
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    int rowType = getItemViewType(position);
    if(rowType == TYPE_ITEM){
      SectionBean item = mData.get(position);
      Intent i = new Intent(mContext, PlaylistActivity.class);
      i.putExtra("selected", item);
      mContext.startActivity(i);
    }
  }

  public static class ViewHolder {
    public TextView textView;
  }

}