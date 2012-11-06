package com.kt.kbp.blogger;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.UrlConverter;
import com.kt.kbp.googleanalytics.GoogleAnalyticsListFragment;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;

public class BloggerFragment extends GoogleAnalyticsListFragment implements PathInterface {

	private View view;
	private OnBlogEntrySelectedListener blogEntrySelectedListener;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	view = inflater.inflate(R.layout.fragment_blogger, container, false);
    	return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("fragments", "onResume: BloggerFragment");
    	new GetBlogEntriesTask().execute(Constants.BLOGGER_URL);
    }
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	try {
    		blogEntrySelectedListener = (OnBlogEntrySelectedListener) activity;
    	} catch (ClassCastException e) {
    		throw new ClassCastException(activity.toString() + " must implement OnBlogEntrySelectedListener");
    	}
    }
    
    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
    	BlogEntry entry = (BlogEntry) listView.getItemAtPosition(position);
    	blogEntrySelectedListener.onBlogEntrySelected(entry);
    }
    
    private class GetBlogEntriesTask extends AsyncTask<String, Void, Blog> {

		@Override
		protected Blog doInBackground(String... urls) {
			try {
				return getBlog(urls[0]);
			} catch (IOException e) {
				trackException("Blogger", e.getMessage());
				return null;
			} catch (XmlPullParserException e) {
				trackException("Blogger", e.getMessage());
				return null; 
			} catch (ParseException e) {
				trackException("Blogger", e.getMessage());
				return null;
			}
		}
    	
		@Override
		protected void onPostExecute(Blog blog) {
			hideProgressBar();
			loadBlog(blog);
		}
		
	    protected void hideProgressBar() {
	    	ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
	    	progressBar.setVisibility(View.GONE);
	    }
	    
		private Blog getBlog(String url) throws IOException, XmlPullParserException, ParseException {
	    	InputStream inputStream = null;
	    	BloggerXmlParser parser = new BloggerXmlParser();
	    	Blog blog = null;
	      
	        try {
	        	inputStream = UrlConverter.downloadUrl(url);
	        	blog = parser.parse(inputStream);
	        } finally {
	        	if (inputStream != null) {
	        		inputStream.close();
	        	}
	        }
	        return blog;
		}
    }
    
    protected void loadBlog(Blog blog) {
    	TextView title = (TextView) view.findViewById(R.id.blog_title);
    	title.setText(blog.getTitle());
    	
    	loadListAdapter(blog.getBlogEntries());
    }
    
    protected void loadListAdapter(List<BlogEntry> entries) {
    	final ListView listView = getListView();
    	BlogEntryListAdapter listAdapter = new BlogEntryListAdapter(view.getContext(), R.layout.blogger_row, entries); 	
    	listView.setAdapter(listAdapter);
    }
    
	@Override
	public Path getPath() {
		return Path.BLOGGER;
	}
	
	public interface OnBlogEntrySelectedListener {
		public void onBlogEntrySelected(BlogEntry entry);
	}

}
