package com.planauts.common;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.planauts.wsj.R;

public class VideoPlayer extends Activity implements OnCompletionListener,OnPreparedListener {
    
    private VideoView vvPlayer;
    private MediaController vidControl;
    private int currentPosition;
    private String fileRes[];
    private String videoTitles[];
    
    private Handler actionBarHideHandler;
          
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
        
        getActionBar().setBackgroundDrawable(null);
        
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
        
        
        LinearLayout llPlayerContainer = (LinearLayout)findViewById(R.id.llPlayerContainer);
        getActionBar().hide();
        
        llPlayerContainer.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				toggleActionBarVisiblity();
				return false;
			}
        });
//        vidControl.show();

        if (!playFileRes()) return;
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
}