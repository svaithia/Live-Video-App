package com.planauts.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planauts.wsj.R;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class SpotlightFragment  extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_spotlight, container, false);
  }
}
