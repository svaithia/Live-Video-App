package com.planauts.activities;

import android.support.v7.app.ActionBarActivity;
import android.widget.MediaController;

/**
 * Created by eight on 10/5/14.
 */
public class VideoControl extends MediaController {
  private ActionBarActivity activity;
  public VideoControl(ActionBarActivity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public void hide() {
    super.hide();
    activity.getSupportActionBar().hide();
  }

  @Override
  public void show(){
    super.show();
    activity.getSupportActionBar().show();
  }


//  private void toggleActionBarVisiblity() {
//    if (getActionBar().isShowing() || vidControl.isShowing()) {
//      Log.e("T", getActionBar().isShowing() + " " + vidControl.isShowing());
//      getActionBar().hide();
//      if (actionBarHideHandler != null) {
//        actionBarHideHandler.removeCallbacksAndMessages(null);
//      }
//    }
//    else {
//      getActionBar().show();
//      actionBarHideHandler = new Handler();
//      actionBarHideHandler.postDelayed(new Runnable() {
//        @Override
//        public void run() {
//          getActionBar().hide();
//        }
//      }, 3000);
//    }
//  }

}
