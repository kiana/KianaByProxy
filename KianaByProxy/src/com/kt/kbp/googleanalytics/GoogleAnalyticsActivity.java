package com.kt.kbp.googleanalytics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.kt.kbp.common.Constants;
import com.kt.kbp.tracker.LocationTracker;
import com.kt.kbp.tracker.PathTracker;

public class GoogleAnalyticsActivity extends FragmentActivity {

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
        loadLocationTracker();
    }
    
    @Override
    protected void onUserLeaveHint() {
    	super.onUserLeaveHint();
		Log.i("fragments", "onuserleavehint, you wanna save user actions");
        if (PathTracker.getInstance().hasPathData() && getLocationTracker().hasLocation()) {
        	Log.i("fragments", "onUserLeaveHint: GoogleAnalyticsActivity. Send data now.");
        	dumpReport();
        	PathTracker.getInstance().clearPath();
        }
    }
	
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.i("stopping", "check do we go through this method when pressing BACK button or pressing HOME button");
    	GoogleAnalyticsTracker.getInstance().dispatch();
    	GoogleAnalyticsTracker.getInstance().stopSession();
    }
    
	public void loadLocationTracker() {
		if (locationTracker == null) {
			locationTracker = new LocationTracker(this);
		}
	}
	
	public LocationTracker getLocationTracker() {
		if (locationTracker == null) {
			loadLocationTracker();
		}
		return locationTracker;
	}
	
	private String getID() {
		return Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);
	}
	
	private void dumpReport() {
	    try {
	        File file = new File(Environment.getExternalStorageDirectory(), "kianabyproxy-" + System.currentTimeMillis() + ".txt");
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	        
	        writer.write("DeviceId: " + getID());
	        writer.newLine();
	        writer.write("Location: " + getLocationTracker().getLocation().getLatitude() 
					+ "+" + getLocationTracker().getLocation().getLongitude());
	        writer.newLine();
	        writer.write("Path: ");
	        writer.newLine();

	        List<String> stops = PathTracker.getInstance().getPath();
	        for (String stop : stops) {
	        	writer.write(stop);
	        	writer.newLine();
	        }


	        writer.flush();
	        writer.close();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
