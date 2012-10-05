package com.kt.kbp.blogger;

import java.util.List;

public class Blog {

	private String id;
	private String title;
	private List<BlogEntry> blogEntries;
	
	public Blog(String id, String title, List<BlogEntry> entries) {
		this.setId(id);
		this.setTitle(title);
		this.setBlogEntries(entries);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<BlogEntry> getBlogEntries() {
		return blogEntries;
	}

	public void setBlogEntries(List<BlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
	}
	
}
