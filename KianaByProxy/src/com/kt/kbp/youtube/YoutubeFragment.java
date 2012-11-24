package com.kt.kbp.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.UrlConverter;
import com.kt.kbp.googleanalytics.GoogleAnalyticsListFragment;

public class YoutubeFragment extends GoogleAnalyticsListFragment {

	private OnVideoSelectedListener videoSelectedListener;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	return inflater.inflate(R.layout.fragment_youtube, container, false);
    }
    
    
    @Override
    public void onResume() {
    	super.onResume();
    	loadVideos(Constants.YOUTUBE_URL);
    }
    
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	try {
    		videoSelectedListener = (OnVideoSelectedListener) activity;
    	} catch (ClassCastException e) {
    		throw new ClassCastException(activity.toString() + " must implement OnVideoSelectedListener");
    	}
    }
    
    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
    	YoutubeEntry entry = (YoutubeEntry) listView.getItemAtPosition(position);
    	if (isConnected(ConnectivityManager.TYPE_WIFI)) {
    		trackEvent("Youtube", "Click", entry.getId(), 0);
    		trackerUpdate("youtube:" + entry.getId());
        	videoSelectedListener.onVideoSelected(entry.getId());
    	} else {
    		Toast.makeText(getActivity(), "Please connect to wifi to view video.", Toast.LENGTH_LONG).show();
    	}
    }
    
    public void loadVideos(String url) {
    	new ParseYoutubeFeedTask().execute(url);
    }
    
    protected boolean isConnected(int connectionType) {
    	ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = connectivityManager.getNetworkInfo(connectionType);
    	return networkInfo != null && networkInfo.isConnected();
    }

    private class ParseYoutubeFeedTask extends AsyncTask<String, Void, List<YoutubeEntry>> {

		@Override
		protected List<YoutubeEntry> doInBackground(String... urls) {
			try {
				return getYoutubeEntries(urls[0]);
			} catch (XmlPullParserException e) {
				trackException("Youtube", e.getMessage());
				return null;
			} catch (IOException e) {
				trackException("Youtube", e.getMessage());
				return null;
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
		VideoListAdapter adapter = new VideoListAdapter(getActivity(), R.layout.youtube_row, entries);
		listView.setAdapter(adapter);
    }
	
	public interface OnVideoSelectedListener {
		public void onVideoSelected(String youtubeId);
	}
}
