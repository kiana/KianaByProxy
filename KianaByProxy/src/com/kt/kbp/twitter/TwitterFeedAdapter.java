package com.kt.kbp.twitter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import twitter4j.Status;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kt.kbp.R;

public class TwitterFeedAdapter extends ArrayAdapter<Status> {

	List<Status> twitterStatuses;
	LayoutInflater inflater;
	
	public TwitterFeedAdapter(Context context, int textViewResourceId, List<Status> objects) {
		super(context, textViewResourceId, objects);
		this.twitterStatuses = objects;
		inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	static class ViewHolder {
		public TextView tweet;
		public TextView date;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		if (row == null) {
			row = inflater.inflate(R.layout.twitter_row, null);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.date = (TextView) row.findViewById(R.id.date);
			viewHolder.tweet = (TextView) row.findViewById(R.id.tweet);
			
			row.setTag(viewHolder);
		}
		
		ViewHolder view = (ViewHolder) row.getTag();
		Status status = twitterStatuses.get(position);
		
		view.date.setText(formatDate(status.getCreatedAt()));
		view.tweet.setText(status.getText());
		
		return row;
	}
	
	private String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
		return formatter.format(date);
	}

}
