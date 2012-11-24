package com.kt.kbp.blogger;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kt.kbp.R;
import com.kt.kbp.googleanalytics.GoogleAnalyticsFragment;

public class ShowBlogEntryFragment extends GoogleAnalyticsFragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	View view = inflater.inflate(R.layout.fragment_show_blog_entry, container, false);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView date = (TextView) view.findViewById(R.id.date_published);
        TextView content = (TextView) view.findViewById(R.id.content);
        
        BlogEntry entry = (BlogEntry) getArguments().getSerializable("entry");
        title.setText(entry.getTitle());
        date.setText(formatDate(entry.getDatePublished()));
        
        content.setText(Html.fromHtml(entry.getContent()));
        content.setMovementMethod(new ScrollingMovementMethod());
        
    	return view;
    }
	
	private String formatDate(Date date) {
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	return formatter.format(date);
	}
	
	public String getEntryId() {
		return getArguments().getString("id");
	}

	public static ShowBlogEntryFragment newInstance(BlogEntry entry) {
		ShowBlogEntryFragment showBlogEntryFragment = new ShowBlogEntryFragment();
		Bundle bundle = new Bundle();
		bundle.putString("id", entry.getId());
		bundle.putSerializable("entry", entry);
		showBlogEntryFragment.setArguments(bundle);
		return showBlogEntryFragment;
	}
}
