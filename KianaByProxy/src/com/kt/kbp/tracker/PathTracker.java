package com.kt.kbp.tracker;

import java.util.ArrayList;
import java.util.List;

public class PathTracker {

	private static PathTracker INSTANCE;
	private List<String> path;
	
	private PathTracker() {
	}
	
	public static PathTracker getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PathTracker();
		}
		return INSTANCE;
	}
	
	public void add(String page) {
		if (path == null) {
			path = new ArrayList<String>();
		}
		else {
			path.add(page);
		}
	}
	
	public List<String> getPath() {
		return path;
	}
	
	public StringBuilder getPathStringBuilder() {
		StringBuilder pathBuilder = new StringBuilder();
		boolean initialized = false;
		for (String step : path) {
			if (initialized) {
				pathBuilder.append("|");
			}
			pathBuilder.append(step);
			initialized = true;
		}
		
		return pathBuilder;
	}
	
	public CharSequence[] getPathAsCharSequence() {
		if (hasPathData()) {
			return path.toArray(new CharSequence[path.size()]);
		} else { 
			return new CharSequence[0];
		}
	}
	
	public boolean hasPathData() {
		return path != null && path.size() > 0;
	}
	
	public void clearPath() {
		path = new ArrayList<String>();
	}

}
