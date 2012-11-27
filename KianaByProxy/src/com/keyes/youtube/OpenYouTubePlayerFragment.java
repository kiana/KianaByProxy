package com.keyes.youtube;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.googleanalytics.GoogleAnalyticsFragment;

public class OpenYouTubePlayerFragment extends GoogleAnalyticsFragment {
	public static final String SCHEME_YOUTUBE_VIDEO = "ytv";
	public static final String SCHEME_YOUTUBE_PLAYLIST = "ytpl";
	
	static final String YOUTUBE_VIDEO_INFORMATION_URL = "http://www.youtube.com/get_video_info?&video_id=";
	static final String YOUTUBE_PLAYLIST_ATOM_FEED_URL = "http://gdata.youtube.com/feeds/api/playlists/";
	
	protected ProgressBar mProgressBar;
	protected TextView    mProgressMessage;
	protected VideoView   mVideoView;
	
	public final static String MSG_INIT = "com.keyes.video.msg.init";
	protected String      mMsgInit       = "Initializing";
	
	public final static String MSG_DETECT = "com.keyes.video.msg.detect";
	protected String      mMsgDetect     = "Detecting Bandwidth";

	public final static String MSG_PLAYLIST = "com.keyes.video.msg.playlist";
	protected String      mMsgPlaylist   = "Determining Latest Video in YouTube Playlist";

	public final static String MSG_TOKEN = "com.keyes.video.msg.token";
	protected String      mMsgToken      = "Retrieving YouTube Video Token";
	
	public final static String MSG_LO_BAND = "com.keyes.video.msg.loband";
	protected String      mMsgLowBand    = "Buffering Low-bandwidth Video";
	
	public final static String MSG_HI_BAND = "com.keyes.video.msg.hiband";
	protected String      mMsgHiBand     = "Buffering High-bandwidth Video";
	
	public final static String MSG_ERROR_TITLE = "com.keyes.video.msg.error.title";
	protected String      mMsgErrorTitle = "Communications Error";
	
	public final static String MSG_ERROR_MSG = "com.keyes.video.msg.error.msg";
	protected String      mMsgError      = "An error occurred during the retrieval of the video.  This could be due to network issues or YouTube protocols.  Please try again later.";
	
	/** Background task on which all of the interaction with YouTube is done */
	protected QueryYouTubeTask mQueryYouTubeTask;
	
	protected String mVideoId = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);

//		getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE); //TODO can i requestWindowFeature some other place?
		getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // create the layout of the view
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
        });
        
        mVideoView = (VideoView) view.findViewById(R.id.three);
        mProgressBar = (ProgressBar) view.findViewById(R.id.four);
        mProgressMessage = (TextView) view.findViewById(R.id.five);
        
        // determine the messages to be displayed as the view loads the video
        extractMessages();
	    
	    // set the flag to keep the screen ON so that the video can play without the screen being turned off
        getActivity().getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mProgressBar.bringToFront();
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressMessage.setText(mMsgInit);
		
		return view;
    }

    @Override
    public void onResume() {
    	super.onResume();
		// extract the playlist or video id from the intent that started this video
		
		Uri lVideoIdUri = getActivity().getIntent().getData();
		
		if(lVideoIdUri == null){
			Log.i(this.getClass().getSimpleName(), "No video ID was specified in the intent.  Closing video activity.");
			getFragmentManager().popBackStack(Constants.YOUTUBE_FRAG, 0);
		}
		String lVideoSchemeStr = lVideoIdUri.getScheme();
		String lVideoIdStr     = lVideoIdUri.getEncodedSchemeSpecificPart();
		if(lVideoIdStr == null){
			Log.i(this.getClass().getSimpleName(), "No video ID was specified in the intent.  Closing video activity.");
			getFragmentManager().popBackStack(Constants.YOUTUBE_FRAG, 0);
		}
		if(lVideoIdStr.startsWith("//")){
			if(lVideoIdStr.length() > 2){
				lVideoIdStr = lVideoIdStr.substring(2);
			} else {
				Log.i(this.getClass().getSimpleName(), "No video ID was specified in the intent.  Closing video activity.");
				getFragmentManager().popBackStack(Constants.YOUTUBE_FRAG, 0);
			}
		}

		///////////////////
		// extract either a video id or a playlist id, depending on the uri scheme
		YouTubeId lYouTubeId = null;
		if(lVideoSchemeStr != null && lVideoSchemeStr.equalsIgnoreCase(SCHEME_YOUTUBE_PLAYLIST)){
			lYouTubeId = new PlaylistId(lVideoIdStr);
		}
		
		else if(lVideoSchemeStr != null && lVideoSchemeStr.equalsIgnoreCase(SCHEME_YOUTUBE_VIDEO)){
			lYouTubeId = new VideoId(lVideoIdStr);
		}

		if(lYouTubeId == null){
			Log.i(this.getClass().getSimpleName(), "Unable to extract video ID from the intent.  Closing video activity.");
			getFragmentManager().popBackStack(Constants.YOUTUBE_FRAG, 0);
		}
		
		mQueryYouTubeTask = (QueryYouTubeTask) new QueryYouTubeTask().execute(lYouTubeId);
    }

	/**
	 * Determine the messages to display during video load and initialization. 
	 */
	private void extractMessages() {
		Intent lInvokingIntent = getActivity().getIntent();
        String lMsgInit = lInvokingIntent.getStringExtra(MSG_INIT);
        if(lMsgInit != null){
        	mMsgInit = lMsgInit;
        }
        String lMsgDetect = lInvokingIntent.getStringExtra(MSG_DETECT);
        if(lMsgDetect != null){
        	mMsgDetect = lMsgDetect;
        }
        String lMsgPlaylist = lInvokingIntent.getStringExtra(MSG_PLAYLIST);
        if(lMsgPlaylist != null){
        	mMsgPlaylist = lMsgPlaylist;
        }
        String lMsgToken = lInvokingIntent.getStringExtra(MSG_TOKEN);
        if(lMsgToken != null){
        	mMsgToken = lMsgToken;
        }
        String lMsgLoBand = lInvokingIntent.getStringExtra(MSG_LO_BAND);
        if(lMsgLoBand != null){
        	mMsgLowBand = lMsgLoBand;
        }
        String lMsgHiBand = lInvokingIntent.getStringExtra(MSG_HI_BAND);
        if(lMsgHiBand != null){
        	mMsgHiBand = lMsgHiBand;
        }
        String lMsgErrTitle = lInvokingIntent.getStringExtra(MSG_ERROR_TITLE);
        if(lMsgErrTitle != null){
        	mMsgErrorTitle = lMsgErrTitle;
        }
        String lMsgErrMsg = lInvokingIntent.getStringExtra(MSG_ERROR_MSG);
        if(lMsgErrMsg != null){
        	mMsgError = lMsgErrMsg;
        }
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		YouTubeUtility.markVideoAsViewed(getActivity(), mVideoId);
		
		if(mQueryYouTubeTask != null){
			mQueryYouTubeTask.cancel(true);
		}
		
		if(mVideoView != null){
			mVideoView.stopPlayback();
		}
		
	    // clear the flag that keeps the screen ON 
		getActivity().getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		this.mQueryYouTubeTask = null;
		this.mVideoView = null;
		
	}
	
	public void updateProgress(String pProgressMsg){
		try {
			mProgressMessage.setText(pProgressMsg);
		} catch(Exception e) {
			Log.e(this.getClass().getSimpleName(), "Error updating video status!", e);
		}
	}
	
	private class ProgressUpdateInfo {
	
		public String mMsg;
	
		public ProgressUpdateInfo(String pMsg){
			mMsg = pMsg;
		}
	}
	
	/**
	 * Task to figure out details by calling out to YouTube GData API.  We only use public methods that
	 * don't require authentication.
	 * 
	 */
	private class QueryYouTubeTask extends AsyncTask<YouTubeId, ProgressUpdateInfo, Uri> {

		private boolean mShowedError = false;

		@Override
		protected Uri doInBackground(YouTubeId... pParams) {
			String lUriStr = null;
			String lYouTubeFmtQuality = "17";   // 3gpp medium quality, which should be fast enough to view over EDGE connection
			String lYouTubeVideoId = null;
			
			if(isCancelled())
				return null;
			
			try {
			
				publishProgress(new ProgressUpdateInfo(mMsgDetect));
				
				WifiManager lWifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
				TelephonyManager lTelephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
				
				////////////////////////////
				// if we have a fast connection (wifi or 3g), then we'll get a high quality YouTube video
				if( (lWifiManager.isWifiEnabled() && lWifiManager.getConnectionInfo() != null && lWifiManager.getConnectionInfo().getIpAddress() != 0) ||
					( (lTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS ||
							
					   /* icky... using literals to make backwards compatible with 1.5 and 1.6 */		
					   lTelephonyManager.getNetworkType() == 9 /*HSUPA*/  ||
					   lTelephonyManager.getNetworkType() == 10 /*HSPA*/  ||
					   lTelephonyManager.getNetworkType() == 8 /*HSDPA*/  ||
					   lTelephonyManager.getNetworkType() == 5 /*EVDO_0*/  ||
					   lTelephonyManager.getNetworkType() == 6 /*EVDO A*/) 
					  
					 && lTelephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) 
				   ){
					lYouTubeFmtQuality = "18";
				}
				

				///////////////////////////////////
				// if the intent is to show a playlist, get the latest video id from the playlist, otherwise the video
				// id was explicitly declared.
				if(pParams[0] instanceof PlaylistId){
					publishProgress(new ProgressUpdateInfo(mMsgPlaylist));
					lYouTubeVideoId = YouTubeUtility.queryLatestPlaylistVideo((PlaylistId) pParams[0]);
				}
				
				else if(pParams[0] instanceof VideoId){
					lYouTubeVideoId = pParams[0].getId();
				}
				
				mVideoId = lYouTubeVideoId;
				
				publishProgress(new ProgressUpdateInfo(mMsgToken));
				
				if(isCancelled())
					return null;

				////////////////////////////////////
				// calculate the actual URL of the video, encoded with proper YouTube token
				lUriStr = YouTubeUtility.calculateYouTubeUrl(lYouTubeFmtQuality, true, lYouTubeVideoId);
				
				if(isCancelled())
					return null;
	
				if(lYouTubeFmtQuality.equals("17")){
					publishProgress(new ProgressUpdateInfo(mMsgLowBand));
				} else {
					publishProgress(new ProgressUpdateInfo(mMsgHiBand));
				}
	
			} catch(Exception e) {
				Log.e(this.getClass().getSimpleName(), "Error occurred while retrieving information from YouTube.", e);
			}

			if(lUriStr != null){
				return Uri.parse(lUriStr);
			} else {
				return null;
			}
		}



		@Override
		protected void onPostExecute(Uri pResult) {
			super.onPostExecute(pResult);
			
			try {
				if(isCancelled())
					return;
				
				if(pResult == null){
					throw new RuntimeException("Invalid NULL Url.");
				}
				
			    mVideoView.setVideoURI(pResult);
			    
				if(isCancelled())
					return;
			    
			    // TODO:  add listeners for finish of video
			    mVideoView.setOnCompletionListener(new OnCompletionListener(){
	
					@Override
					public void onCompletion(MediaPlayer pMp) {
						if(isCancelled())
							return;
						getFragmentManager().popBackStack(Constants.YOUTUBE_FRAG, 0);
					}
			    	
			    });
			    
				if(isCancelled())
					return;
	
				final MediaController lMediaController = new MediaController(getActivity());
				mVideoView.setMediaController(lMediaController);
				lMediaController.show(0);
				//mVideoView.setKeepScreenOn(true);
				mVideoView.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {
	
					@Override
					public void onPrepared(MediaPlayer pMp) {
						if(isCancelled())
							return;
						OpenYouTubePlayerFragment.this.mProgressBar.setVisibility(View.GONE);
						OpenYouTubePlayerFragment.this.mProgressMessage.setVisibility(View.GONE);
					}
			    	
			    });
			    
				if(isCancelled())
					return;
	
				mVideoView.requestFocus();
				mVideoView.start();
			} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Error playing video!", e);
				
				if(!mShowedError){
					showErrorAlert();
				}
			}
		}

		private void showErrorAlert() {
			
			try {
				Builder lBuilder = new AlertDialog.Builder(getActivity());
				lBuilder.setTitle(mMsgErrorTitle);
				lBuilder.setCancelable(false);
				lBuilder.setMessage(mMsgError);
	
				lBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
	
					@Override
					public void onClick(DialogInterface pDialog, int pWhich) {
						getFragmentManager().popBackStack(Constants.YOUTUBE_FRAG, 0);
					}
					
				});
	
				AlertDialog lDialog = lBuilder.create();
				lDialog.show();
			} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Problem showing error dialog.", e);
			}
		}

		@Override
		protected void onProgressUpdate(ProgressUpdateInfo... pValues) {
			super.onProgressUpdate(pValues);
			
			OpenYouTubePlayerFragment.this.updateProgress(pValues[0].mMsg);
		}
		
		
		
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}
