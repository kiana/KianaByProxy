package com.kt.kbp.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class StreamDrawableTask extends AsyncTask<ImageView, Void, Drawable> {
	
	protected URL url;
	protected ImageView imageView = null;
	protected ProgressBar progressBar = null;
	
	public StreamDrawableTask(String urlAsString) throws MalformedURLException {
		this(new URL(urlAsString));
	}
	
	public StreamDrawableTask(URL url) {
		this.url = url;
	}
	
	public StreamDrawableTask(String urlAsString, ProgressBar progressBar) throws MalformedURLException {
		this(urlAsString);
		this.progressBar = progressBar;
	}
	
	public StreamDrawableTask(URL url, ProgressBar progressBar) {
		this(url);
		this.progressBar = progressBar;
	}
	
	@Override
	protected Drawable doInBackground(ImageView... imageViews) {
		Drawable drawable = null;
		try {
			this.imageView = imageViews[0];
			drawable = Drawable.createFromStream(url.openConnection().getInputStream(), "src");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drawable;
	}
	
	@Override
	protected void onPostExecute(Drawable drawable) {
		setProgressBarGone();
		imageView.setImageDrawable(drawable);
	}
	
	private void setProgressBarGone() {
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
	}
}
