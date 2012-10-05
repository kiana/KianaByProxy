package com.kt.kbp.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.keyes.youtube.OpenYouTubePlayerActivity;
import com.kt.kbp.MainActivity;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.UrlConverter;

public class YoutubeActivity extends ListActivity {
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.activity_youtube);
		
		TextView back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(YoutubeActivity.this, MainActivity.class));
			}
		});
		
        loadVideos(Constants.YOUTUBE_URL);
    }
    
    //TODO Manage Network Usage
    public void loadVideos(String url) {
    	if (isConnected(ConnectivityManager.TYPE_WIFI) || isConnected(ConnectivityManager.TYPE_MOBILE)) {
    		new ParseYoutubeFeedTask().execute(url);
    	} else {
    		Toast.makeText(this, "Network unavailable to load videos.", Toast.LENGTH_LONG).show();
    	}
    }
    
    protected boolean isConnected(int connectionType) {
    	ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = connectivityManager.getNetworkInfo(connectionType);
    	return networkInfo != null && networkInfo.isConnected();
    }
    
    protected void playVideo(String youtubeId) {
        startActivity(useOpenSourcePlayer(youtubeId));
    }
    
    protected Intent useOpenSourcePlayer(String youtubeId) {
    	return new Intent(null, Uri.parse("ytv://"+youtubeId), this, OpenYouTubePlayerActivity.class);
    }
    
    /*
     * In case the other player breaks, use this.
     */
    protected Intent useYoutubePlayer(String youtubeId) {
    	return new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeId));
    }
    
    //TODO show progress on page while loading
    private class ParseYoutubeFeedTask extends AsyncTask<String, Void, List<YoutubeEntry>> {

		@Override
		protected List<YoutubeEntry> doInBackground(String... urls) {
			try {
				return getYoutubeEntries(urls[0]);
			} catch (XmlPullParserException e) {
				return null; //TODO WEBANALYTICS - track error
			} catch (IOException e) {
				return null; //TODO WEBANALYTICS - track error
			}
		}
    	
		@Override
		protected void onPostExecute(List<YoutubeEntry> entries) {
			loadListAdapter(entries);
		}
		
	    private List<YoutubeEntry> getYoutubeEntries(String url) throws XmlPullParserException, IOException {
	    	InputStream inputStream = null;
	    	YoutubeXmlParser parser = new YoutubeXmlParser();
	    	List<YoutubeEntry> youtubeEntries = null;
	      
	        try {
	        	inputStream = UrlConverter.downloadUrl(url);
	        	youtubeEntries = parser.parse(inputStream);
	        } finally {
	        	if (inputStream != null) {
	        		inputStream.close();
	        	}
	        }
	        return youtubeEntries;
	    }
    }
    
    protected void loadListAdapter(List<YoutubeEntry> entries) {
		final ListView listView = getListView();
		VideoListAdapter adapter = new VideoListAdapter(this, R.layout.youtube_row, entries);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				YoutubeEntry entry = (YoutubeEntry) listView.getItemAtPosition(position);
				playVideo(entry.getId());
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_youtube, menu);
        return true;
    }
}
