package com.example.locus.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AccountSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_ACCOUNT = "account";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SEX = "sex";
	public static final String COLUMN_INTEREST = "interest";
	public static final String COLUMN_PIC = "pic";
	public static final String COLUMN_LATI = "lati";
	public static final String COLUMN_LONGTI = "longti";
	public static final String COLUMN_LOGGEDIN = "loggedin";

	private static final String DATABASE_NAME = "locus.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = String
			.format("create table %s(%s text primary key ON CONFLICT REPLACE, %s text not null, %s integer, %s text, %s text, %s text, %s text, %s text);",
					TABLE_ACCOUNT, COLUMN_ID, COLUMN_NAME, COLUMN_SEX,
					COLUMN_INTEREST, COLUMN_PIC, COLUMN_LATI, COLUMN_LONGTI, COLUMN_LOGGEDIN);

	public AccountSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(Constants.AppCoreTag, "enter create db for account");
		try {
			db.execSQL(DATABASE_CREATE);
		} catch (Exception e) {
			Log.e(Constants.AppCoreTag, e.toString());
		}
		Log.i(Constants.AppCoreTag, "exit create db for account");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(AccountSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
		onCreate(db);
	}

}
