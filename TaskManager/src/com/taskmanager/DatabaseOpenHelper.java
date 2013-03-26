package com.taskmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "events_database.db";
	public static final String TABLE_NAME = "events";
	
	public static final String TABLE_SQL = "CREATE TABLE " + TABLE_NAME
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "datetime TEXT, "
			+ "title TEXT, "
			+ "body TEXT);";
	
	public DatabaseOpenHelper(Context context) {
		super(context, DB_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(TABLE_SQL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
