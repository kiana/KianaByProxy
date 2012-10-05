package com.kt.kbp.blogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kt.kbp.R;

public class BlogEntryListAdapter extends ArrayAdapter<BlogEntry>{

	private final List<BlogEntry> blogEntries;
	private LayoutInflater inflater;
	
	public BlogEntryListAdapter(Context context, int textViewResourceId,
			List<BlogEntry> objects) {
		super(context, textViewResourceId, objects);
		blogEntries = objects;
		inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	static class ViewHolder {
		public TextView title;
		public TextView date;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		if (row == null) {
			row = inflater.inflate(R.layout.blogger_row, null);
			
			ViewHolder viewHolder = new ViewHolder();
			
			viewHolder.title = (TextView) row.findViewById(R.id.title);
			viewHolder.date = (TextView) row.findViewById(R.id.date);
			
			row.setTag(viewHolder);
		}
		
		ViewHolder viewHolder = (ViewHolder) row.getTag();
		BlogEntry entry = blogEntries.get(position);
		viewHolder.title.setText(entry.getTitle());
		viewHolder.date.setText(formatDate(entry.getDatePublished()));
		return row;
	}
	
	//TODO pick your battles. Also in BloggerActivity
	private String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(date);
	}
}
