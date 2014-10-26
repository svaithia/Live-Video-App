package com.planauts.activities;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.VideoView;

import com.amplitude.api.Amplitude;
import com.planauts.bean.PlaylistBean;
import com.planauts.common.Constants;
import com.planauts.wsj.R;

import java.util.List;

public class VideoPlayer extends ActionBarActivity implements OnCompletionListener, OnPreparedListener, MediaPlayer.OnErrorListener {

  public static final String INTENT_VIDEOS = "VIDEOS";

  private static final String TAG = VideoPlayer.class.getSimpleName();
  private VideoView vvPlayer;
  private VideoControl vidControl;
  private int currentPosition = 0;
  private PlaylistBean[] playlistArray;

  @Override
  public void onCreate(Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.videoplayer);
    vvPlayer = (VideoView) findViewById(R.id.myvideoview);

    Bundle e = getIntent().getExtras();
    if(e != null){
      List<PlaylistBean> playlistArr = e.getParcelableArrayList(INTENT_VIDEOS);
      playlistArray = playlistArr.toArray(new PlaylistBean[playlistArr.size()]);
    }

    vvPlayer.setOnCompletionListener(this);
    vvPlayer.setOnPreparedListener(this);
    vidControl = new VideoControl(this);
    vidControl.setPrevNextListeners(new nextOnClickListener(), new prevOnClickListener());
    vidControl.setAnchorView(vvPlayer);
    vvPlayer.setMediaController(vidControl);
    vvPlayer.requestFocus();
    vidControl.requestFocus();
    vvPlayer.setOnErrorListener(this);

    if (!playFileRes()) return;

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
    if (playlistArray == null) {
      vvPlayer.stopPlayback();
      finish();
      return false;
    }
    else {
      PlaylistBean item = playlistArray[currentPosition];
      String url = item.url;
      getSupportActionBar().setTitle(item.title);

      vvPlayer.stopPlayback();
      vvPlayer.setVideoURI(Uri.parse(url));
      vvPlayer.start();
      return true;
    }
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    if (++currentPosition < playlistArray.length) playFileRes();
    else finish();
  }

  @Override
  protected void onResume() {
    super.onResume();
    Amplitude.startSession();
  }

  @Override
  public void onOptionsMenuClosed(Menu menu) {
    super.onOptionsMenuClosed(menu);
    vidControl.menu_open = false;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.video_player, menu);
    MenuItem item = menu.findItem(R.id.menu_item_share);
    ShareActionProvider myShareActionProvider = new ShareActionProvider(this);
    MenuItemCompat.setActionProvider(item, myShareActionProvider);

    if(myShareActionProvider != null){
      String currentTitle = playlistArray[currentPosition].title;
      myShareActionProvider.setShareIntent(Constants.setUpShareIntent(getString(R.string.share_default_message, currentTitle)));
      myShareActionProvider.setSubUiVisibilityListener(new ActionProvider.SubUiVisibilityListener() {
        @Override
        public void onSubUiVisibilityChanged(boolean b) {
          vidControl.menu_open = b;
          if(!b){
            vidControl.show();
          }
        }
      });
    }
    return true;
  }

  @Override
  public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
    Log.e(TAG, "onError MediaPlayer");
    Amplitude.logEvent("onError MediaPlayer");
    return false;
  }

  private class nextOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      vvPlayer.stopPlayback();
      currentPosition = (currentPosition + 1) % (playlistArray.length);
      playFileRes();
    }
  }

  private class prevOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      vvPlayer.stopPlayback();
      currentPosition = (currentPosition - 1) % (playlistArray.length);
      playFileRes();
    }
  }

}