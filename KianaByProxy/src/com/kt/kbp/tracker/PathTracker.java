package com.kt.kbp.tracker;

import com.kt.kbp.path.Path;

public class PathTracker {

	private StringBuilder path;
	private static PathTracker INSTANCE;
	
	private PathTracker() {
	}
	
	public static PathTracker getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PathTracker();
		}
		return INSTANCE;
	}
	
	public void add(Path page) {
		if (path == null) {
			path = new StringBuilder(page.toString());
		}
		else {
			path.append("|")
				.append(page);
		}
	}
	
	public String getPath() {
		return path.toString();
	}
	
	public boolean hasPathData() {
		return path != null;
	}
	
	public void clearPath() {
		path = null;
	}
}
