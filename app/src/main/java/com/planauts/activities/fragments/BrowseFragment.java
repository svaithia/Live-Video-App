package com.planauts.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.planauts.bean.SectionBean;
import com.planauts.listadapters.SectionListAdapter;
import com.planauts.scrapper.SectionScrapper;
import com.planauts.wsj.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class BrowseFragment extends Fragment implements SectionScrapper.Callback{
  private ListView lvSectionList;
  private ProgressBar progressbar;

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_browse, container, false);
    lvSectionList = (ListView) view.findViewById(R.id.lvSectionList);
    progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    SectionScrapper sectionScrapper = new SectionScrapper(this);
    sectionScrapper.execute();
  }

  @Override
  public void success(HashMap<String, List<SectionBean>> sectionURLBean) {
    progressbar.setVisibility(View.GONE);
    SectionListAdapter sectionListAdapter = new SectionListAdapter(getActivity());
    sectionListAdapter.addMap(sectionURLBean);
    lvSectionList.setAdapter(sectionListAdapter);
    lvSectionList.setOnItemClickListener(sectionListAdapter);
  }

  @Override
  public void failure() {

  }



}
