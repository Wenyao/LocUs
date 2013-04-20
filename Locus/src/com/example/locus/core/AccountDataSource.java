package com.example.locus.core;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.locus.entity.User;

public class AccountDataSource {
	private SQLiteDatabase database;
	private AccountSQLiteHelper dbHelper;
	private String[] allColumns = { 
			AccountSQLiteHelper.COLUMN_ID,
			AccountSQLiteHelper.COLUMN_NAME, 
			AccountSQLiteHelper.COLUMN_SEX,
			AccountSQLiteHelper.COLUMN_INTEREST,
			AccountSQLiteHelper.COLUMN_PIC
			};

	public AccountDataSource(Context context) {
		dbHelper = new AccountSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public User createUser(User user) {
		ContentValues values = new ContentValues();
		values.put(AccountSQLiteHelper.COLUMN_ID, user.getId());
		values.put(AccountSQLiteHelper.COLUMN_NAME, user.getName());
		values.put(AccountSQLiteHelper.COLUMN_SEX, user.getSex().getValue());
		values.put(AccountSQLiteHelper.COLUMN_INTEREST, user.getInterests());
		values.put(AccountSQLiteHelper.COLUMN_PIC, user.getPicURL());
		long insertId = database.insert(AccountSQLiteHelper.TABLE_ACCOUNT, null,
				values);
		Cursor cursor = database.query(AccountSQLiteHelper.TABLE_ACCOUNT,
				allColumns, AccountSQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		User newUser = cursorToUser(cursor);
		cursor.close();
		return newUser;
	}

	public void deleteUser(User user) {
		String id = user.getId();
		System.out.println("User deleted with id: " + id);
		database.delete(AccountSQLiteHelper.TABLE_ACCOUNT, AccountSQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();

		Cursor cursor = database.query(AccountSQLiteHelper.TABLE_ACCOUNT,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			User user = cursorToUser(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return users;
	}

	private User cursorToUser(Cursor cursor) {
		User user = new User(
				cursor.getString(1),
				cursor.getInt(2),
				cursor.getString(3),
				cursor.getString(4));
		user.setId(cursor.getString(0));
		return user;
	}
}
