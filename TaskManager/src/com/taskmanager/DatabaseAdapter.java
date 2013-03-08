package com.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
	SQLiteDatabase database;
	DatabaseOpenHelper dbHelper;
	
	public DatabaseAdapter(Context context)
	{
		dbHelper = new DatabaseOpenHelper(context);
	}
	
	public void Open()
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void Close()
	{
		database.close();
	}
	
	public long InsertNote(String title, String body)
	{
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("body", body);
		return database.insert("events", "", values);		
	}
	
	public Cursor getAllEvents() {
		return database.query("events", new String[] {"_id", "title", "body"},
		null, null, null, null, null);
	}
	
	public Cursor getOneEvent(long id) {
		return database.query("events", null, "_id=" + id,
		null, null, null, null);
	}
	
	public void updateEvent(long id, String title, String body) {
		ContentValues editCon = new ContentValues();
		editCon.put("title", title);
		editCon.put("body", body);		
		database.update("events", editCon, "_id=" + id, null);
	}
	
	public void deleteEvents(long id) {		
		database.delete("events", "_id=" + id, null);		
	}
}
