package com.kt.kbp.youtube;

import java.net.MalformedURLException;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kt.kbp.R;
import com.kt.kbp.common.StreamDrawableTask;

public class VideoListAdapter extends ArrayAdapter<YoutubeEntry> {

	private final List<YoutubeEntry> entries;
	private LayoutInflater inflater;
	
	public VideoListAdapter(Context context, int textViewResourceId,
			List<YoutubeEntry> objects) {
		super(context, textViewResourceId, objects);
		this.entries = objects;
		inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	static class ViewHolder {
		public ProgressBar progressBar;
		public ImageView thumbnail;
		public TextView title;
		public TextView description;
		public TextView viewCount;
		public TextView duration;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.youtube_row, null);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
			viewHolder.thumbnail = (ImageView) rowView.findViewById(R.id.youtube_image);
			viewHolder.title = (TextView) rowView.findViewById(R.id.title);
			viewHolder.description = (TextView) rowView.findViewById(R.id.description);
			viewHolder.duration = (TextView) rowView.findViewById(R.id.duration);
			viewHolder.viewCount = (TextView) rowView.findViewById(R.id.viewCount);
			
			rowView.setTag(viewHolder);
		}
		
		ViewHolder view = (ViewHolder) rowView.getTag();
		YoutubeEntry entry = entries.get(position);
		view.description.setText(entry.getMediaGroup().getDescription());
		view.duration.setText(entry.getMediaGroup().getDuration());
		view.title.setText(entry.getMediaGroup().getTitle());
		view.viewCount.setText(entry.getViewCount());
		
		//set thumbnail on imageview
		try {
			new StreamDrawableTask(entry.getMediaGroup().getThumbNailUrl(), view.progressBar).execute(view.thumbnail);
		} catch (MalformedURLException e) {
			e.printStackTrace(); // TODO WEBANALYTICS
		}
		
		return rowView;
	}
	
}
