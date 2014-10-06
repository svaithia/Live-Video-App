package com.planauts.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.VideoView;

import com.amplitude.api.Amplitude;
import com.planauts.wsj.R;

public class VideoPlayer extends ActionBarActivity implements OnCompletionListener, OnPreparedListener {

  private VideoView vvPlayer;
  private VideoControl vidControl;
  private int currentPosition = 0;
  private String fileRes[];
  private String videoTitles[];
  private Handler actionBarHideHandler;

  @Override
  public void onCreate(Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.videoplayer);
    vvPlayer = (VideoView) findViewById(R.id.myvideoview);

    Bundle e = getIntent().getExtras();
    if(e != null){
      fileRes = e.getStringArray("videoPlaylistUrls");
      videoTitles = e.getStringArray("videoPlaylistTitltes");
    }

    vvPlayer.setOnCompletionListener(this);
    vvPlayer.setOnPreparedListener(this);
    vidControl = new VideoControl(this);
    vidControl.setPrevNextListeners(new nextOnClickListener(), new prevOnClickListener());
    vidControl.setAnchorView(vvPlayer);
    vvPlayer.setMediaController(vidControl);
    vvPlayer.requestFocus();
    vidControl.requestFocus();

    if (!playFileRes()) return;
//
//    LinearLayout llPlayerContainer = (LinearLayout) findViewById(R.id.llPlayerContainer);
//    llPlayerContainer.setOnTouchListener(new OnTouchListener() {
//      @Override
//      public boolean onTouch(View v, MotionEvent event) {
//        toggleActionBarVisiblity();
//        Log.e("WTFF", "WTFF");
//        return true;
//      }
//    });
  }

  @Override
  public void onBackPressed() {
    if(getSupportActionBar().isShowing() || vidControl.isShowing()){
      vidControl.hide();
    }
    else {
      finish();
    }
  }



  @Override
  public void onPrepared(MediaPlayer mp) {
    vidControl.show();
  }

  private boolean playFileRes() {
    if (fileRes == null) {
      stopPlaying();
      return false;
    }
    else {
      String url = fileRes[currentPosition];
      getSupportActionBar().setTitle(videoTitles[currentPosition]);

      vvPlayer.setVideoURI(Uri.parse(url));
      vvPlayer.start();
      return true;
    }
  }

  public void stopPlaying() {
    vvPlayer.stopPlayback();
    this.finish();
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    if (++currentPosition < fileRes.length) playFileRes();
    else finish();
  }

  @Override
  protected void onResume() {
    super.onResume();
    Amplitude.startSession();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.video_player, menu);
    MenuItem item = menu.findItem(R.id.menu_item_share);
    ShareActionProvider myShareActionProvider = new ShareActionProvider(this);
    MenuItemCompat.setActionProvider(item, myShareActionProvider);

    if(myShareActionProvider != null)
      myShareActionProvider.setShareIntent(setUpShareIntent());
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_share:
        if (actionBarHideHandler != null) {
          actionBarHideHandler.removeCallbacksAndMessages(null);
        }
        break;
    }
    return true;
  }

  private Intent setUpShareIntent() {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_default_message, videoTitles[currentPosition]));
    return shareIntent;
  }

  private class nextOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      vvPlayer.stopPlayback();
      currentPosition = (currentPosition + 1) % (fileRes.length);
      playFileRes();
    }
  }

  private class prevOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      vvPlayer.stopPlayback();
      currentPosition = (currentPosition - 1) % (fileRes.length);
      playFileRes();
    }
  }

}