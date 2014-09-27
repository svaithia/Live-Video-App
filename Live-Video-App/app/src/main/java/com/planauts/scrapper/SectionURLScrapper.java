package com.planauts.scrapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.planauts.bean.SectionURLBean;
import com.planauts.common.Constants;

public class SectionURLScrapper {
	public SectionURLScrapper(){}
	private volatile boolean parsingComplete = false;
	public boolean parsingComplete(){
		return parsingComplete;
	}
	
	private SectionURLBean sectionUrlBeans;
	public SectionURLBean sectionUrlBeans(){
		return sectionUrlBeans;
	}
	
	private XmlPullParserFactory xmlFactoryObject;
	
	private void parseXMLAndStoreIt(XmlPullParser myParser) {
		int eventType;
      
      try {
    	  String name;
    	  LinkedHashMap<String, String> map = null;
    	  LinkedHashMap<String, String> vod = null;
    	  LinkedHashMap<String, String> special = null;
    	  LinkedHashMap<String, String> program = null;
    	  for(eventType = myParser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = myParser.next()){
    		  switch(eventType){
    		  case XmlPullParser.START_TAG:
    			  name = myParser.getName();
    			  if(!name.equalsIgnoreCase("outline")){
    				  break;
    			  }
    			  if(myParser.getAttributeValue(null, "text").equalsIgnoreCase("VOD")){
    				  map = new LinkedHashMap<String, String>();
    				  break;
    			  }
    			  else if(myParser.getAttributeValue(null, "text").equalsIgnoreCase("Program")){
    				  vod = map;
    				  map = new LinkedHashMap<String, String>();
  				  	  break;
    			  }
    			  else if(myParser.getAttributeValue(null, "text").equalsIgnoreCase("Special")){
      				  program = map;
    				  map = new LinkedHashMap<String, String>();
      				  break;
      			  }
    			  
    			  if(name.equalsIgnoreCase("outline")){
    				  map.put(myParser.getAttributeValue(null, "text"), myParser.getAttributeValue(null, "url"));
    			  }
    			  break;
    		  }
    	  }
    	  special = map;
    	  sectionUrlBeans = new SectionURLBean(vod, program, special);
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
               URL url = new URL(Constants.SECTION_API_URL);
               HttpURLConnection conn = (HttpURLConnection) 
               url.openConnection();
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
