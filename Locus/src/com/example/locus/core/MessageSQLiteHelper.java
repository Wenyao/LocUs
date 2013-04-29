package com.example.locus.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MessageSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_MESSAGE = "message";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SRC_ID = "srcId";
	public static final String COLUMN_DES_ID = "desId";
	public static final String COLUMN_KIND = "kind";
	public static final String COLUMN_DATA = "data";
	public static final String COLUMN_MSG_ID = "msgId";

	private static final String DATABASE_NAME = "locus_msg.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = String
			.format("create table %s(%s integer primary key autoincrement, %s text not null, %s text not null, %s text, %s text, %s integer);",
					TABLE_MESSAGE, COLUMN_ID, COLUMN_SRC_ID, COLUMN_DES_ID,
					COLUMN_KIND, COLUMN_DATA, COLUMN_MSG_ID);

	public MessageSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(Constants.AppCoreTag, "enter create db for message");
		try {
			db.execSQL(DATABASE_CREATE);
		} catch (Exception e) {
			Log.e(Constants.AppCoreTag, e.toString());
		}
		Log.i(Constants.AppCoreTag, "exit create db for message");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MessageSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
		onCreate(db);
	}

}
