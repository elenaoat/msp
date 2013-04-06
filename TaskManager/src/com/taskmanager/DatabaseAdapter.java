package com.taskmanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
//import java.util.Date;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteException;
import android.net.ParseException;

public class DatabaseAdapter {
	private SQLiteDatabase database;
	private DatabaseOpenHelper dbHelper;
	private Context context;
	
	public DatabaseAdapter(Context c){
		this.context=c;
		dbHelper = new DatabaseOpenHelper(context);
	}
	
	public void Open(){
		database = dbHelper.getWritableDatabase();
	}
	
	public void Close()
	{
		database.close();
	}
	
	public Cursor getAllConfig() {
	 
		return database.query(
				"global_config", 
				new String[] {"property", "valueType","textValue", "intergerValue", "dateValue"},
				null, null, null, null, null);
	}
	// initialize global configuration table
	public void initializeGConfig(){
		this.Open();
		ContentValues values= new ContentValues();
		
		values.put("property", "NotificationB4");
		values.put("valueType", "int");
		values.put("intergerValue", "Fifteen");
		
		database.insert("global_config", "", values);
		
		values.put("property", "NotificationFreq");
		values.put("valueType", "int");
		values.put("intergerValue", "Five");
		
		database.insert("global_config", "", values);
		
		values.put("property", "NotificationType");
		values.put("valueType", "text");
		values.put("textValue", "Both");
		
		database.insert("global_config", "", values);
		this.Close();
    }
	
	public Cursor viewEventByID(long id){
		
		return database.query("master_event", null, "id=" + id,
				null, null, null, null);
		
	}
	
	
	
	
	
	/*
	 * This method returns all the events for a particular date.
	 * Input parameter format: 'YYYY-MM-DD' 
	 * Input parameter Example: '2013-05-09'
	 * Output: Cursor having the following columns:
	 * 		name,description,eventStartDayTime,eventEndDayTime
	 */
	public Cursor getEventByDate(String searchDate){
		
		Cursor cursor;
		cursor = database.rawQuery("SELECT id,name,description,eventStartDayTime,eventEndDayTime FROM master_event " +
				"WHERE substr(eventStartDayTime,1,10) = '"+searchDate+"'", null);        
		return cursor;
	}
	
	public boolean executeSql(String sql){
		
		database.beginTransaction();
        SQLiteStatement sqlStmt = database.compileStatement(sql);
        try{
        	sqlStmt.execute();
            database.setTransactionSuccessful();
            
        }catch(SQLiteException ex){
        	
        	ex.printStackTrace();
        	return false;
        	
        }finally{
        	database.endTransaction();
        }
        
        return true;
	}
	
	
	/*
	 * Insert a new event in the database
	 * Input parameter format: all are String
	 * 	 Output: 0 = failure; 1 = success
	 */
	public long createEvent
	        (String name, 
	         String description, 
	         String eventStartDayTime, 
	         String eventEndDayTime,
	         String notificationFreq,
	         String notificationB4, 
	         String notificationType, 
	         String recurrenceFlag, 
	         String recurrenceEndDay)
	{
		//TODO createEvent
		
		 String sql,startTimeString,endTimeString;
	   	 long parentId;
	   	 Date startDate = null,endDate=null,endDayTime=null;
	   	 int incr=0;
	   	 
	   	 
	   	 if(recurrenceFlag == ""){
	   		 
	   		 sql = "insert into master_event (name,description,eventStartDayTime," +
	   				 "eventEndDayTime,notificationFreq,notificationB4,notificationType,recurrenceFlag," +
	   				 "recurrenceEndDay)" +
	   				 "values('" + name + "','" + description + "','" + eventStartDayTime + "','"
	   						  + eventEndDayTime + "','" + notificationFreq + "','" + notificationB4 +
	   						  "','" + notificationType + "','" + recurrenceFlag + "','" +
	   						  recurrenceEndDay + "')";
	   		 
	   		 if(!executeSql(sql))
	   			 return 0;   	 
	   	 }
	   	 else if(recurrenceFlag.equals("daily")||recurrenceFlag.equals("weekly")){
	   		if(recurrenceFlag.equals("daily")) incr=1; else incr=7;
	   		 
	   		 ContentValues values= new ContentValues();
	   		 
	   		 values.put("name", name);
	   		 values.put("description", description);
	   		 values.put("eventStartDayTime", eventStartDayTime);
	   		 values.put("eventEndDayTime", eventEndDayTime);
	   		 values.put("notificationFreq", notificationFreq);
	   		 values.put("notificationB4", notificationB4);
	   		 values.put("notificationType", notificationType);
	   		 values.put("recurrenceFlag", recurrenceFlag);
	   		 values.put("recurrenceEndDay", recurrenceEndDay);
	   		 
	   		 parentId = database.insert("master_event", "", values);
	   		 
	   		 if(parentId == -1)
	   			 return 0;
	   		 
	   		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	   		 
	   		 try {  
	   			startDate = format.parse(eventStartDayTime);
	   			endDate = format.parse(recurrenceEndDay);
	   			endDayTime = format.parse(eventEndDayTime);
	   			//System.out.println(date);  
	   		 } catch (ParseException e) {  
	   			// TODO Auto-generated catch block  
	   			e.printStackTrace();  
	   		 } catch (java.text.ParseException e) {
	   			 // TODO Auto-generated catch block
	   			 e.printStackTrace();
	   		 }
	   		 
	   		Calendar c = Calendar.getInstance();
	   		c.setTime(startDate);
	   		    
	   		Calendar c1 = Calendar.getInstance();
	   		c1.setTime(endDayTime);
	   			
	   		c.add(Calendar.DATE, incr);
	   		startDate = c.getTime();
	   			 
	   		c1.add(Calendar.DATE, incr);
	   		endDayTime = c1.getTime();
	   			 
	   		    
	   		 while(startDate.before(endDate)){
	   			 
	   			 
	   			 startTimeString = format.format(startDate);
	   			 endTimeString = format.format(endDayTime);
	   			 
	   			 sql = "insert into master_event (name,description,parentId,eventStartDayTime," +
	   					 "eventEndDayTime,notificationFreq,notificationB4,notificationType,recurrenceFlag," +
	   					 "recurrenceEndDay)" +
	   					 "values('" + name + "','" + description + "','" + parentId + "','"
	   					 + startTimeString + "','" + endTimeString + "','"
	   					 + notificationFreq + "','" + notificationB4 + "','" +
	   					 notificationType + "','" + recurrenceFlag + "','" +     recurrenceEndDay + "')"; 
	   			 
	   			 if(!executeSql(sql))
	   				 return 0;
	   			c.add(Calendar.DATE, incr);
	   			startDate = (Date) c.getTime();
	   			 
	   			c1.add(Calendar.DATE, incr);
	   			endDayTime = c1.getTime();
	   			 
	   			 
	   		 }// end while
	   							 
	   	 } else if(recurrenceFlag.equals("monthly")){
	   		 
	   		 ContentValues values= new ContentValues();
	   		 
	   		 values.put("name", name);
	   		 values.put("description", description);
	   		 values.put("eventStartDayTime", eventStartDayTime);
	   		 values.put("eventEndDayTime", eventEndDayTime);
	   		 values.put("notificationFreq", notificationFreq);
	   		 values.put("notificationB4", notificationB4);
	   		 values.put("notificationType", notificationType);
	   		 values.put("recurrenceFlag", recurrenceFlag);
	   		 values.put("recurrenceEndDay", recurrenceEndDay);
	   		 
	   		 parentId = database.insert("master_event", "", values);
	   		 
	   		 if(parentId == -1)
	   			 return 0;
	   		 
	   		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	   		 
	   		 try {  
	   			startDate = format.parse(eventStartDayTime);
	   			endDate = format.parse(recurrenceEndDay);
	   			endDayTime = format.parse(eventEndDayTime);
	   			//System.out.println(date);  
	   		 } catch (ParseException e) {  
	   			// TODO Auto-generated catch block  
	   			e.printStackTrace();  
	   		 } catch (java.text.ParseException e) {
	   			 // TODO Auto-generated catch block
	   			 e.printStackTrace();
	   		 }
	   		 
	   		Calendar c = Calendar.getInstance();
	   		c.setTime(startDate);
	   		    
	   		Calendar c1 = Calendar.getInstance();
	   		c1.setTime(endDayTime);
	   			
	   		c.add(Calendar.MONTH, 1);
	   		startDate = c.getTime();
	   			 
	   		c1.add(Calendar.MONTH, 1);
	   		endDayTime = c1.getTime();
	   			 
	   		    
	   		 while(startDate.before(endDate)){
	   			 
	   			 
	   			 startTimeString = format.format(startDate);
	   			 endTimeString = format.format(endDayTime);
	   			 
	   			 sql = "insert into master_event (name,description,parentId,eventStartDayTime," +
	   					 "eventEndDayTime,notificationFreq,notificationB4,notificationType,recurrenceFlag," +
	   					 "recurrenceEndDay)" +
	   					 "values('" + name + "','" + description + "','" + parentId + "','"
	   					 + startTimeString + "','" + endTimeString + "','"
	   					 + notificationFreq + "','" + notificationB4 + "','" +
	   					 notificationType + "','" + recurrenceFlag + "','" +     recurrenceEndDay + "')"; 
	   			 
	   			 if(!executeSql(sql))
	   				 return 0;
	   			c.add(Calendar.MONTH, 1);
	   			startDate = (Date) c.getTime();
	   			 
	   			c1.add(Calendar.MONTH, 1);
	   			endDayTime = c1.getTime();
	   			 
	   			 
	   		 } //end while
	   		 
	   	 }else{
	   		 return 0;
	   	 }
	   	 
	    return 1;
    }

	public void deleteEvents(long id) {		
		//TODO delete all rows that has the perentID equal to given ID
		
		database.execSQL("delete from master_event where id = " + id);
	}
	
	public int getNotificationB4(){
		
		Cursor cursor;
		cursor = database.rawQuery("SELECT integerValue FROM global_config " +
				"WHERE property = 'NotificationB4' ", null);        
		return Integer.parseInt(cursor.getString(0));
	}
	
	public int getNotificationFreq(){
		Cursor cursor;
		cursor = database.rawQuery("SELECT integerValue FROM global_config " +
				"WHERE property = 'NotificationFreq' ", null);        
		return Integer.parseInt(cursor.getString(0));
	}
	
	public String getNotificationType(){
		Cursor cursor;
		cursor = database.rawQuery("SELECT textValue FROM global_config " +
				"WHERE property = 'NotificationType' ", null);        
		return cursor.getString(0);
	}
	public String getRecurrenceFlag(){
		
		return null;
	}
	
	public boolean setNotificationB4(int newValue){
						
		String sql = "UPDATE global_config SET intergerValue = "+ newValue +" WHERE property = 'NotificationB4'";
        database.beginTransaction();
        SQLiteStatement sqlStmt = database.compileStatement(sql);
        try{
        	sqlStmt.execute();
            database.setTransactionSuccessful();
            
        }catch(SQLiteException ex){
        	
        	ex.printStackTrace();
        	return false;
        	
        }finally{
        	database.endTransaction();
        }
		
		return true;
		
	}
    public boolean setNotificationFreq(int newValue){
    	
    	String sql = "UPDATE global_config SET intergerValue = "+ newValue +" WHERE property = 'NotificationFreq'";
        database.beginTransaction();
        SQLiteStatement sqlStmt = database.compileStatement(sql);
        try{
        	sqlStmt.execute();
            database.setTransactionSuccessful();
            
        }catch(SQLiteException ex){
        	
        	ex.printStackTrace();
        	return false;
        	
        }finally{
        	database.endTransaction();
        }
		return true;
		
	}
    public boolean setNotificationType(String txt){
    	
    	String sql = "UPDATE global_config SET textValue = '"+ txt +"' WHERE property = 'NotificationType'";
        database.beginTransaction();
        SQLiteStatement sqlStmt = database.compileStatement(sql);
        try{
        	sqlStmt.execute();
            database.setTransactionSuccessful();
            
        }catch(SQLiteException ex){
        	
        	ex.printStackTrace();
        	return false;
        	
        }finally{
        	database.endTransaction();
        }
		return true;
		
	}
    
    public boolean setRecurrenceFlag(String txt){
    	
    	String sql = "UPDATE global_config SET TextValue = "+ txt +" WHERE Property = 'RecurrenceFlag'";
        database.beginTransaction();
        SQLiteStatement sqlStmt = database.compileStatement(sql);
        try{
        	sqlStmt.execute();
            database.setTransactionSuccessful();
            
        }catch(SQLiteException ex){
        	
        	ex.printStackTrace();
        	return false;
        	
        }finally{
        	database.endTransaction();
        }
		return true;
		
	}
    public boolean setRecurrenceEndTime(String txt){
		
    	String sql = "UPDATE global_config SET TextValue = "+ txt +" WHERE Property = 'RecurrenceEndTime'";
        database.beginTransaction();
        SQLiteStatement sqlStmt = database.compileStatement(sql);
        try{
        	sqlStmt.execute();
            database.setTransactionSuccessful();
            
        }catch(SQLiteException ex){
        	
        	ex.printStackTrace();
        	return false;
        	
        }finally{
        	database.endTransaction();
        }
		return true;
		
	}
	
	public Cursor getAllEvents() {
		return database.query("master_event", new String[] {"id", "recurrenceFlag","name", "description"},
		null, null, null, null, null);
	}
	
	
	public Cursor getOneEvent(long id) {
		return database.query("master_event", null, "id=" + id,
		null, null, null, null);
	}
	
	public void updateEvent(long id, String title, String body) {
		ContentValues editCon = new ContentValues();
		editCon.put("title", title);
		editCon.put("body", body);		
		database.update("master_event", editCon, "_id=" + id, null);
	}
	
	public boolean editEvent(
					 int sId,
					 String name, 
			         String description, 
			         String eventStartDayTime, 
			         String eventEndDayTime,
			         String notificationFreq,
			         String notificationB4, 
			         String notificationType, 
			         String recurrenceFlag, 
			         String recurrenceEndDay)
	{
				//TODO createEvent
				
				String sql = "update master_event set name = '" + name + "'," +
						"description= '" + description + "'," +
						"eventStartDayTime = '" + eventStartDayTime + "'," +
						"eventEndDayTime = '" + eventEndDayTime + "'," +
						"notificationFreq = '" + notificationFreq + "'," +
						"notificationB4 = '" + notificationB4 + "'," +
						"notificationType = '" + notificationType + "'," +
						"recurrenceFlag = '" + recurrenceFlag + "'," +
						"recurrenceEndDay = '" + recurrenceEndDay + "'" +
						" where id = " + sId;
				
				database.beginTransaction();
		        SQLiteStatement sqlStmt = database.compileStatement(sql);
		        try{
		        	sqlStmt.execute();
		            database.setTransactionSuccessful();
		            
		        }catch(SQLiteException ex){
		        	
		        	ex.printStackTrace();
		        	return false;
		        	
		        }finally{
		        	database.endTransaction();
		        }
				
				return true;
	}
		
}
