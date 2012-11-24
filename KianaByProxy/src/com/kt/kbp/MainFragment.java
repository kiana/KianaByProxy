package com.kt.kbp;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kt.kbp.common.Constants;
import com.kt.kbp.common.FragmentFactory;
import com.kt.kbp.googleanalytics.GoogleAnalyticsFragment;

public class MainFragment extends GoogleAnalyticsFragment {

	private Vibrator hapticFeedback;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	View view = inflater.inflate(R.layout.fragment_main, container, false);
        hapticFeedback = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        
        ImageView youtubeView = (ImageView) view.findViewById(R.id.img_youtube);
        youtubeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/youtube");
				hapticFeedback.vibrate(50);
				showFragment(Constants.YOUTUBE_FRAG, R.id.youtubefrag);
			}
		});
        
        ImageView flickrView = (ImageView) view.findViewById(R.id.img_flickr);
        flickrView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/flickr");
				hapticFeedback.vibrate(50);
				showFragment(Constants.FLICKR_FRAG, R.id.flickrfrag);
			}
		});
        
        ImageView twitterView = (ImageView) view.findViewById(R.id.img_twitter);
        twitterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/twitter");
				hapticFeedback.vibrate(50);
				showFragment(Constants.TWITTER_FRAG, R.id.twitterfrag);
			}
		});
        
        ImageView bloggerView = (ImageView) view.findViewById(R.id.img_blogger);
        bloggerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/blogger");
				hapticFeedback.vibrate(50);
				showFragment(Constants.BLOGGER_FRAG, R.id.bloggerfrag);
			}
		});
        
        TextView charityView = (TextView) view.findViewById(R.id.charity_link);
        charityView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				trackPageView("/paypal");
				hapticFeedback.vibrate(50);
				showFragment(Constants.PAYPAL_FRAG, R.id.paypalfrag);
			}
		});
        
    	return view;
    }

	protected void showFragment(String tag, int id) {
		Fragment fragment = getFragmentManager().findFragmentByTag(tag);
		if (fragment == null) {
			fragment = FragmentFactory.getNewFragment(id);
		}
		trackerUpdate(tag);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_frame, fragment, tag);
		transaction.addToBackStack(tag);
		transaction.commit();
	}
}
