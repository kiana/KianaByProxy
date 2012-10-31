package com.kt.kbp.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.keyes.youtube.OpenYouTubePlayerActivity;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.ExceptionTrackerInterface;
import com.kt.kbp.common.UrlConverter;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;

public class YoutubeFragment extends ListFragment  implements ExceptionTrackerInterface, PathInterface {

	private View view;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	view = inflater.inflate(R.layout.fragment_youtube, container, false);
    	
        TextView back = (TextView) view.findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				Fragment mainFragment = fm.findFragmentByTag(Constants.MAIN_FRAG);
				transaction.show(mainFragment);
				transaction.hide(fm.findFragmentByTag(Constants.YOUTUBE_FRAG));
				transaction.commit();
			}
		});
    	
    	return view;
    }
    
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("fragments", "resuming YoutubeFragment");
    	loadVideos(Constants.YOUTUBE_URL);
    }
    
    public void loadVideos(String url) {
    	new ParseYoutubeFeedTask().execute(url);
    }
    
    protected boolean isConnected(int connectionType) {
    	ConnectivityManager connectivityManager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = connectivityManager.getNetworkInfo(connectionType);
    	return networkInfo != null && networkInfo.isConnected();
    }
    
    protected void playVideo(String youtubeId) {
    	if (isConnected(ConnectivityManager.TYPE_WIFI)) {
    		GoogleAnalyticsTracker.getInstance().trackEvent("Youtube", "Watch", youtubeId, 0);
            startActivity(useOpenSourcePlayer(youtubeId));
    	} else {
    		Toast.makeText(view.getContext(), "Please connect to wifi to view video.", Toast.LENGTH_LONG).show();
    	}
    }
    
    protected Intent useOpenSourcePlayer(String youtubeId) {
    	//TODO log activity in PathTracker
    	//TODO turn Activity into a Fragment
    	return new Intent(null, Uri.parse("ytv://"+youtubeId), view.getContext(), OpenYouTubePlayerActivity.class);
    }
    
    /*
     * In case the other player breaks, use this.
     */
    protected Intent useYoutubePlayer(String youtubeId) {
    	return new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeId));
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
		VideoListAdapter adapter = new VideoListAdapter(view.getContext(), R.layout.youtube_row, entries);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				YoutubeEntry entry = (YoutubeEntry) listView.getItemAtPosition(position);
				GoogleAnalyticsTracker.getInstance().trackEvent("Youtube", "Click|Video", entry.getId(), 0);
				playVideo(entry.getId());
			}
		});
    }
    
	@Override
	public Path getPath() {		
		return Path.YOUTUBE;
	}

	@Override
	public void trackException(String category, String message) {
		//category, action, label, value
		GoogleAnalyticsTracker.getInstance().trackEvent(category, "Exception", message, 0);
	}

}
