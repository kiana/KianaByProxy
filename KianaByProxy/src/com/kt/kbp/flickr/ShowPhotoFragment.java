package com.kt.kbp.flickr;

import java.net.MalformedURLException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.yuyang226.flickr.photos.Photo;
import com.kt.kbp.R;
import com.kt.kbp.common.StreamDrawableTask;
import com.kt.kbp.googleanalytics.GoogleAnalyticsFragment;

public class ShowPhotoFragment extends GoogleAnalyticsFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	View view = inflater.inflate(R.layout.fragment_show_photo, container, false);
    	
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

    public String getPhotoId() {
    	return getArguments().getString("id");
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
