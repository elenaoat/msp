package com.taskmanager;

public class CustomDate {

	public String year_str;
	public String month_str;
	public String day_str;
	public int year;
	public int month;
	public int day;
	
	public CustomDate(){
		
	}
	
	public CustomDate(String date){
	
		this.year_str = date.substring(0, 4);
		this.month_str = date.substring(5, 7);
		this.day_str = date.substring(8, 10);
		this.year = Integer.parseInt(year_str);
		this.month = Integer.parseInt(removeZero(month_str));
		this.day = Integer.parseInt(removeZero(day_str));
	}
	public String removeZero(String str){
		String str_edited;
		if (str.charAt(0) == '0'){
			str_edited = new String(str.substring(1, 2));
		}
		else {
			str_edited = new String(str);
		}
		return str_edited;
	}
	
	public String getYear_str() {
		return year_str;
	}
	public void setYear_str(String year_str) {
		this.year_str = year_str;
	}
	public String getMonth_str() {
		return month_str;
	}
	public void setMonth_str(String month_str) {
		this.month_str = month_str;
	}
	public String getDay_str() {
		return day_str;
	}
	public void setDay_str(String day_str) {
		this.day_str = day_str;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}

}
