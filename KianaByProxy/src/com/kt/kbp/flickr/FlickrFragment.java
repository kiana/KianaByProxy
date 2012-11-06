package com.kt.kbp.flickr;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.flickr.ShowPhotoFragment.OnPhotoSelectedListener;
import com.kt.kbp.googleanalytics.GoogleAnalyticsFragment;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;

public class FlickrFragment extends GoogleAnalyticsFragment implements PathInterface {

	private View view;
	private GridView gridView;
	private OnPhotoSelectedListener photoSelectedListener;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	view = inflater.inflate(R.layout.fragment_flickr, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
    	return view;
    }
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	try {
    		photoSelectedListener = (OnPhotoSelectedListener) activity;
    	} catch (ClassCastException e) {
    		throw new ClassCastException(activity.toString() + " must implement OnPhotoSelectedListener");
    	}
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    	new StreamPhotoListTask().execute();
    }

    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("fragments", "onResume: FlickrFragment");
    }
    
	@Override
	public Path getPath() {
		return Path.FLICKR;
	}

    private class StreamPhotoListTask extends AsyncTask<Void, Void, PhotoList> {
    	
		@Override
		protected PhotoList doInBackground(Void... params) {
			
			PhotoList photoList = new PhotoList();
			
			Flickr flickr = new Flickr(Constants.FLICKR_KEY);
			try {
				photoList = flickr.getPeopleInterface().getPublicPhotos(Constants.FLICKR_USERID, 100, 1);
			} catch (IOException e) {
				trackException("Flickr", e.getMessage());
			} catch (FlickrException e) {
				trackException("Flickr", e.getMessage());
			} catch (JSONException e) {
				trackException("Flickr", e.getMessage());
			}
			return photoList;
		}
    	
		@Override
		protected void onPostExecute(final PhotoList photoList) {
			setUpGridView(photoList);
		}
    }
    
    public void setUpGridView(PhotoList photoList) {
        Activity activity = getActivity();
        if (activity != null) {
			PhotoGridAdapter adapter = new PhotoGridAdapter(activity, R.layout.flickr_griditem, photoList);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onGridItemClick(gridView, view, position, id);
				}
			});
        }
    }
    
    public void onGridItemClick(GridView gridView, View v, int position, long id) {
    	Photo photo = (Photo) gridView.getItemAtPosition(position);
    	photoSelectedListener.onPhotoSelected(photo);
    }
}
