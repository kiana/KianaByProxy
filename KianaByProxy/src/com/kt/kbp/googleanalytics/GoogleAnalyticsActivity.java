package com.kt.kbp.googleanalytics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.kt.kbp.common.Constants;
import com.kt.kbp.tracker.PathTracker;

public class GoogleAnalyticsActivity extends FragmentActivity {

	protected Tracker tracker;
	//protected LocationTracker locationTracker;
	protected GoogleAnalytics googleAnalytics;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getApplicationContext());
        tracker = googleAnalytics.getTracker(Constants.GA_TRACKING_ID);
        
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				dumpExceptionReport(ex.getMessage());
			}
		});
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	PathTracker.getInstance().clearPath();
    }
    
    protected void showExceptionDialog(Throwable ex) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
        builder.setMessage(ex.getMessage())
               .setPositiveButton("Send", new ExceptionSender(ex))
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });

        builder.create().show();
    }
    
    protected class ExceptionSender implements OnClickListener {

    	private Throwable throwable;
    	
    	public ExceptionSender(Throwable throwable) {
    		this.throwable = throwable;
    	}
    	
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			StringBuilder emailContent = new StringBuilder();
			if (PathTracker.getInstance().hasPathData()) {
				emailContent.append(PathTracker.getInstance().getPathStringBuilder());
				emailContent.append("\n");
			}
			emailContent.append(throwable.getMessage());
   			final Intent intent = new Intent(Intent.ACTION_SEND);
		    intent.setType("plain/text");
		    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kianabyproxy@gmail.com"});
		    intent.putExtra(Intent.EXTRA_SUBJECT, "AnDevConIV: Exception Thrown");
		    intent.putExtra(Intent.EXTRA_TEXT, emailContent.toString());
		    startActivity(intent);
		}
    	
    }
    
    private void dumpExceptionReport(String message) {
    	try {		

    		File file = new File(Environment.getExternalStorageDirectory(), "kianabyproxy-exceptionreport.txt");
    		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

    		if (PathTracker.getInstance().hasPathData()) {
    			writer.write(PathTracker.getInstance().getPathStringBuilder().toString());
    			writer.newLine();
    		}
    		writer.write(message);
    		writer.flush();
    		writer.close();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
}
