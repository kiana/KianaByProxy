package com.kt.kbp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.FragmentFactory;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;

public class MainFragment extends Fragment implements PathInterface {

	private Vibrator hapticFeedback;
	private View view;
		
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	view = inflater.inflate(R.layout.fragment_main, container, false);
        hapticFeedback = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        
        ImageView youtubeView = (ImageView) view.findViewById(R.id.img_youtube);
        youtubeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//category, action, label, value
				GoogleAnalyticsTracker.getInstance().trackEvent("Main", "Youtube", "Click", 0);
				hapticFeedback.vibrate(50);
				showFragment(Constants.YOUTUBE_FRAG, R.id.youtubefrag);
			}
		});
        
        ImageView flickrView = (ImageView) view.findViewById(R.id.img_flickr);
        flickrView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//category, action, label, value
				GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
				tracker.trackEvent("Main", "Flickr", "Click", 0);
				hapticFeedback.vibrate(50);
				showFragment(Constants.FLICKR_FRAG, R.id.flickrfrag);
			}
		});
        
        ImageView twitterView = (ImageView) view.findViewById(R.id.img_twitter);
        twitterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//category, action, label, value
				GoogleAnalyticsTracker.getInstance().trackEvent("Main", "Twitter", "Click", 0);
				hapticFeedback.vibrate(50);
				showFragment(Constants.TWITTER_FRAG, R.id.twitterfrag);
			}
		});
        
        ImageView bloggerView = (ImageView) view.findViewById(R.id.img_blogger);
        bloggerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//category, action, label, value
				GoogleAnalyticsTracker.getInstance().trackEvent("Main", "Blogger", "Click", 0);
				hapticFeedback.vibrate(50);
				showFragment(Constants.BLOGGER_FRAG, R.id.bloggerfrag);
			}
		});
        
        TextView charityView = (TextView) view.findViewById(R.id.charity_link);
        charityView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//category, action, label, value
				GoogleAnalyticsTracker.getInstance().trackEvent("Main", "Charity", "Click", 0);
				hapticFeedback.vibrate(50);
				showFragment(Constants.PAYPAL_FRAG, R.id.paypalfrag);
			}
		});
        
    	return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("fragments", "resuming MainFragment");
    }

	@Override
	public Path getPath() {
		return Path.MAIN;
	}

	protected void showFragment(String tag, int id) {
		Fragment fragment = getFragmentManager().findFragmentByTag(tag);
		if (fragment == null) {
			fragment = FragmentFactory.getNewFragment(id);
		}
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(Constants.CONTENT_VIEW_ID, fragment, tag);
		transaction.addToBackStack(tag);
		transaction.commit();
	}

}
