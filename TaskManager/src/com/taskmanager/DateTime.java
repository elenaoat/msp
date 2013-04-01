package com.taskmanager;

public class DateTime {
	private int hour;
	private int minute;
	
	public DateTime(int hour, int minute){
		this.setHour(hour);
		this.setMinute(minute);
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

}
