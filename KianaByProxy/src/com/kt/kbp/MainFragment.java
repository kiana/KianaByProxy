package com.kt.kbp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.kbp.common.Constants;
import com.kt.kbp.googleanalytics.GoogleAnalyticsFragment;
import com.kt.kbp.tracker.PathTracker;

public class MainFragment extends GoogleAnalyticsFragment {

	private Vibrator hapticFeedback;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	loadLocationTracker();
    	
    	View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
        });
        
    	hapticFeedback = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        
        ImageView youtubeView = (ImageView) view.findViewById(R.id.img_youtube);
        youtubeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/youtube");
				hapticFeedback.vibrate(50);
				handleFragment(Constants.YOUTUBE_FRAG, R.id.youtubefrag);
			}
		});
        
        ImageView flickrView = (ImageView) view.findViewById(R.id.img_flickr);
        flickrView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/flickr");
				hapticFeedback.vibrate(50);
				handleFragment(Constants.FLICKR_FRAG, R.id.flickrfrag);
			}
		});
        
        ImageView twitterView = (ImageView) view.findViewById(R.id.img_twitter);
        twitterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/twitter");
				hapticFeedback.vibrate(50);
				handleFragment(Constants.TWITTER_FRAG, R.id.twitterfrag);
			}
		});
        
        ImageView bloggerView = (ImageView) view.findViewById(R.id.img_blogger);
        bloggerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/blogger");
				hapticFeedback.vibrate(50);
				handleFragment(Constants.BLOGGER_FRAG, R.id.bloggerfrag);
			}
		});
        
        TextView charityView = (TextView) view.findViewById(R.id.charity_link);
        charityView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/paypal");
				hapticFeedback.vibrate(50);
				handleFragment(Constants.PAYPAL_FRAG, R.id.paypalfrag);
			}
		});
        
        
       TextView exportView = (TextView) view.findViewById(R.id.report_link);
       exportView.setOnClickListener(new OnClickListener() {
    	   @Override
    	   public void onClick(View view) {
    		   trackEvent("Main", "Export", "Report", 0);
    		   hapticFeedback.vibrate(50);
    		   dumpReport();
    	   }
       });

       TextView feedBackView = (TextView) view.findViewById(R.id.feedback_link);
       feedBackView.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			trackEvent("Main", "Feedback", "Email", 0);
			final Intent intent = new Intent(Intent.ACTION_SEND);
		    intent.setType("plain/text");
		    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kianabyproxy@gmail.com"});
		    intent.putExtra(Intent.EXTRA_SUBJECT, "AnDevConIV: Feedback");
		    intent.putExtra(Intent.EXTRA_TEXT, "");
		    startActivity(intent);
		}
	});
       
    	return view;
    }
	
	private String getID() {
		return Secure.getString(getActivity().getContentResolver(),
                Secure.ANDROID_ID);
	}
	
	public void dumpReport() {
		try {
			String toastReport = "No tracking data to report.";
			if (PathTracker.getInstance().hasPathData() && getLocationTracker().hasLocation()) {
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
				toastReport = "Report successfully exported.";
			} 
			Toast.makeText(getActivity(), toastReport, Toast.LENGTH_LONG).show();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
