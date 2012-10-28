package com.kt.kbp.youtube;

import com.kt.kbp.activitypath.ActivityPath;
import com.kt.kbp.activitypath.ActivityPathInterface;
import com.kt.kbp.googleanalytics.GoogleAnalyticsYouTubeActivity;

public class ExtendedYouTubePlayerActivity extends GoogleAnalyticsYouTubeActivity implements ActivityPathInterface  {

	@Override
	public ActivityPath getActivityPath() {
		return ActivityPath.SHOWVIDEO;
	}

}
