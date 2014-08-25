package com.planauts.scrapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.planauts.bean.SectionURLBean;
import com.planauts.common.Constants;

public class SectionURLScrapper {
	
	public Constants a;
	
	public SectionURLScrapper(){
	}
	
	public volatile boolean parsingComplete = true;
	private XmlPullParserFactory xmlFactoryObject;
	 
	public List<SectionURLBean> parseXMLAndStoreIt(XmlPullParser myParser) {
		List<SectionURLBean> returnList = new ArrayList<SectionURLBean>();
		int eventType;
      
      try {
    	  String name;
    	  for(eventType = myParser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = myParser.next()){
    		  switch(eventType){
    		  case XmlPullParser.START_TAG:
    			  name = myParser.getName();
    			  if(name.equalsIgnoreCase("outline")){
    				  if(!myParser.getAttributeValue(null, "text").equalsIgnoreCase("VOD")){
    					  returnList.add(new SectionURLBean(
    					  myParser.getAttributeValue(null, "text"), myParser.getAttributeValue(null, "url")
    							  ));
    				  }
    			  }
    			  break;
    		  }
    	  }  
      } catch (Exception e) {
         e.printStackTrace();
      }
	return null;
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
