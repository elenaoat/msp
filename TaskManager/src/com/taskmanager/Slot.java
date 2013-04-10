package com.taskmanager;

public class Slot {
	public int id;
	public int time;
	public String name;
	

	public Slot(int id, int time, String name){
		this.time = time;
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
