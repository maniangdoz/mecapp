package com.maniangdoz.mecapp.models;

import org.springframework.stereotype.Component;

@Component
public class Surfer {
	private long id;
	private String name;
	private Comm comment;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Comm getComment() {
		return comment;
	}
	
	public void setComment(Comm comment) {
		this.comment = comment;
	}
	
}
