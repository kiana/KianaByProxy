package com.kt.kbp.flickr;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.kt.kbp.MainActivity;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;

public class FlickrActivity extends Activity {

	private GridView gridView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);
        
        
		TextView back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FlickrActivity.this, MainActivity.class));
			}
		});

        gridView = (GridView) findViewById(R.id.gridview);
        new StreamPhotoListTask().execute(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_flickr, menu);
        return true;
    }
    
    private class StreamPhotoListTask extends AsyncTask<Context, Void, PhotoList> {

    	private Context context;
    	
		@Override
		protected PhotoList doInBackground(Context... contexts) {
			this.context = contexts[0];
			
			PhotoList photoList = new PhotoList();
			
			Flickr flickr = new Flickr(Constants.FLICKR_KEY);
			try {
				photoList = flickr.getPeopleInterface().getPublicPhotos(Constants.FLICKR_USERID, 100, 1);
			} catch (IOException e) {
				// TODO WEBANALYTICS - track error
				e.printStackTrace();
			} catch (FlickrException e) {
				// TODO WEBANALYTICS - track error
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO WEBANALYTICS - track error
				e.printStackTrace();
			}
			return photoList;
		}
    	
		@Override
		protected void onPostExecute(final PhotoList photoList) {
			PhotoGridAdapter adapter = new PhotoGridAdapter(context, R.layout.flickr_griditem, photoList);
			
			gridView.setAdapter(adapter);
			
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Photo photo = (Photo) gridView.getItemAtPosition(position);
					//TODO be able to pass the photo into the ShowPhotoActivity
					Intent i = new Intent(FlickrActivity.this, ShowPhotoActivity.class);
					i.putExtra("photoUrl", photo.getLargeUrl());
					i.putExtra("photoTitle", photo.getTitle());
					startActivity(i);
				}
			});
		}
    }
    
}
