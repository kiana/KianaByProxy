package com.kt.kbp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.kt.kbp.blogger.BloggerActivity;
import com.kt.kbp.flickr.FlickrActivity;
import com.kt.kbp.twitter.TwitterActivity;
import com.kt.kbp.youtube.YoutubeActivity;

public class MainActivity extends Activity {
	
	private Vibrator hapticFeedback;
		
	//TODO cleanup drawable directories
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hapticFeedback = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        setContentView(R.layout.activity_main);
        
        ImageView youtubeView = (ImageView) findViewById(R.id.img_youtube);
        youtubeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				hapticFeedback.vibrate(50);
				Intent i = new Intent(view.getContext(), YoutubeActivity.class);
				startActivity(i);
			}
		});
        
        ImageView flickrView = (ImageView) findViewById(R.id.img_flickr);
        flickrView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				hapticFeedback.vibrate(50);
				Intent i = new Intent(view.getContext(),FlickrActivity.class);
				startActivity(i);
			}
		});
        
        ImageView twitterView = (ImageView) findViewById(R.id.img_twitter);
        twitterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				hapticFeedback.vibrate(50);
				Intent i = new Intent(view.getContext(),TwitterActivity.class);
				startActivity(i);
			}
		});
        
        ImageView bloggerView = (ImageView) findViewById(R.id.img_blogger);
        bloggerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				hapticFeedback.vibrate(50);
				Intent i = new Intent(view.getContext(), BloggerActivity.class);
				startActivity(i);
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    } 
}
