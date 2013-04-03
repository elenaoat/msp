package com.taskmanager;

public class Task {

	public String time;
	public String task;
	
	public Task(){
		super();		
	}
	
	public Task(String time, String task){
		super();
		this.time = time;
		this.task = task;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
}
