package com.planauts.bean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class PlaylistBean {
	String title;
	public String title(){ return title; }
	public void title(String t){title = t;}
	
	String description;
	public String description(){ return description; }
	public void description(String d){description = d;}
	
	String url;
	public String url(){ return url; }
	public void url(String u){url = u;}
	
	String image;
	public String image(){ return image; }
	public void image(String i){image = i;}
	
	String pubDate;
	public String pubDate(){ return pubDate; }
	public void pubDate(String pd){pubDate = pd;}

	int duration;
	public int duration(){ return duration; }
	public void duration(int d){duration = d;}
	public String durationFormatted(){
		int s = duration % 60;
		int m = (duration/60) % 60;
		int h = (duration/(60*60)) % 24;
		
	    return ((h > 0) ? String.format("%02d", h) + ":" : "") + String.format("%02d", m) + ":" + String.format("%02d", s);
	}
	
	
	public PlaylistBean(){
	
	}
	
	public PlaylistBean(String t, String d, String u, String i, String pd, int dur){
		title(t);
		description(d);
		url(u);
		image(i);
		pubDate(pd);
		duration(dur);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(title + "-" + description + "-" + url+"-"+image+"-"+pubDate+"-"+duration);
		return sb.toString();
	}
	
}
