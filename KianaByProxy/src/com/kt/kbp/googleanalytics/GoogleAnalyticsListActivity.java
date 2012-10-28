package com.kt.kbp.googleanalytics;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;

import com.amazonaws.services.dynamodb.model.PutItemResult;
import com.amazonaws.tvmclient.AmazonClientManager;
import com.amazonaws.tvmclient.DBManager;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.apps.analytics.Transaction;
import com.kt.kbp.activitypath.ActivityPath;
import com.kt.kbp.activitypath.ActivityPathInterface;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.ExceptionTrackerInterface;
import com.kt.kbp.tracker.LocationTracker;
import com.kt.kbp.tracker.PathTracker;
import com.kt.kbp.tracker.SessionTracker;

public class GoogleAnalyticsListActivity extends ListActivity implements ExceptionTrackerInterface, ActivityPathInterface {

	protected GoogleAnalyticsTracker tracker;
	protected PathTracker pathTracker;
	protected LocationTracker locationTracker;
	protected SessionTracker sessionTracker;
	
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
        Log.i("userPathData", "onResume-" + getClass().getSimpleName());
        if (getSessionTracker().shouldStartNewSession() 
        		&& getPathTracker().hasPathData()
        		&& getLocationTracker().hasLocation()) {
        	insertUserPathData();
        }
        
    	getPathTracker().add(getActivityPath());
    	getSessionTracker().update();
    }

    @Override
    protected void onPause() {
        super.onDestroy();
        Log.i("userPathData", "onPause-" + getClass().getSimpleName());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("userPathData", "onDestroy-" + getClass().getSimpleName());
    	
        // Purge analytics so they don't hold references to this activity
        getTracker().dispatch();

        // Need to do this for every activity that uses google analytics
        GoogleAnalyticsSessionManager.getInstance().decrementActivityCount();
    }
    
    @Override
    public void onUserInteraction() {
    	Log.i("userPathData", "onUserInteraction called for " + getClass().getSimpleName());
    }
    
    @Override
    public void onUserLeaveHint() {
    	Log.i("userPathData", "onUserLeaveHint called for " + getClass().getSimpleName());
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
    
    public void insertUserPathData() {
    	Log.i("userPathData", "dispatching: " + getPathTracker().getPath());
    	//new DBManagerTask().execute();
    }
    
	@Override
	public ActivityPath getActivityPath() {
		return ActivityPath.GOOGLEANALYTICS;
	}
    
	public LocationTracker getLocationTracker() {
		if (locationTracker == null) {
			locationTracker = new LocationTracker(this);
		}
		return locationTracker;
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
    
    private class DBManagerTask extends AsyncTask<Void, Void, PutItemResult> {

		@Override
		protected PutItemResult doInBackground(Void... arg0) {
			
			AmazonClientManager clientManager = new AmazonClientManager(getSharedPreferences(
					Constants.DB_MANAGER, Context.MODE_PRIVATE));
			
	    	return DBManager.insert(clientManager, 
	    			getID(), 
	    			"" + getSessionTracker().getLastUpdateTime(), 
	    			"" + getLocationTracker().getLocation().getLatitude(), 
	    			"" + getLocationTracker().getLocation().getLongitude(), 
	    			getPathTracker().getPath(),
	    			"userPathData");
		}
    	
		protected void onPostExecute(PutItemResult result) {
			if (result != null) {
				getPathTracker().clearPath();
			}
		}
    }
    
	private String getID() {
		return Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);
	}
}