package com.kt.kbp.flickr;

import java.net.MalformedURLException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.kt.kbp.R;
import com.kt.kbp.common.StreamDrawableTask;

public class PhotoGridAdapter extends ArrayAdapter<Photo> {

	private PhotoList photoList;
	private LayoutInflater inflater;
	
	static class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}
	
	public PhotoGridAdapter(Context context, int textViewResourceId, PhotoList photoList) {
		super(context, textViewResourceId, photoList);
		this.photoList = photoList;

		inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View grid, ViewGroup parent) {
		
		View gridView = grid; 
		
		if (gridView == null) {
			gridView = inflater.inflate(R.layout.flickr_griditem, null);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
			viewHolder.progressBar = (ProgressBar) gridView.findViewById(R.id.progressBar);
			
			gridView.setTag(viewHolder);
		}
		
		ViewHolder view = (ViewHolder) gridView.getTag();
		Photo photo = photoList.get(position);
		
		try {
			new StreamDrawableTask(photo.getLargeSquareUrl(), view.progressBar).execute(view.imageView);
		} catch (MalformedURLException e) {
			e.printStackTrace(); // TODO WEBANALYTICS
		}
		
		return gridView;
	}
}
