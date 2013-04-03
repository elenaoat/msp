package com.taskmanager;

import android.content.ContentValues;
//import java.util.Date;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteException;

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
		Open();
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
		Close();
    }
	
	public Cursor viewEventByID(long id){
		
		return database.query("master_event", null, "id=" + id,
				null, null, null, null);
		
	}
	// User must input name, dueDate 
	public long createEvent
	        (String name, 
	         String description, 
	         String dueDate, 
	         int notB4, int notFreq, 
	         String notType, 
	         String recFlag, String recEndTime )
	{
		//TODO createEvent
		ContentValues values = new ContentValues();
		
		values.put("name", recFlag);
		values.put("recurrenceFlag", recFlag);
		values.put("description", description);
		values.put("notificationB4", notB4);
		values.put("notificationFreq", notFreq);
		values.put("notificationType", notType);
		values.put("recurrenceEndDate", recEndTime);
		values.put("dueDate", dueDate);
		
		return database.insert("master_event", null , values);
		
	}
	public void deleteEvents(long id) {		
		//TODO delete all rows that has the perentID equal to given ID
		//database.delete("master_event", "id=" + id, null);	
		
		database.execSQL("delete from master_event where id='"+id+"' or parentID = id '"+id+"'");
	}
	
	public int getNotificationB4(){
		
		Cursor cursor;
		cursor = database.rawQuery("SELECT intergerValue FROM global_config " +
				"WHERE property = 'NotificationB4' ", null);        
		return Integer.parseInt(cursor.getString(0));
	}
	
	public int getNotificationFreq(){
		Cursor cursor;
		cursor = database.rawQuery("SELECT intergerValue FROM global_config " +
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
			 long id,
			 String name,  // mandatory
	         String description, 
	         String dueDate,  // mandatory
	         int notB4, int notFreq, 
	         String notType, 
	         String recFlag, String recEndTime)
	{
		
		ContentValues editCon = new ContentValues();
		
		
		editCon.put("recurrenceFlag", recFlag);
		editCon.put("recurrenceEndDate", recEndTime);
		editCon.put("name", name);
		editCon.put("description", description);
		editCon.put("dueDate", dueDate);
		editCon.put("notificationB4", notB4);
		editCon.put("notificationType", notType);
		
		
				
		database.update("master_event", editCon, "id=" + id, null);
		
		return true;
	}
	
	
}
