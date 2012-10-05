package com.kt.kbp.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.kt.kbp.common.XmlParser;

public class YoutubeXmlParser extends XmlParser {

	private static final String nameSpace = null;
	private static final String media_nameSpace = "media";
	private static final String yt_nameSpace = "yt";
	
    public List<YoutubeEntry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    
    private List<YoutubeEntry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<YoutubeEntry> entries = new ArrayList<YoutubeEntry>();

        parser.require(XmlPullParser.START_TAG, nameSpace, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if ("entry".equals(name)) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }  
        return entries;
    }
    
    private YoutubeEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, null, "entry");

    	MediaGroup mediaGroup = null;
    	String viewCount = null;
    	String id = null;
    	
    	while (parser.next() != XmlPullParser.END_TAG) {
    		if (parser.getEventType() != XmlPullParser.START_TAG) {
    			continue;
    		}
    		
    		String prefix = parser.getPrefix();
    		String name = parser.getName();
    		if (media_nameSpace.equals(prefix) && "group".equals(name)) {
    			//go a level deeper and read all data inside
    			mediaGroup = readMediaGroup(parser, parser.getNamespace(prefix));
    		}
    		else if (yt_nameSpace.equals(prefix) && "statistics".equals(name)) {
    			//read viewCount attribute
    			viewCount = readViewCount(parser, parser.getNamespace(prefix));
    		} else if ("id".equals(name)) {
    			id = readId(parser, null);
    		} else {
    			skip(parser);
    		}
    	}
    	
    	return new YoutubeEntry(id, mediaGroup, viewCount);
    }
    
    private MediaGroup readMediaGroup(XmlPullParser parser, String namespacedPrefix) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, namespacedPrefix, "group");
    	
    	String description = null;
    	String thumbNail = null;
    	boolean unreadThumbNail = true;
    	String title = null;
    	String duration = null;
    	
    	while (parser.next() != XmlPullParser.END_TAG) {
    		if (parser.getEventType() != XmlPullParser.START_TAG) {
    			continue;
    		}
    		
    		String prefix = parser.getPrefix();
    		String name = parser.getName();
    		if (yt_nameSpace.equals(prefix)) {
    			if ("duration".equals(name)) {
    				duration = formatDuration(readDuration(parser, parser.getNamespace(prefix)));
    			} else {
        			skip(parser);
        		}
    		} else if (media_nameSpace.equals(prefix)) {
    			if ("description".equals(name)) {
    				description = readDescription(parser, namespacedPrefix);
    			} else if (unreadThumbNail && "thumbnail".equals(name)) {
    				thumbNail = readThumbnail(parser, namespacedPrefix);
    				if (thumbNail.contains("0.jpg")) {
    					unreadThumbNail = false;
    				}
    			} else if ("title".equals(name)) {
    				title = readTitle(parser, namespacedPrefix);
    			} else {
        			skip(parser);
        		}
    		} else {
    			skip(parser);
    		}
    		
    	}
    	
    	return new MediaGroup(thumbNail, title, description, duration);
    }
    
    private String readDescription(XmlPullParser parser, String namespacedPrefix) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, namespacedPrefix, "description");
    	String description = readText(parser);
    	parser.require(XmlPullParser.END_TAG, namespacedPrefix, "description");
    	return description;
    }

    private String readThumbnail(XmlPullParser parser, String namespacedPrefix) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, namespacedPrefix, "thumbnail");
    	String thumbnail = readAttribute(parser, "thumbnail", "url");
    	parser.require(XmlPullParser.END_TAG, namespacedPrefix, "thumbnail");
    	return thumbnail;
    }
    
    private String readTitle(XmlPullParser parser, String namespacedPrefix) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, namespacedPrefix, "title");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, namespacedPrefix, "title");
    	return title;
    }
    
    private String readDuration(XmlPullParser parser, String namespacedPrefix) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, namespacedPrefix, "duration");
    	String duration = readAttribute(parser, "duration", "seconds");
    	parser.require(XmlPullParser.END_TAG, namespacedPrefix, "duration");
    	return duration;
    }
    
    private String formatDuration(String duration) {
    	String formattedDuration;
    	
    	int d = Integer.parseInt(duration);
    	int minutes = d / 60;
    	int seconds = d % 60;
    	
    	formattedDuration = minutes + ":" + seconds;
    	return formattedDuration;
    }
    
    private String readId(XmlPullParser parser, String namespacedPrefix) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, namespacedPrefix, "id");
    	String id = readText(parser);
    	String videoId = id.substring(id.lastIndexOf("/")+1);
    	parser.require(XmlPullParser.END_TAG, namespacedPrefix, "id");
    	return videoId;
    }
    
    private String readViewCount(XmlPullParser parser, String namespacedPrefix) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, namespacedPrefix, "statistics");
    	String viewCount = readAttribute(parser, "statistics", "viewCount");
    	parser.require(XmlPullParser.END_TAG, namespacedPrefix, "statistics");
    	return viewCount;
    }
   
}
