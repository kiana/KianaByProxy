package com.kt.kbp.blogger;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kt.kbp.MainActivity;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.UrlConverter;

public class BloggerActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogger);
        
		TextView back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(BloggerActivity.this, MainActivity.class));
			}
		});
		
		new GetBlogEntriesTask().execute(Constants.BLOGGER_URL);
    }

    private class GetBlogEntriesTask extends AsyncTask<String, Void, Blog> {

		@Override
		protected Blog doInBackground(String... urls) {
			try {
				return getBlog(urls[0]);
			} catch (IOException e) {
				e.printStackTrace();
				return null; //TODO WEBANALYTICS - track error
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				return null; //TODO WEBANALYTICS - track error
			} catch (ParseException e) {
				e.printStackTrace();
				return null; //TODO WEBANALYTICS - track error
			}
		}
    	
		@Override
		protected void onPostExecute(Blog blog) {
			hideProgressBar();
			loadBlog(blog);
		}
		
	    protected void hideProgressBar() {
	    	ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
    	TextView title = (TextView) findViewById(R.id.blog_title);
    	title.setText(blog.getTitle());
    	
    	loadListAdapter(blog.getBlogEntries());
    }
    
    protected void loadListAdapter(List<BlogEntry> entries) {
    	final ListView listView = getListView();
    	BlogEntryListAdapter listAdapter = new BlogEntryListAdapter(this, R.layout.blogger_row, entries);
    	
    	listView.setAdapter(listAdapter);
    	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BlogEntry entry = (BlogEntry) listView.getItemAtPosition(position);
				
				Intent i = new Intent(BloggerActivity.this, ShowBlogEntryActivity.class);
				i.putExtra("title", entry.getTitle());
				i.putExtra("date", formatDate(entry.getDatePublished()));
				i.putExtra("content", entry.getContent());
				
				startActivity(i);
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_blogger, menu);
        return true;
    }

    //TODO pick your battles. Also in BlogEntryListAdapter
	private String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(date);
	}
}
