package com.kt.kbp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.gmail.yuyang226.flickr.photos.Photo;
import com.keyes.youtube.OpenYouTubePlayerFragment;
import com.kt.kbp.blogger.BlogEntry;
import com.kt.kbp.blogger.BloggerFragment.OnBlogEntrySelectedListener;
import com.kt.kbp.blogger.ShowBlogEntryFragment;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.FragmentHandler;
import com.kt.kbp.flickr.ShowPhotoFragment;
import com.kt.kbp.flickr.ShowPhotoFragment.OnPhotoSelectedListener;
import com.kt.kbp.youtube.YoutubeFragment.OnVideoSelectedListener;

public class MainActivity extends FragmentActivity implements OnBlogEntrySelectedListener, OnPhotoSelectedListener, OnVideoSelectedListener {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_frame, new MainFragment(), Constants.MAIN_FRAG);
		transaction.addToBackStack(Constants.MAIN_FRAG);
		transaction.commit();
		getSupportFragmentManager().executePendingTransactions();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onPhotoSelected(Photo photo) {
		Fragment showPhotoFragment = ShowPhotoFragment.newInstance(photo);
		
		FragmentHandler.showFragment(getSupportFragmentManager(), 
				Constants.SHOW_PHOTO_FRAG, 
				showPhotoFragment);
	}

	@Override
	public void onBlogEntrySelected(BlogEntry entry, int position) {
		Fragment showBlogEntryFragment = ShowBlogEntryFragment.newInstance(entry);
		FragmentHandler.showFragment(getSupportFragmentManager(), 
				Constants.SHOW_BLOG_ENTRY_FRAG, 
				showBlogEntryFragment);
		
	}

	@Override
	public void onVideoSelected(String youtubeId) {
		
		getIntent().setData(Uri.parse("ytv://" + youtubeId));
		
		Fragment showVideoFragment = new OpenYouTubePlayerFragment();
		FragmentHandler.showFragment(getSupportFragmentManager(), 
				Constants.SHOW_VIDEO_FRAG, 
				showVideoFragment);
	} 
}
