package com.maniangdoz.mecapp.models;

import org.springframework.stereotype.Component;

@Component
public class Surfer implements Comparable<Surfer> {
	private long id;
	private String name;
	
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

	@Override
	public int compareTo(Surfer o) {
		// TODO Auto-generated method stub
		long comparedId = o.getId();
		return (int) (this.id - comparedId);
	}
	
}
