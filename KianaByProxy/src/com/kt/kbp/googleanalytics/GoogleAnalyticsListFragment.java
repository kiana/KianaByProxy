package com.kt.kbp.googleanalytics;

import android.support.v4.app.ListFragment;
import android.util.Log;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.apps.analytics.Transaction;
import com.kt.kbp.tracker.PathTracker;
import com.kt.kbp.tracker.SessionTracker;

public class GoogleAnalyticsListFragment extends ListFragment {

	protected PathTracker pathTracker;
	protected SessionTracker sessionTracker;
	protected GoogleAnalyticsTracker tracker;
	
    public void trackerUpdate(String path) {
    	Log.i("fragments", "adding " + path + " to path list.");
    	getPathTracker().add(path); //TODO do the ADD onClick() or in trackPageView()
    	getSessionTracker().update(); //TODO check whether I even need this anymore now that i'm using one activity
    }
    
    public void trackPageView(String pageViewed) {
    	getTracker().trackPageView(pageViewed);
    }
    
	public void trackEvent(String category, String action, String label, int value) {
		getTracker().trackEvent(category, action, label, value);
	}
	
	public void trackException(String category, String message) {
		//category, action, label, value
		getTracker().trackEvent(category, "Exception", message, 0);
	}
	
    public void trackTransaction(Transaction transaction) {
    	getTracker().addTransaction(transaction);
    	getTracker().trackTransactions(); //TODO should this be done here?
    }
    
    protected GoogleAnalyticsTracker getTracker() {
    	if (tracker == null) {
    		tracker = GoogleAnalyticsTracker.getInstance();
    	}
    	return tracker;
    }
    
    protected PathTracker getPathTracker() {
    	if (pathTracker == null) {
    		pathTracker = PathTracker.getInstance();
    	}
    	return pathTracker;
    }
    
    protected SessionTracker getSessionTracker() {
    	if (sessionTracker == null) {
    		sessionTracker = SessionTracker.getInstance();
    	}
    	return sessionTracker;
    }
}
