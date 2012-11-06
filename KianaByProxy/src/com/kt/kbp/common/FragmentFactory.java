package com.kt.kbp.common;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kt.kbp.MainFragment;
import com.kt.kbp.R;
import com.kt.kbp.blogger.BloggerFragment;
import com.kt.kbp.blogger.ShowBlogEntryFragment;
import com.kt.kbp.flickr.FlickrFragment;
import com.kt.kbp.flickr.ShowPhotoFragment;
import com.kt.kbp.paypal.CancelledFragment;
import com.kt.kbp.paypal.FailedFragment;
import com.kt.kbp.paypal.PaypalFragment;
import com.kt.kbp.twitter.TwitterFragment;
import com.kt.kbp.youtube.YoutubeFragment;

public class FragmentFactory {

	private static List<Integer> fragments;
	static {
		fragments = new ArrayList<Integer>();
		fragments.add(R.id.bloggerfrag);
		fragments.add(R.id.cancelledfrag);
		fragments.add(R.id.failedfrag);
		fragments.add(R.id.flickrfrag);
		fragments.add(R.id.mainfrag);
		fragments.add(R.id.paypalfrag);
		fragments.add(R.id.showblogentryfrag);
		fragments.add(R.id.showphotofrag);
		fragments.add(R.id.succeededfrag);
		fragments.add(R.id.twitterfrag);
		fragments.add(R.id.youtubefrag);
	}
	
	public static void toggle(FragmentManager manager, int fragmentToShow) {
		FragmentTransaction transaction = manager.beginTransaction();
		for (Integer id : fragments) {
			if (id == fragmentToShow) {
				Fragment fragment = manager.findFragmentById(fragmentToShow);
				if (fragment != null && fragment.isHidden()) {
					transaction.show(fragment);
				}
			} else {
				
			}
		}
	}
	
	public static Fragment getNewFragment(int fragmentType) {
		switch (fragmentType) {
		case R.id.bloggerfrag:
			return new BloggerFragment();
		case R.id.cancelledfrag:
			return new CancelledFragment();
		case R.id.failedfrag:
			return new FailedFragment();
		case R.id.flickrfrag:
			return new FlickrFragment();
		case R.id.mainfrag:
			return new MainFragment();
		case R.id.paypalfrag:
			return new PaypalFragment();
		case R.id.showblogentryfrag:
			return new ShowBlogEntryFragment();
		case R.id.showphotofrag:
			return new ShowPhotoFragment();
		case R.id.twitterfrag:
			return new TwitterFragment();
		case R.id.youtubefrag:
			return new YoutubeFragment();
		default:
			return new MainFragment();
		}
	}
}
