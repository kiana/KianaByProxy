package com.kt.kbp.twitter;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.ExceptionTrackerInterface;
import com.kt.kbp.common.StreamDrawableTask;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;

public class TwitterFragment extends ListFragment implements ExceptionTrackerInterface, PathInterface {

	private Twitter twitter = new TwitterFactory(new ConfigurationBuilder().build()).getInstance();
	private View view;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	view = inflater.inflate(R.layout.fragment_twitter, container, false);
    	return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("fragments", "resuming TwitterFragment");
        new GetTimelineTask().execute();
        new GetUserTask().execute();
    }
    
    private class GetUserTask extends AsyncTask<Void, Void, User> {

		@Override
		protected User doInBackground(Void... params) {
			try {
				return twitter.showUser(Constants.TWITTER_USERNAME);
			} catch (TwitterException e) {
				trackException("Twitter|GetUser", e.getMessage());
			}
			return null;
		}
    	
		protected void onPostExecute(User user) {
			setUserInfo(user);
		}
    }
    
    protected void setUserInfo(User user) {
    	if (user != null) {
    		
        	ImageView imageView = (ImageView) view.findViewById(R.id.twitter_image);
        	new StreamDrawableTask(user.getProfileImageURL()).execute(imageView);
        	
        	TextView handle = (TextView) view.findViewById(R.id.handle);
        	handle.setText("@" + user.getScreenName());
        	
        	TextView description = (TextView) view.findViewById(R.id.description);
        	description.setText(user.getDescription());
    	}
    }
    
    private class GetTimelineTask extends AsyncTask<Void, Void, ResponseList<Status>> {
		@Override
		protected ResponseList<twitter4j.Status> doInBackground(Void... args) {
			ResponseList<twitter4j.Status> statuses = null;
	        try {
				statuses = twitter.getUserTimeline(Constants.TWITTER_USERNAME);
			} catch (TwitterException e) {
				trackException("Twitter|GetTimeline", e.getMessage());
			}
	        return statuses;
		}
    	
		protected void onPostExecute(ResponseList<twitter4j.Status> statuses) {
			hideProgressBar();
			loadListAdapter(statuses);
		}
    }

    protected void hideProgressBar() {
    	ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    	progressBar.setVisibility(View.GONE);
    }
    
    protected void loadListAdapter(ResponseList<Status> statuses) {
    	final ListView listView = getListView();
    	TwitterFeedAdapter adapter = new TwitterFeedAdapter(view.getContext(), R.layout.twitter_row, statuses);
    	listView.setAdapter(adapter);
    	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Status status = (Status)listView.getItemAtPosition(position);
				GoogleAnalyticsTracker.getInstance().trackEvent("Twitter", "Click|Tweet", Long.toString(status.getId()), 0);
			}
    	});
    }
    
	@Override
	public Path getPath() {
		return Path.TWITTER;
	}

	public void trackException(String category, String message) {
		//category, action, label, value
		GoogleAnalyticsTracker.getInstance().trackEvent(category, "Exception", message, 0);
	}
	
}
