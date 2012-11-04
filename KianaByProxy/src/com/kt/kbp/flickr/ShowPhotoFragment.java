package com.kt.kbp.flickr;

import java.net.MalformedURLException;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.yuyang226.flickr.photos.Photo;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.kt.kbp.R;
import com.kt.kbp.common.ExceptionTrackerInterface;
import com.kt.kbp.common.StreamDrawableTask;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;

public class ShowPhotoFragment extends Fragment  implements ExceptionTrackerInterface, PathInterface {

	private View view;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	view = inflater.inflate(R.layout.fragment_show_photo, container, false);
    	
    	Bundle bundle = getArguments();
        TextView title = (TextView) view.findViewById(R.id.flickr_label);
        ImageView flickrImage = (ImageView) view.findViewById(R.id.flickrimage);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        title.setText(bundle.getString("title"));
        try {
			new StreamDrawableTask(bundle.getString("url"), progressBar).execute(flickrImage);
		} catch (MalformedURLException e) {
			trackException("ShowPhoto", e.getMessage());
		}	
        
    	return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("fragments", "onResume: ShowPhotoFragment");
    }

    public String getPhotoId() {
    	return getArguments().getString("id");
    }
    
	@Override
	public Path getPath() {
		return Path.SHOWPHOTO;
	}
	
	public void trackException(String category, String message) {
		//category, action, label, value
		GoogleAnalyticsTracker.getInstance().trackEvent(category, "Exception", message, 0);
	}
	
	public interface OnPhotoSelectedListener {
		void onPhotoSelected(Photo photo);
	}

	public static ShowPhotoFragment newInstance(Photo photo) {
		ShowPhotoFragment showPhotoFragment = new ShowPhotoFragment();
		Bundle bundle = new Bundle();
		bundle.putString("id", photo.getId());
		bundle.putString("title", photo.getTitle());
		bundle.putString("url", photo.getLargeUrl());
		showPhotoFragment.setArguments(bundle);
		return showPhotoFragment;
	}
}
