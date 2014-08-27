package com.planauts.slidingmenu.adapter;

import java.util.ArrayList;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.planauts.slidingmenu.model.NavDrawerItem;
import com.planauts.wsj.R;
 
public class aa extends BaseAdapter {
    private ArrayList<NavDrawerItem> strings;
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
 
	private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
 
	private LayoutInflater mInflater;
     
    public aa(Context context){
    	strings = new ArrayList<NavDrawerItem>();
        this.mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }
    
    public void addItem(final NavDrawerItem item) {
    	strings.add(item);
		notifyDataSetChanged();
	}
 
	public void addSectionHeaderItem(final NavDrawerItem item) {
		strings.add(item);
		sectionHeader.add(strings.size() - 1);
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
        return strings.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return strings.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	int rowType = getItemViewType(position);

        switch(rowType){
    	case TYPE_ITEM:
    		if(convertView==null){
    			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
    		}
    		convertView = getViewHelperItem(position, convertView, parent);
    		break;
    	case TYPE_SEPARATOR:
    		if(convertView==null){
    			convertView = mInflater.inflate(R.layout.drawer_section_list_item, null);
    		}
    		convertView = getViewHelperSeperator(position, convertView, parent);
    		break;
    	}
        return convertView;
    }
    
    private View getViewHelperItem(int position, View convertView, ViewGroup parent) {
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        
        txtTitle.setText(strings.get(position).getTitle());
//         
//        // displaying count
//        // check whether it set visible or not
//        if(strings.get(position).getCounterVisibility()){
//            txtCount.setText(strings.get(position).getCount());
//        }else{
//            // hide the counter view
//            txtCount.setVisibility(View.GONE);
//        }
    	
    	return convertView;
    }
    
    private View getViewHelperSeperator(int position, View convertView, ViewGroup parent) {
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(strings.get(position).getTitle());
        
        return convertView;
    }
 
}