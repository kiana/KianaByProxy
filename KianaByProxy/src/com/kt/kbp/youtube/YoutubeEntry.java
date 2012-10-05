package com.kt.kbp.youtube;

public class YoutubeEntry {

	private MediaGroup mediaGroup;
	private String viewCount;
	private String id;
	
	public YoutubeEntry(String id, MediaGroup mediaGroup, String viewCount) {
		this.setMediaGroup(mediaGroup);
		this.viewCount = viewCount;
		this.setId(id);
	}
	
	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public MediaGroup getMediaGroup() {
		return mediaGroup;
	}

	public void setMediaGroup(MediaGroup mediaGroup) {
		this.mediaGroup = mediaGroup;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
		
}
