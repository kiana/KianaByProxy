package com.kt.kbp.blogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.kt.kbp.R;

public class ShowBlogEntryActivity extends Activity {

	private TextView title;
	private TextView date;
	private TextView content;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_blog_entry);
        
		TextView back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ShowBlogEntryActivity.this, BloggerActivity.class));
			}
		});
		
        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date_published);
        content = (TextView) findViewById(R.id.content);
        
        title.setText(getIntent().getStringExtra("title"));
        date.setText(getIntent().getStringExtra("date"));
        
        
        content.setText(Html.fromHtml(getIntent().getStringExtra("content")));
        content.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_blog_entry, menu);
        return true;
    }

    
}
