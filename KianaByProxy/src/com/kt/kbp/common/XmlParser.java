package com.kt.kbp.common;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlParser {

    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
    
    protected String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    

	protected String readAttribute(XmlPullParser parser, String tagName, String attributeName)
			throws XmlPullParserException, IOException {
		String tag = parser.getName();
    	String attribute = "";
    	if (tagName.equals(tag)) {
    		attribute = parser.getAttributeValue(null, attributeName);
    		parser.nextTag();
    	}
		return attribute;
	}
}
