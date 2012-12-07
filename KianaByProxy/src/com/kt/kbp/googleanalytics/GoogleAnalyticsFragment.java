package com.kt.kbp.googleanalytics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.analytics.tracking.android.Transaction;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.FragmentHandler;
import com.kt.kbp.tracker.LocationTracker;
import com.kt.kbp.tracker.PathTracker;

public class GoogleAnalyticsFragment extends Fragment {

	protected PathTracker pathTracker;
	protected Tracker tracker;
	private LocationTracker locationTracker;
    
	public FragmentManager getSupportFragmentManager() {
		return getFragmentManager();
	}
	
    public void trackerUpdate(String path) {
    	Log.i("fragments", "adding " + path + " to path list.");
    	getPathTracker().add(path);
    }
    
    public void trackPageView(String pageViewed) {
    	getTracker().trackView(pageViewed);
    }
	
	public void trackEvent(String category, String action, String label, long value) {
		getTracker().trackEvent(category, action, label, value);
	}
	
	public void trackException(String category, Exception e) {
		getTracker().trackException("Exception:" + category, e, true);
	}
	
    public void trackTransaction(Transaction transaction) {
    	getTracker().trackTransaction(transaction);
    }
    
    protected Tracker getTracker() {
    	if (tracker == null) {
            GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getActivity().getApplicationContext());
            //googleAnalytics.setDebug(true);
            tracker = googleAnalytics.getTracker(Constants.GA_TRACKING_ID);
    	}
    	return tracker;
    }
    
    protected PathTracker getPathTracker() {
    	if (pathTracker == null) {
    		pathTracker = PathTracker.getInstance();
    	}
    	return pathTracker;
    }
    
    public void handleFragment(String tag, int id) {
    	//showFragment(tag, findFragment(tag, id));
    	FragmentHandler.handleFragment(getFragmentManager(), tag, id);
    }
 
    public Fragment findFragment(String tag, int id) {
		
    	return FragmentHandler.findFragment(getFragmentManager(), tag, id);
    	
//    	Fragment fragment = getFragmentManager().findFragmentByTag(tag);
//		if (fragment == null) {
//			fragment = FragmentHandler.getNewFragment(id);
//		}
//		return fragment;
    }
    
	public void showFragment(String tag, Fragment fragment) {
		FragmentHandler.showFragment(getFragmentManager(), tag, fragment);
		
//		trackerUpdate(tag);
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.fragment_frame, fragment, tag);
//		transaction.addToBackStack(tag);
//		transaction.commit();
//		getFragmentManager().executePendingTransactions();
	}
	
	public void loadLocationTracker() {
		if (locationTracker == null) {
			locationTracker = new LocationTracker(getActivity());
		}
	}
	
	public LocationTracker getLocationTracker() {
		if (locationTracker == null) {
			loadLocationTracker();
		}
		return locationTracker;
	}
}
