package com.taskmanager;

public class Slot {
	public String time;
	public String name;

	public Slot(String time, String name){
		this.time = time;
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
