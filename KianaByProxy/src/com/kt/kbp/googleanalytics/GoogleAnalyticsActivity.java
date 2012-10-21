package com.kt.kbp.googleanalytics;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.apps.analytics.Transaction;
import com.kt.kbp.common.ExceptionTrackerInterface;

public class GoogleAnalyticsActivity extends Activity implements ExceptionTrackerInterface {
	
	protected GoogleAnalyticsTracker tracker;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = GoogleAnalyticsTracker.getInstance();

        // Need to do this for every activity that uses google analytics
        GoogleAnalyticsSessionManager.getInstance(getApplication()).incrementActivityCount();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Example of how to track a pageview event
        GoogleAnalyticsTracker.getInstance().trackPageView(getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Purge analytics so they don't hold references to this activity
        GoogleAnalyticsTracker.getInstance().dispatch();

        // Need to do this for every activity that uses google analytics
        GoogleAnalyticsSessionManager.getInstance().decrementActivityCount();
    }

	public void trackException(String category, String message) {
		//category, action, label, value
		GoogleAnalyticsTracker.getInstance().trackEvent(category, "Exception", message, 0);
	}
	
    public void trackTransaction(Transaction transaction) {
    	GoogleAnalyticsTracker.getInstance().addTransaction(transaction);
    	GoogleAnalyticsTracker.getInstance().trackTransactions(); //TODO should this be done here?
    }
}
