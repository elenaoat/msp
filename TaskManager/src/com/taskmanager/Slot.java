package com.taskmanager;

public class Slot {
	public int id;
	public String time;
	public String name;
	

	public Slot(int id, String time, String name){
		this.time = time;
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
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
