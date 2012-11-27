package com.kt.kbp.googleanalytics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.analytics.tracking.android.Transaction;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.FragmentFactory;
import com.kt.kbp.tracker.LocationTracker;
import com.kt.kbp.tracker.PathTracker;

public class GoogleAnalyticsListFragment extends ListFragment {

	protected PathTracker pathTracker;
	protected GoogleAnalytics googleAnalytics;
	protected Tracker tracker;
	private LocationTracker locationTracker;
	
    public void trackerUpdate(String path) {
    	Log.i("fragments", "adding " + path + " to path list.");
    	getPathTracker().add(path); //TODO do the ADD onClick() or in trackPageView()
    }
    
    public void trackPageView(String pageViewed) {
    	getTracker().trackView(pageViewed);
    }
    
	public void trackEvent(String category, String action, String label, int value) {
		getTracker().trackEvent(category, action, label, (long) value);
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
            googleAnalytics.setDebug(true);
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
    	showFragment(tag, findFragment(tag, id));
    }
 
    public Fragment findFragment(String tag, int id) {
		Fragment fragment = getFragmentManager().findFragmentByTag(tag);
		if (fragment == null) {
			fragment = FragmentFactory.getNewFragment(id);
		}
		return fragment;
    }
    
	public void showFragment(String tag, Fragment fragment) {
		trackerUpdate(tag);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_frame, fragment, tag);
		transaction.addToBackStack(tag);
		transaction.commit();
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
