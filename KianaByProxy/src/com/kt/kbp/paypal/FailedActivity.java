package com.kt.kbp.paypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.kt.kbp.MainActivity;
import com.kt.kbp.R;
import com.kt.kbp.activitypath.ActivityPath;
import com.kt.kbp.activitypath.ActivityPathInterface;
import com.kt.kbp.googleanalytics.GoogleAnalyticsActivity;

public class FailedActivity extends GoogleAnalyticsActivity implements ActivityPathInterface {

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
			public void onClick(View v) {
				//category, action, label, value
				trackEvent("Paypal", "Donate", "From Failed", 0);
				startActivity(new Intent(v.getContext(), DonateActivity.class));
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_failed, menu);
        return true;
    }

	@Override
	public ActivityPath getActivityPath() {
		return ActivityPath.FAILED;
	}
 
}
