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

	public DatabaseAdapter(Context c) {
		this.context = c;
		dbHelper = new DatabaseOpenHelper(context);
	}

	public void Open() {
		database = dbHelper.getWritableDatabase();
	}

	public void Close() {
		database.close();
	}

	public Cursor getAllConfig() {

		return database.query("global_config", new String[] { "property",
				"valueType", "textValue", "intergerValue", "dateValue" }, null,
				null, null, null, null);
	}

	// initialize global configuration table
	public void initializeGConfig() {
		this.Open();
		ContentValues values = new ContentValues();

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

	public Cursor viewEventByID(long id) {

		return database.query("master_event", null, "id=" + id, null, null,
				null, null);

	}

	/*
	 * This method returns all the events for a particular date. Input parameter
	 * format: 'YYYY-MM-DD' Input parameter Example: '2013-05-09' Output: Cursor
	 * having the following columns:
	 * name,description,eventStartDayTime,eventEndDayTime
	 */
	public Cursor getEventByDate(String searchDate) {

		Cursor cursor;
		cursor = database
				.rawQuery(
						"SELECT id,name,description,eventStartDayTime,eventEndDayTime FROM master_event "
								+ "WHERE substr(eventStartDayTime,1,10) = '"
								+ searchDate + "'", null);
		return cursor;
	}

	/*
	 * Insert a new event in the database Input parameter format: all are String
	 * Output: 0 = failure; 1 = success
	 */
	public long createEvent(String name, String description,
			String eventStartDayTime, String eventEndDayTime,
			String notificationFreq, String notificationB4,
			String notificationType, String recurrenceFlag,
			String recurrenceEndDay) {
		// TODO createEvent

		String sql = "insert into master_event (name,description,eventStartDayTime,"
				+ "eventEndDayTime,notificationFreq,notificationB4,notificationType,recurrenceFlag,"
				+ "recurrenceEndDay)"
				+ "values('"
				+ name
				+ "','"
				+ description
				+ "','"
				+ eventStartDayTime
				+ "','"
				+ eventEndDayTime
				+ "','"
				+ notificationFreq
				+ "','"
				+ notificationB4
				+ "','"
				+ notificationType
				+ "','"
				+ recurrenceFlag
				+ "','"
				+ recurrenceEndDay + "')";

		database.beginTransaction();
		SQLiteStatement sqlStmt = database.compileStatement(sql);
		try {
			sqlStmt.execute();
			database.setTransactionSuccessful();

		} catch (SQLiteException ex) {

			ex.printStackTrace();
			return 0;

		} finally {
			database.endTransaction();
		}

		return 1;
	}

	public long createBriefEvent(String name, String description,
			String eventStartDayTime, String eventEndDayTime) {
		// TODO createEvent

		String sql = "insert into master_event (name,description,eventStartDayTime,"
				+ "eventEndDayTime)"
				+ "values('"
				+ name
				+ "','"
				+ description
				+ "','" + eventStartDayTime + "','" + eventEndDayTime + "')";

		database.beginTransaction();
		SQLiteStatement sqlStmt = database.compileStatement(sql);
		try {
			sqlStmt.execute();
			database.setTransactionSuccessful();

		} catch (SQLiteException ex) {

			ex.printStackTrace();
			return 0;

		} finally {
			database.endTransaction();
		}

		return 1;
	}

	public void deleteEvents(long id) {
		// TODO delete all rows that has the perentID equal to given ID

		database.execSQL("delete from master_event where id = " + id);
	}

	public int getNotificationB4() {

		Cursor cursor;
		cursor = database.rawQuery("SELECT integerValue FROM global_config "
				+ "WHERE property = 'NotificationB4' ", null);
		return Integer.parseInt(cursor.getString(0));
	}

	public int getNotificationFreq() {
		Cursor cursor;
		cursor = database.rawQuery("SELECT integerValue FROM global_config "
				+ "WHERE property = 'NotificationFreq' ", null);
		return Integer.parseInt(cursor.getString(0));
	}

	public String getNotificationType() {
		Cursor cursor;
		cursor = database.rawQuery("SELECT textValue FROM global_config "
				+ "WHERE property = 'NotificationType' ", null);
		return cursor.getString(0);
	}

	public String getRecurrenceFlag() {

		return null;
	}

	public boolean setNotificationB4(int newValue) {

		String sql = "UPDATE global_config SET intergerValue = " + newValue
				+ " WHERE property = 'NotificationB4'";
		database.beginTransaction();
		SQLiteStatement sqlStmt = database.compileStatement(sql);
		try {
			sqlStmt.execute();
			database.setTransactionSuccessful();

		} catch (SQLiteException ex) {

			ex.printStackTrace();
			return false;

		} finally {
			database.endTransaction();
		}

		return true;

	}

	public boolean setNotificationFreq(int newValue) {

		String sql = "UPDATE global_config SET intergerValue = " + newValue
				+ " WHERE property = 'NotificationFreq'";
		database.beginTransaction();
		SQLiteStatement sqlStmt = database.compileStatement(sql);
		try {
			sqlStmt.execute();
			database.setTransactionSuccessful();

		} catch (SQLiteException ex) {

			ex.printStackTrace();
			return false;

		} finally {
			database.endTransaction();
		}
		return true;

	}

	public boolean setNotificationType(String txt) {

		String sql = "UPDATE global_config SET textValue = '" + txt
				+ "' WHERE property = 'NotificationType'";
		database.beginTransaction();
		SQLiteStatement sqlStmt = database.compileStatement(sql);
		try {
			sqlStmt.execute();
			database.setTransactionSuccessful();

		} catch (SQLiteException ex) {

			ex.printStackTrace();
			return false;

		} finally {
			database.endTransaction();
		}
		return true;

	}

	public boolean setRecurrenceFlag(String txt) {

		String sql = "UPDATE global_config SET TextValue = " + txt
				+ " WHERE Property = 'RecurrenceFlag'";
		database.beginTransaction();
		SQLiteStatement sqlStmt = database.compileStatement(sql);
		try {
			sqlStmt.execute();
			database.setTransactionSuccessful();

		} catch (SQLiteException ex) {

			ex.printStackTrace();
			return false;

		} finally {
			database.endTransaction();
		}
		return true;

	}

	public boolean setRecurrenceEndTime(String txt) {

		String sql = "UPDATE global_config SET TextValue = " + txt
				+ " WHERE Property = 'RecurrenceEndTime'";
		database.beginTransaction();
		SQLiteStatement sqlStmt = database.compileStatement(sql);
		try {
			sqlStmt.execute();
			database.setTransactionSuccessful();

		} catch (SQLiteException ex) {

			ex.printStackTrace();
			return false;

		} finally {
			database.endTransaction();
		}
		return true;

	}

	public Cursor getAllEvents() {
		return database.query("master_event", new String[] { "id",
				"recurrenceFlag", "name", "description" }, null, null, null,
				null, null);
	}

	public Cursor getOneEvent(long id) {
		return database.query("master_event", null, "id=" + id, null, null,
				null, null);
	}

	public void updateEvent(long id, String title, String body) {
		ContentValues editCon = new ContentValues();
		editCon.put("title", title);
		editCon.put("body", body);
		database.update("master_event", editCon, "_id=" + id, null);
	}

	public boolean editEvent(int sId, String name, String description,
			String eventStartDayTime, String eventEndDayTime,
			String notificationFreq, String notificationB4,
			String notificationType, String recurrenceFlag,
			String recurrenceEndDay) {
		// TODO createEvent

		String sql = "update master_event set name = '" + name + "',"
				+ "description= '" + description + "',"
				+ "eventStartDayTime = '" + eventStartDayTime + "',"
				+ "eventEndDayTime = '" + eventEndDayTime + "',"
				+ "notificationFreq = '" + notificationFreq + "',"
				+ "notificationB4 = '" + notificationB4 + "',"
				+ "notificationType = '" + notificationType + "',"
				+ "recurrenceFlag = '" + recurrenceFlag + "',"
				+ "recurrenceEndDay = '" + recurrenceEndDay + "'"
				+ " where id = " + sId;

		database.beginTransaction();
		SQLiteStatement sqlStmt = database.compileStatement(sql);
		try {
			sqlStmt.execute();
			database.setTransactionSuccessful();

		} catch (SQLiteException ex) {

			ex.printStackTrace();
			return false;

		} finally {
			database.endTransaction();
		}

		return true;
	}

}
