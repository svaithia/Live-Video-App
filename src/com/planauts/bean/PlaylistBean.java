package com.planauts.bean;

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
	
	public PlaylistBean(){
	
	}
	
	public PlaylistBean(String t, String d, String u, String i, int dur){
		title = t;
		description = d;
		url = u;
		image = i;
		duration = dur;
	}
}
