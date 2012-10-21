package com.kt.kbp.paypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.kt.kbp.MainActivity;
import com.kt.kbp.R;
import com.kt.kbp.googleanalytics.GoogleAnalyticsActivity;

public class SucceededActivity extends GoogleAnalyticsActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succeeded);
        
        tracker.trackPageView("/succeededActivity");
        
        TextView backLink = (TextView)findViewById(R.id.back_from_success);
        backLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SucceededActivity.this, MainActivity.class));
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_succeeded, menu);
        return true;
    }
    
}
