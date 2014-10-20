package com.planauts.activities;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.MediaController;

/**
 * Created by eight on 10/5/14.
 */
public class VideoControl extends MediaController {
  private ActionBarActivity activity;
  public boolean menu_open;

  public VideoControl(ActionBarActivity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public void hide() {
    super.hide();
    if(!menu_open) {
      activity.getSupportActionBar().hide();
    }
  }

  @Override
  public void show(){
    super.show();
    activity.getSupportActionBar().show();
  }
}
