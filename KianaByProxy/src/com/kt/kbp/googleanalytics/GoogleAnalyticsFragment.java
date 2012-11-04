package com.kt.kbp.googleanalytics;

import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;
import com.kt.kbp.tracker.LocationTracker;
import com.kt.kbp.tracker.PathTracker;
import com.kt.kbp.tracker.SessionTracker;

import android.app.Fragment;
import android.util.Log;

public class GoogleAnalyticsFragment extends Fragment implements PathInterface {

	protected PathTracker pathTracker;
	protected LocationTracker locationTracker;
	protected SessionTracker sessionTracker;
	
    @Override
    public void onResume() {
    	super.onResume();
        if (getSessionTracker().shouldStartNewSession() 
        		&& getPathTracker().hasPathData()
        		&& getLocationTracker().hasLocation()) {
        	insertUserPathData();
        }
        
    	getPathTracker().add(getPath());
    	getSessionTracker().update();
    }
    
	@Override
	public Path getPath() {
		return Path.GOOGLEANALYTICS;
	}

    public void insertUserPathData() {
    	Log.i("userPathData", "dispatching: " + getPathTracker().getPath());
    	//new DBManagerTask().execute();
    }
    
	public LocationTracker getLocationTracker() {
		if (locationTracker == null) {
			locationTracker = new LocationTracker(getActivity());
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
}
