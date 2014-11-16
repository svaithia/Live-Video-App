package com.planauts.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class PlaylistBean implements Parcelable {
	public final String title;
	public final String description;
  public final String url;
  public final String image;
  public final String pubDate;
  public final int duration;
  public String TAG = PlaylistBean.class.getSimpleName();

  public static final String[] properties = {"title", "description", "url"};
  public String getProperties(int i){
    switch(i){
      case 0:{
        return title;
      }
      case 1:{
        return description;
      }
      case 2:{
        return url;
      }
    }
    return title;
  }

  public PlaylistBean(String t, String d, String u, String i, String pd, int dur){
    title = t;
    description = d;
    url = u;
    image = i;
    pubDate = pd;
    duration = dur;
  }

  public PlaylistBean(Parcel in){
    title = in.readString();
    description = in.readString();
    url = in.readString();
    image = in.readString();
    pubDate = in.readString();
    duration = in.readInt();
  }

	public String durationFormatted(){
		int s = duration % 60;
		int m = (duration/60) % 60;
		int h = (duration/(60*60)) % 24;

	  return ((h > 0) ? String.format("%02d", h) + ":" : "") + String.format("%02d", m) + ":" + String.format("%02d", s);
	}

  public String pubDateFormatted(){
    String formattedDate = pubDate;
    try {
      SimpleDateFormat originalFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
      originalFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      Date dateStr = originalFormat.parse(pubDate);
      originalFormat.setTimeZone(TimeZone.getDefault());
      formattedDate = originalFormat.format(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return formattedDate;
  }
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(title + "-" + description + "-" + url+"-"+image+"-"+pubDate+"-"+duration);
		return sb.toString();
	}

  public static final Parcelable.Creator<PlaylistBean> CREATOR = new Parcelable.Creator<PlaylistBean>() {
    public PlaylistBean createFromParcel(Parcel in) {
      return new PlaylistBean(in);
    }

    public PlaylistBean[] newArray(int size) {
      return new PlaylistBean[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(title);
    parcel.writeString(description);
    parcel.writeString(url);
    parcel.writeString(image);
    parcel.writeString(pubDate);
    parcel.writeInt(duration);
  }
}
