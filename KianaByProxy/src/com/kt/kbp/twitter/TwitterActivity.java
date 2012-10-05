package com.kt.kbp.twitter;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kt.kbp.MainActivity;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.StreamDrawableTask;

public class TwitterActivity extends ListActivity {
	
	Twitter twitter = new TwitterFactory(new ConfigurationBuilder().build()).getInstance();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        
		TextView back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(TwitterActivity.this, MainActivity.class));
			}
		});
		
        new GetTimeLineTask().execute();
        new GetUserTask().execute();
    }
    
    private class GetUserTask extends AsyncTask<Void, Void, User> {

		@Override
		protected User doInBackground(Void... params) {
			try {
				return twitter.showUser(Constants.TWITTER_USERNAME);
			} catch (TwitterException e) {
				e.printStackTrace(); //TODO WEBANALYTICS STUFF
			}
			return null;
		}
    	
		protected void onPostExecute(User user) {
			setUserInfo(user);
		}
    }
    
    protected void setUserInfo(User user) {
    	if (user != null) {
        	ImageView imageView = (ImageView) findViewById(R.id.twitter_image);
        	new StreamDrawableTask(user.getProfileImageURL()).execute(imageView);
        	
        	TextView handle = (TextView) findViewById(R.id.handle);
        	handle.setText("@" + user.getScreenName());
        	
        	TextView description = (TextView) findViewById(R.id.description);
        	description.setText(user.getDescription());
    	}
    }
    
    private class GetTimeLineTask extends AsyncTask<Void, Void, ResponseList<Status>> {
		@Override
		protected ResponseList<twitter4j.Status> doInBackground(Void... args) {
			ResponseList<twitter4j.Status> statuses = null;
	        try {
				statuses = twitter.getUserTimeline(Constants.TWITTER_USERNAME);
			} catch (TwitterException e) {
				e.printStackTrace(); //TODO WEBANALYTICS STUFF
			}
	        return statuses;
		}
    	
		protected void onPostExecute(ResponseList<twitter4j.Status> statuses) {
			hideProgressBar();
			loadListAdapter(statuses);
		}
    }

    protected void hideProgressBar() {
    	ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
    	progressBar.setVisibility(View.GONE);
    }
    
    protected void loadListAdapter(ResponseList<Status> statuses) {
    	ListView listView = getListView();
    	TwitterFeedAdapter adapter = new TwitterFeedAdapter(this, R.layout.twitter_row, statuses);
    	listView.setAdapter(adapter);
    	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//TODO WEBANALYTICS
			}
    	});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_twitter, menu);
        return true;
    }

    
}
