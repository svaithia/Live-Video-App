package com.planauts.bean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class PlaylistBean {
	public final String title;
	public final String description;
  public final String url;
  public final String image;
  public final String pubDate;
  public final int duration;

	public String durationFormatted(){
		int s = duration % 60;
		int m = (duration/60) % 60;
		int h = (duration/(60*60)) % 24;
		
	    return ((h > 0) ? String.format("%02d", h) + ":" : "") + String.format("%02d", m) + ":" + String.format("%02d", s);
	}

	public PlaylistBean(String t, String d, String u, String i, String pd, int dur){
		title = t;
		description = d;
		url = u;
		image = i;
		pubDate = pd;
		duration = dur;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(title + "-" + description + "-" + url+"-"+image+"-"+pubDate+"-"+duration);
		return sb.toString();
	}
	
}
