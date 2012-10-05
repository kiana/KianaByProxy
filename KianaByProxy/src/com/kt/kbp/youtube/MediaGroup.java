package com.kt.kbp.youtube;


public class MediaGroup {
	
	private String thumbNailUrl; 
	private String title;
	private String description;
	private String duration;
	
	public MediaGroup(String thumbNail, String title, String description, String duration) {
		this.thumbNailUrl = thumbNail;
		this.title = title;
		this.description = description;
		this.duration = duration;
	}
	
	public String getThumbNailUrl() {
		return thumbNailUrl;
	}
	
	public void setThumbNailUrl(String thumbNail) {
		this.thumbNailUrl = thumbNail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
