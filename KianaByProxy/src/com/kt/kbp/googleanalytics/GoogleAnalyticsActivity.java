package com.kt.kbp.googleanalytics;

import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.kt.kbp.common.Constants;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;
import com.kt.kbp.tracker.LocationTracker;
import com.kt.kbp.tracker.PathTracker;
import com.kt.kbp.tracker.SessionTracker;

public class GoogleAnalyticsActivity extends FragmentActivity implements PathInterface {

	protected GoogleAnalyticsTracker tracker;
	protected LocationTracker locationTracker;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleAnalyticsTracker.getInstance().startNewSession(Constants.GA_TRACKING_ID, 1000*60, getApplicationContext());
        getLocationTracker();
    }
    
    protected void onStop() {
    	super.onStop();
    	GoogleAnalyticsTracker.getInstance().dispatch();
    	GoogleAnalyticsTracker.getInstance().stopSession();
    	
        if (SessionTracker.getInstance().shouldStartNewSession() 
        		&& PathTracker.getInstance().hasPathData()
        		&& getLocationTracker().hasLocation()) {
        	//insertUserPathData();
        	Toast.makeText(this, PathTracker.getInstance().getPath(), Toast.LENGTH_LONG).show();
        	Toast.makeText(this, getLocationTracker().getLocation().getLatitude() + "+" +
        			getLocationTracker().getLocation().getLongitude(), Toast.LENGTH_LONG).show();
        	Toast.makeText(this, getID(), Toast.LENGTH_LONG).show();
        }
        
    	Log.i("fragments", "onStop: MainActivity. Send data now.");
    }
    
    
	public void trackEvent(String category, String action, String label, int value) {
		getTracker().trackEvent(category, action, label, value);
	}
    
    protected GoogleAnalyticsTracker getTracker() {
    	if (tracker == null) {
    		tracker = GoogleAnalyticsTracker.getInstance();
    	}
    	return tracker;
    }
    
    public void insertUserPathData() {
    	Log.i("userPathData", "dispatching: " + PathTracker.getInstance().getPath());
    }
    
	@Override
	public Path getPath() {
		return Path.GOOGLEANALYTICS;
	}
    
	public LocationTracker getLocationTracker() {
		if (locationTracker == null) {
			locationTracker = new LocationTracker(this);
		}
		return locationTracker;
	}
	
	private String getID() {
		return Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);
	}
	
//    protected PathTracker getPathTracker() {
//    	if (pathTracker == null) {
//    		pathTracker = PathTracker.getInstance();
//    	}
//    	return pathTracker;
//    }
//    
//    protected SessionTracker getSessionTracker() {
//    	if (sessionTracker == null) {
//    		sessionTracker = SessionTracker.getInstance();
//    	}
//    	return sessionTracker;
//    }

}
