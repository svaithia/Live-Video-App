package com.planauts.scrapper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by svaithia@uwaterloo.ca on 10/25/14.
 */
public class XMLParser {


  // For the tags title and summary, extracts their text values.
  public static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
    String result = "";
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.getText();
      parser.nextTag();
    }
    return result;
  }

  // Processes title tags in the feed.
  public static String readTextByTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
    parser.require(XmlPullParser.START_TAG, null, tag);
    String text = readText(parser);
    parser.require(XmlPullParser.END_TAG, null, tag);
    return text;
  }

  public static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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

}
