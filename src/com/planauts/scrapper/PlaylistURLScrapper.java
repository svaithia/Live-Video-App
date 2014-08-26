package com.planauts.scrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.planauts.bean.PlaylistBean;

public class PlaylistURLScrapper {
	private String url;
	private int height;
	public PlaylistURLScrapper(String u, int h){
		url = u;
		height = h;
		url = "http://a1731.g.akamai.net/f/1731/67675/2m/video-api.wsj.com/mw2/mediarss/wsjdn/wsjtv.asp?type=playlist&query=Most+Viewed+WSJ+Videos";
		height = 360;
	}
	private volatile boolean parsingComplete = false;
	public boolean parsingComplete(){
		return parsingComplete;
	}
	
	private List<PlaylistBean> playlistUrlBeans = new ArrayList<PlaylistBean>();
	public List<PlaylistBean> playlistUrlBeans(){
		return playlistUrlBeans;
	}
	
	private XmlPullParserFactory xmlFactoryObject;
	
	private void parseXMLAndStoreIt(XmlPullParser myParser) {
		int eventType;
		playlistUrlBeans = new ArrayList<PlaylistBean>();
      
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
    	            skip(myParser);
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
            myparser.nextTag();
            parseXMLAndStoreIt(myparser);
            stream.close();
            } catch (Exception e) {
            	System.out.println("sds");
               e.printStackTrace();
            }
        }
    });
    thread.start(); 
   }	
   
	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	// Processes title tags in the feed.
	private String readTextByTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, null, tag);
	    String text = readText(parser);
	    parser.require(XmlPullParser.END_TAG, null, tag);
	    return text;
	}
   
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
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
	            title = readTextByTag(parser, "title");
	        } else if (name.equalsIgnoreCase("description")) {
	            description = readTextByTag(parser, "description");
	            
	        } else if(name.equalsIgnoreCase("media:group")){
	        	while(parser.next() != XmlPullParser.END_TAG){
	        		name = parser.getName();
	        		String u = parser.getAttributeValue("", "url");
	        		String h = parser.getAttributeValue("", "height");
	        		if(name.equalsIgnoreCase("media:thumbnail") && u != null && h != null && Integer.valueOf(h) == 94){
			        	image = u;
			        } else if(name.equalsIgnoreCase("media:content") && u != null && h != null && Integer.valueOf(h) == height){
			        	url = u;
			        	duration = Integer.valueOf(parser.getAttributeValue("", "duration"));
			        }
	        		skip(parser);
	        	}
	        }  else if(name.equalsIgnoreCase("pubDate")){
	        	pubDate = readTextByTag(parser, "pubDate");
	        }  else {
	            skip(parser);
	        }
	    }
	    return new PlaylistBean(title, description, url, image, pubDate, duration);
	}
	
}
