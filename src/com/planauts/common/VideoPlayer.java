package com.planauts.common;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.MediaController;
import android.widget.VideoView;

import com.planauts.wsj.R;

public class VideoPlayer extends Activity implements OnCompletionListener,OnPreparedListener {
    
    private VideoView vvPlayer;
    private MediaController vidControl;
    private int currentPosition;
    private String fileRes[];
          
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.videoplayer);
        
        currentPosition = 0;
            
        Bundle e = getIntent().getExtras();
        fileRes = (e!=null) ? e.getStringArray("videoPlaylistUrls") : null;
 
        vvPlayer = (VideoView)findViewById(R.id.myvideoview);
        vvPlayer.setOnCompletionListener(this);
        vvPlayer.setOnPreparedListener(this);
 
        
        
        vidControl = new MediaController(this);
        vidControl.setPrevNextListeners(new nextOnClickListener(), new prevOnClickListener());
        
        vidControl.setAnchorView(vvPlayer);
        vvPlayer.setMediaController(vidControl);
        
        if (!playFileRes()) return;
    }
             
    private boolean playFileRes() {
        if (fileRes==null) {
            stopPlaying();
            return false;
        } else {
        	String url = fileRes[currentPosition];
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
}