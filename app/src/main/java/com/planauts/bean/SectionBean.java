package com.planauts.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class SectionBean implements Parcelable {
  public final String name;
  public final String url;

  public SectionBean(String name, String url){
    this.name = name;
    this.url = url;
  }

  public SectionBean(Parcel in){
    name = in.readString();
    url = in.readString();
  }

  public static final Parcelable.Creator<SectionBean> CREATOR = new Parcelable.Creator<SectionBean>() {
    public SectionBean createFromParcel(Parcel in) {
      return new SectionBean(in);
    }

    public SectionBean[] newArray(int size) {
      return new SectionBean[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(name);
    parcel.writeString(url);
  }
}
