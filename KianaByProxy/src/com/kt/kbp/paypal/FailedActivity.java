package com.kt.kbp.paypal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.kt.kbp.MainActivity;
import com.kt.kbp.R;

public class FailedActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed);
        TextView backLink = (TextView)findViewById(R.id.back_to_main);
        backLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FailedActivity.this, MainActivity.class));
			}
		});
        
        TextView donateLink = (TextView)findViewById(R.id.donate);
        donateLink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(FailedActivity.this, DonateActivity.class));
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_failed, menu);
        return true;
    }

    @Override
    public void onStart() {
    	super.onStart();
    	EasyTracker.getInstance().activityStart(this);
    }
    
    
    @Override
    public void onStop() {
    	super.onStop();
    	EasyTracker.getInstance().activityStop(this);
    }  
}