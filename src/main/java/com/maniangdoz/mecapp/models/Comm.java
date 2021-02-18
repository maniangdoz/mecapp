package com.maniangdoz.mecapp.models;

import org.springframework.stereotype.Component;

import com.google.cloud.Timestamp;

@Component
public class Comm implements Comparable<Comm>{
	private long id;
	private String text;
	private Timestamp time;
	
	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int compareTo(Comm o) {
		long compareId = ((Comm) o).getId();
		int result = (int) (this.id - compareId);
		return result;
	}

	
}
