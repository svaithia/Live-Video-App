package com.planauts.scrapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.planauts.bean.PlaylistBean;

public class PlaylistURLScrapper {
	private String url;
	private int height;
	public PlaylistURLScrapper(String u, int h){
		url = u;
		height = h;
		url = "http://a1731.g.akamai.net/f/1731/67675/2m/video-api.wsj.com/mw2/mediarss/wsjdn/wsjtv.asp?type=playlist&query=Most+Viewed+WSJ+Videos";
		height = 540;
	}
	private volatile boolean parsingComplete = false;
	public boolean parsingComplete(){
		return parsingComplete;
	}
	
	private List<PlaylistBean> playlistUrlBeans;
	public List<PlaylistBean> sectionUrlBeans(){
		return playlistUrlBeans;
	}
	
	private XmlPullParserFactory xmlFactoryObject;
	
	private void parseXMLAndStoreIt(XmlPullParser myParser) {
		int eventType;
      
      try {
    	  String name;
    	  for(eventType = myParser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = myParser.next()){
    		  if(eventType == XmlPullParser.START_TAG){
    			  name = myParser.getName();
    			  if(name.equalsIgnoreCase("item")){
    				  PlaylistBean pb = new PlaylistBean();
    				  while(eventType != XmlPullParser.END_TAG && !myParser.getName().equalsIgnoreCase("item")){
    					  eventType = myParser.getEventType();
    					  if(eventType == XmlPullParser.START_TAG){
    						  name = myParser.getName();
    						  if(name.equalsIgnoreCase("title")){
    	    					  
    						  }
    						  else if(name.equalsIgnoreCase("link")){
        	    				  
    						  }
    						  else if(name.equalsIgnoreCase("pubDate")){
        	    				  
    						  }
    						  else if(name.equalsIgnoreCase("description")){
        	    				  
    						  }
    						  else if(name.equalsIgnoreCase("media:title")){
        	    				  
    						  }
    					  }
    					  eventType = myParser.next();
    				  }
    				  playlistUrlBeans.add(pb);
    			  }
    		  }
    	  }
    	  parsingComplete = true;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
	
   public void fetchXML(){
      Thread thread = new Thread(new Runnable(){
         @Override
         public void run() {
            try {
              URL URLObj = new URL(url);
              HttpURLConnection conn = (HttpURLConnection) URLObj.openConnection();
			  conn.setReadTimeout(10000 /* milliseconds */);
			  conn.setConnectTimeout(15000 /* milliseconds */);
			  conn.setRequestMethod("GET");
			  conn.setDoInput(true);
			  conn.connect();
            InputStream stream = conn.getInputStream();

            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
            , false);
            myparser.setInput(stream, null);
            parseXMLAndStoreIt(myparser);
            stream.close();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    });
    thread.start(); 
   }	
	
}
