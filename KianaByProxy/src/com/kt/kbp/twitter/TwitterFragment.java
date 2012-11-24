package com.kt.kbp.twitter;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.StreamDrawableTask;
import com.kt.kbp.googleanalytics.GoogleAnalyticsListFragment;

public class TwitterFragment extends GoogleAnalyticsListFragment {

	private Twitter twitter = new TwitterFactory(new ConfigurationBuilder().build()).getInstance();
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	View view = inflater.inflate(R.layout.fragment_twitter, container, false);
    	return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
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
    		
        	ImageView imageView = (ImageView) getActivity().findViewById(R.id.twitter_image);
        	new StreamDrawableTask(user.getProfileImageURL()).execute(imageView);
        	
        	TextView handle = (TextView) getActivity().findViewById(R.id.handle);
        	handle.setText("@" + user.getScreenName());
        	
        	TextView description = (TextView) getActivity().findViewById(R.id.description);
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
    	ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
    	if (progressBar != null) {
    		progressBar.setVisibility(View.GONE);
    	}
    }
    
    protected void loadListAdapter(ResponseList<Status> statuses) {
    	final ListView listView = getListView();
    	TwitterFeedAdapter adapter = new TwitterFeedAdapter(getActivity(), R.layout.twitter_row, statuses);
    	listView.setAdapter(adapter);
    	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Status status = (Status)listView.getItemAtPosition(position);
				trackEvent("Twitter", "Click", Long.toString(status.getId()), 0);
			}
    	});
    }
}
