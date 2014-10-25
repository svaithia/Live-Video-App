package com.planauts.scrapper;

import android.os.AsyncTask;
import android.util.Log;

import com.planauts.bean.SectionBean;
import com.planauts.bean.SectionURLBean;
import com.planauts.common.Constants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SectionScrapper extends AsyncTask<Void, Void, HashMap<String, List<SectionBean>>> {
  Callback callback;
  private String TAG = SectionScrapper.class.getSimpleName();

  public SectionScrapper(Callback callback){
    this.callback = callback;
  }

  private HashMap<String, List<SectionBean>> parseXMLAndStoreIt(XmlPullParser myParser) {
    HashMap<String, List<SectionBean>> map = new LinkedHashMap<String, List<SectionBean>>();
    try {
      String key = null;
      for(int eventType = myParser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = myParser.next()){
        if(eventType == XmlPullParser.START_TAG){
          String name = myParser.getName();
          if(!name.equalsIgnoreCase("outline")){
            continue;
          }

          if(myParser.getAttributeCount() == 1){
            key = myParser.getAttributeValue(null, "text");
            map.put(key, new ArrayList<SectionBean>());
          }
          else if(key != null) {
            map.get(key).add(new SectionBean(myParser.getAttributeValue(null, "text"), myParser.getAttributeValue(null, "url")));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }


  @Override
  protected HashMap<String, List<SectionBean>> doInBackground(Void... voids) {
    HashMap<String, List<SectionBean>> sectionURLBean = null;
    try {
      URL url = new URL(Constants.SECTION_API_URL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);
      conn.setRequestMethod("GET");
      conn.setDoInput(true);
      conn.connect();
      InputStream stream = conn.getInputStream();

      XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
      XmlPullParser myParser = xmlFactoryObject.newPullParser();

      myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
      myParser.setInput(stream, null);
      sectionURLBean = parseXMLAndStoreIt(myParser);
      stream.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return sectionURLBean;
  }

  @Override
  protected void onPostExecute(HashMap<String, List<SectionBean>> sectionURLBean) {
    if(sectionURLBean == null){
      callback.failure();
      return;
    }

    callback.success(sectionURLBean);

  }

  public interface Callback{
    public void success(HashMap<String, List<SectionBean>> sectionURLBean);
    public void failure();
  }
}
