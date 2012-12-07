package com.kt.kbp.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kt.kbp.MainFragment;
import com.kt.kbp.R;
import com.kt.kbp.blogger.BloggerFragment;
import com.kt.kbp.flickr.FlickrFragment;
import com.kt.kbp.twitter.TwitterFragment;
import com.kt.kbp.youtube.YoutubeFragment;

public class FragmentHandler {

    public static void handleFragment(FragmentManager fragmentManager, String tag, int id) {
    	showFragment(fragmentManager, tag, findFragment(fragmentManager, tag, id));
    }
 
    public static Fragment findFragment(FragmentManager fragmentManager, String tag, int id) {
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		if (fragment == null) {
			fragment = FragmentHandler.getNewFragment(id);
		}
		return fragment;
    }
    
	public static void showFragment(FragmentManager fragmentManager, String tag, Fragment fragment) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_frame, fragment, tag);
		transaction.addToBackStack(tag);
		transaction.commit();
		fragmentManager.executePendingTransactions();
	} 
	
	public static Fragment getNewFragment(int fragmentType) {
		switch (fragmentType) {
		case R.id.mainfrag:
			return new MainFragment();
		case R.id.bloggerfrag:
			return new BloggerFragment();
		case R.id.flickrfrag:
			return new FlickrFragment();
		case R.id.twitterfrag:
			return new TwitterFragment();
		case R.id.youtubefrag:
			return new YoutubeFragment();
		default:
			return new MainFragment();
		}
	}
}
