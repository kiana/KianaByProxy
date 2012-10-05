package com.kt.kbp.flickr;

import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kt.kbp.R;
import com.kt.kbp.common.StreamDrawableTask;

public class ShowPhotoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        
		TextView back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ShowPhotoActivity.this, FlickrActivity.class));
			}
		});
        
        String url = getIntent().getStringExtra("photoUrl");
        String photoTitle = getIntent().getStringExtra("photoTitle");

        TextView title = (TextView) findViewById(R.id.flickr_label);
        ImageView flickrImage = (ImageView) findViewById(R.id.flickrimage);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        
        title.setText(photoTitle);
        
        try {
			new StreamDrawableTask(url, progressBar).execute(flickrImage);
		} catch (MalformedURLException e) {
			e.printStackTrace(); // TODO WEBANALYTICS
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_photo, menu);
        return true;
    }

    
}
