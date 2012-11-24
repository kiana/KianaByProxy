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
	
	/**
	 * This method ensures the path contains more than MainActivity and MainFragment info
	 * @return
	 */
	public boolean containsData() {
		return false;
	}
	
	/**
	 * This method returns true if the user has navigated to more than one page.
	 * @return
	 */
	public boolean trackingCount() {
		return false;
	}
}
