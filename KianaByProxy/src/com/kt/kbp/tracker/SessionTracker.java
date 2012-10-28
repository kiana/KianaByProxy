package com.kt.kbp.tracker;

import com.kt.kbp.common.Constants;

/**
 * SessionTracker keeps track of time period in which application is active. 
 * When a user has been inactive in the application for more than five minutes 
 * (judged by amount of time between activity requests), the application is eligible to begin a new session.
 * 
 * @author Kiana
 *
 */
public class SessionTracker {
	
	private static SessionTracker sessionTracker;
	private static long lastUpdateTime;
	
	private SessionTracker() {
	}
	
	public static SessionTracker getInstance() {
		if (sessionTracker == null) {
			sessionTracker = new SessionTracker();
		}
		return sessionTracker;
	}
	
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	
	public void update() {
		lastUpdateTime = System.currentTimeMillis();
	}
	
	public boolean shouldStartNewSession() {
		return (lastUpdateTime + Constants.FIVE_MINUTES) <= System.currentTimeMillis();
	}
	
}

