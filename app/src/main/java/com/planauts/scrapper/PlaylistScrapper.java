package com.planauts.scrapper;

import android.os.AsyncTask;

import com.planauts.bean.PlaylistBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class PlaylistScrapper extends AsyncTask<Void, Void, List<PlaylistBean>> {
  public interface Callback{
    public void success(List<PlaylistBean> playlistBeans);
    public void failure();
  }
  private String url;
  private int height;
  private Callback callback;
  private List<PlaylistBean> parseXMLAndStoreIt(XmlPullParser myParser) {
    int eventType;
    List<PlaylistBean> playlistUrlBeans = new ArrayList<PlaylistBean>();

    try {
      String name;
      while (myParser.next() != XmlPullParser.END_TAG){
        if (myParser.getEventType() != XmlPullParser.START_TAG) {
          continue;
        }
        name = myParser.getName();
        // Starts by looking for the entry tag
        if (name.equals("item")) {
          playlistUrlBeans.add(readItem(myParser));
        } else if(name.equals("channel")){

        }else {
          XMLParser.skip(myParser);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return playlistUrlBeans;
  }
  private PlaylistBean readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
    parser.require(XmlPullParser.START_TAG, null, "item");
    String title = null;
    String description = null;
    String url = null;
    String image = null;
    String pubDate = null;
    int duration = 0;

    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      if (name.equals("title")) {
        title = XMLParser.readTextByTag(parser, "title");
      } else if (name.equalsIgnoreCase("description")) {
        description = XMLParser.readTextByTag(parser, "description");

      } else if(name.equalsIgnoreCase("media:group")){
        while(parser.next() != XmlPullParser.END_TAG){
          name = parser.getName();
          String u = parser.getAttributeValue("", "url");
          String h = parser.getAttributeValue("", "height");
          if(name.equalsIgnoreCase("media:thumbnail") && u != null && h != null && Integer.valueOf(h) == 270){
            image = u;
          } else if(name.equalsIgnoreCase("media:content") && u != null && h != null && Integer.valueOf(h) == height){
            url = u;
            duration = Integer.valueOf(parser.getAttributeValue("", "duration"));
          }
          XMLParser.skip(parser);
        }
      }  else if(name.equalsIgnoreCase("pubDate")){
        pubDate = XMLParser.readTextByTag(parser, "pubDate");
      }  else {
        XMLParser.skip(parser);
      }
    }
    return new PlaylistBean(title, description, url, image, pubDate, duration);
  }

  public PlaylistScrapper(String url, int height, Callback callback){
    this.url = url;
    this.height = height;
    this.callback = callback;
  }

  @Override
  protected List<PlaylistBean> doInBackground(Void... voids) {
    List<PlaylistBean> playlist = new ArrayList<PlaylistBean>();
    try {
      URL URLObj = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) URLObj.openConnection();
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.setDoInput(true);
      conn.connect();
      InputStream stream = conn.getInputStream();

      XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
      XmlPullParser myparser = xmlFactoryObject.newPullParser();

      myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
        , false);

      myparser.setInput(stream, null);
      myparser.nextTag();
      playlist = parseXMLAndStoreIt(myparser);
      stream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return playlist;
  }

  @Override
  protected void onPostExecute(List<PlaylistBean> playlistBeans) {
    if(playlistBeans.isEmpty()){
      callback.failure();
      return;
    }
    callback.success(playlistBeans);
  }
}
