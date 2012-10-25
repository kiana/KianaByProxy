package com.kt.kbp.googleanalytics;

import com.kt.kbp.activitypath.ActivityPath;

import android.provider.Settings.Secure;

public class PathTracker {

	private StringBuilder path;
	private static PathTracker INSTANCE;
	private static long sessionStart;
	private static final long FIVE_MINUTES = 1000*60*5;
	
	private PathTracker() {
	}
	
	public static PathTracker getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PathTracker();
		}
		return INSTANCE;
	}
	
	public void add(ActivityPath page) {
		if (path == null) {
			path = new StringBuilder(page.toString());
			sessionStart = System.currentTimeMillis();
		}
		else {
			path.append("|")
				.append(page);
		}
	}
	
	public String getPathName() {
		return Secure.ANDROID_ID + "|" + sessionStart;
	}
	
	public String getPath() {
		return path.toString();
	}
	
	public boolean startNewSession() {
		return (sessionStart + FIVE_MINUTES) <= System.currentTimeMillis();
	}
	
	public void clearPath() {
		path = null;
	}
}
