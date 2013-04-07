package com.taskmanager;

public class Task {

	public String name;
//	public String description;
	//public String recurrenceFlag;
	public String eventTime;
	//public String eventStartDayTime;
	//public String recurrenceEndDay;
	
/*	+ "name TEXT, "
	+ "description TEXT, "
	+ "createDayTime TEXT, "
	+ "eventStartDayTime TEXT, "
	+ "eventEndDayTime TEXT, "
	+ "notificationB4 TEXT, "
	+ "notificationFreq TEXT, "
	+ "notificationType TEXT, "
	+ "deleteFlag TEXT);";*/
	public Task(){
		super();		
	}
	
	public Task(String name, String description, String eventTime){
		super();
		this.name = name;
	//	this.description = description;
		this.eventTime = eventTime;
		//this.eventEndDayTime = eventEndDayTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

/*	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
*/
	public String getEventTime() {
		return eventTime;
	}

	/*public void setEventEndDayTime(String eventEndDayTime) {
		this.eventEndDayTime = eventEndDayTime;
	}

	public String getEventStartDayTime() {
		return eventStartDayTime;
	}

	public void setEventStartDayTime(String eventStartDayTime) {
		this.eventStartDayTime = eventStartDayTime;
	}*/
	

}