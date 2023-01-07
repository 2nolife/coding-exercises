/*
 * Copyright © 2006-2010. BSkyB Ltd All Rights reserved
 */

package com.sky.dvdstore;

public class Dvd {
	
	private String reference;
	private String title;
	private String review;
	
	public Dvd(String reference, String title, String description) {
		this.reference = reference;
		this.title = title;
		this.review = description;
	}

    public String getReview() {
		return review;
	}

	public String getReference() {
		return reference;
	}

	public String getTitle() {
		return title;
	}
    
}
