package com.planauts.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ShareActionProvider;
import android.widget.VideoView;

import com.planauts.wsj.R;

public class VideoPlayer extends Activity implements OnCompletionListener,OnPreparedListener {
    
    private VideoView vvPlayer;
    private MediaController vidControl;
    private int currentPosition;
    private String fileRes[];
    private String videoTitles[];
    
    private Handler actionBarHideHandler;
    private ShareActionProvider mShareActionProvider;
          
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.videoplayer);
        currentPosition = 0;
            
        Bundle e = getIntent().getExtras();
        fileRes = (e!=null) ? e.getStringArray("videoPlaylistUrls") : null;
        videoTitles = (e!=null) ? e.getStringArray("videoPlaylistTitltes") : null;
 
        vvPlayer = (VideoView)findViewById(R.id.myvideoview);
        vvPlayer.setOnCompletionListener(this);
        vvPlayer.setOnPreparedListener(this);
        
        vidControl = new MediaController(this){
            public boolean dispatchKeyEvent(KeyEvent event){
            	boolean backButtonPressed = event.getKeyCode() == KeyEvent.KEYCODE_BACK;
            	if(backButtonPressed){
	                if (vidControl.isShowing() || getActionBar().isShowing()){
	                	vidControl.hide();
	                	getActionBar().hide();
	                }else{
	                	((Activity) getContext()).finish();
	                }
            	}

                return super.dispatchKeyEvent(event);
            }
        };
        vidControl.setPrevNextListeners(new nextOnClickListener(), new prevOnClickListener());
        
        vidControl.setAnchorView(vvPlayer);
        vvPlayer.setMediaController(vidControl);
        
        if (!playFileRes()) return;
        
        LinearLayout llPlayerContainer = (LinearLayout)findViewById(R.id.llPlayerContainer);
        llPlayerContainer.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				toggleActionBarVisiblity();
				return false;
			}
        });
        getActionBar().hide();
        
    }
             
    private boolean playFileRes() {
        if (fileRes==null) {
            stopPlaying();
            return false;
        } else {
        	String url = fileRes[currentPosition];
        	
        	getActionBar().setTitle(videoTitles[currentPosition]);
        	 
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
    	if(++currentPosition < fileRes.length){
        	playFileRes();
    	}
    	else{
            finish();
    	}
    }
    
    private class nextOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			vvPlayer.stopPlayback();
			currentPosition = (currentPosition+1)%(fileRes.length);
	    	playFileRes();
		}
    }
    
    private class prevOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			vvPlayer.stopPlayback();
			currentPosition = (currentPosition-1)%(fileRes.length);
	    	playFileRes();
		}
    }
             
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
    }
	
	private void toggleMediaControlsVisiblity() {
        if (vidControl.isShowing()) { 
            vidControl.hide();
        } else {
            vidControl.show();
        }
    }
	
	private void toggleActionBarVisiblity() {
        if (getActionBar().isShowing()) {
        	 getActionBar().hide();
        	 if(actionBarHideHandler != null){
        		 actionBarHideHandler.removeCallbacksAndMessages(null);
        	 }
        } else {
        	 getActionBar().show();
        	 ActionBar actionBar = getActionBar();
        	 
        	 actionBarHideHandler = new Handler();
        	 actionBarHideHandler.postDelayed(new Runnable() {
        	     @Override
        	     public void run() {
        	         getActionBar().hide();
        	     }
        	 }, 3000);
        }
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.video_player, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        ShareActionProvider myShareActionProvider = (ShareActionProvider) item.getActionProvider();
        myShareActionProvider.setShareIntent(setUpShareIntent());
        Log.i("thiCREATE", "thisCREATE");

        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.i("thi", "this");
      switch (item.getItemId()) {
      case R.id.menu_item_share:
          if(actionBarHideHandler != null){
    		 actionBarHideHandler.removeCallbacksAndMessages(null);
          }
        break;
      default:
        break;
      }
      return true;  
    }
    
	private Intent setUpShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
    	shareIntent.setType("text/plain");
    	shareIntent.putExtra(Intent.EXTRA_TEXT, "I just watched \"" + videoTitles[currentPosition] 
	    			+ "\" on WSJ Live Android! It's a great video!");
    	return shareIntent;
	}
	
}