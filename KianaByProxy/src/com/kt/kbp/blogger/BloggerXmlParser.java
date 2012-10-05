package com.kt.kbp.blogger;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.kt.kbp.common.XmlParser;

public class BloggerXmlParser extends XmlParser {

	public Blog parse(InputStream in) throws XmlPullParserException, IOException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
	}
	
    private Blog readFeed(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {    	
        String id = "";
        String title = "";
        List<BlogEntry> entries = new ArrayList<BlogEntry>();
        
    	parser.require(XmlPullParser.START_TAG, null, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if ("id".equals(name)) {
            	id = readId(parser);
            }
            else if ("title".equals(name)) {
            	title = readTitle(parser);
            } 
            else if ("entry".equals(name)) {
            	entries.add(readEntry(parser));
            }
            else {
            	skip(parser);
            }
            	
        }
    	return new Blog(id, title, entries);
    }
    
    private String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, null, "id");
    	String id = readText(parser);
    	parser.require(XmlPullParser.END_TAG, null, "id");
    	return id;
    }
    
    private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, null, "title");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, null, "title");
    	return title;
    }
    
    private BlogEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
    	String id = "";
    	String datePublished = "";
    	String title = "";
    	String content = "";
    	
    	parser.require(XmlPullParser.START_TAG, null, "entry");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if ("id".equals(name)) {
            	id = readId(parser);
            }
            else if ("published".equals(name)) {
            	datePublished = readPublished(parser);
            }
            else if ("title".equals(name)) {
            	title = readTitle(parser);
            }
            else if ("content".equals(name)) {
            	content = readContent(parser);
            }
            else {
            	skip(parser);
            }
        }
    	return new BlogEntry(id, formatDate(datePublished), title, content);
    }
    
    private Date formatDate(String dateAsString) throws ParseException {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    	return formatter.parse(dateAsString);
    }
    
    private String readPublished(XmlPullParser parser) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, null, "published");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, null, "published");
    	return title;
    }
    
    private String readContent(XmlPullParser parser) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, null, "content");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, null, "content");
    	return title;
    }
}
