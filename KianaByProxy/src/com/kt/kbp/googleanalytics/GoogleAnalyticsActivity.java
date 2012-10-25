package com.kt.kbp.googleanalytics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.apps.analytics.Transaction;
import com.kt.kbp.activitypath.ActivityPath;
import com.kt.kbp.activitypath.ActivityPathInterface;
import com.kt.kbp.common.ExceptionTrackerInterface;

public class GoogleAnalyticsActivity extends Activity implements ExceptionTrackerInterface, ActivityPathInterface {
	
	protected GoogleAnalyticsTracker tracker;
	protected PathTracker pathTracker;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Need to do this for every activity that uses google analytics
        GoogleAnalyticsSessionManager.getInstance(getApplication()).incrementActivityCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTracker().trackPageView(getClass().getSimpleName());
        
        if (getPathTracker().startNewSession()) {
        	sendPathCustomVariable();
        	getPathTracker().clearPath();
        }
    	getPathTracker().add(getActivityPath());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    	Log.i(getClass().getSimpleName(), "onDestroy");
    	
        // Purge analytics so they don't hold references to this activity
        getTracker().dispatch();

        // Need to do this for every activity that uses google analytics
        GoogleAnalyticsSessionManager.getInstance().decrementActivityCount();
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
    
    public void sendPathCustomVariable() {
    	//index, name, value, opt_scope
    	getTracker().setCustomVar(1, getPathTracker().getPathName(), getPathTracker().getPath(), 2);
    	getTracker().dispatch();
    }

	@Override
	public ActivityPath getActivityPath() {
		return ActivityPath.GOOGLEANALYTICS;
	}
    
}
