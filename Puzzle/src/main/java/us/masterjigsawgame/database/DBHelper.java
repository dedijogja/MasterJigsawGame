package us.masterjigsawgame.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import us.masterjigsawgame.util.AppConstants;


class DBHelper extends SQLiteOpenHelper {

	private static int DATABASE_VERSION = 1;

	private static DBHelper dbHelper;
	private SQLiteDatabase sqLiteDatabase;

	private DBHelper(Context context) {
		super(context, AppConstants.DATABASE_NAME, null, DATABASE_VERSION);
		sqLiteDatabase = this.getWritableDatabase();
	}

	SQLiteDatabase getSqLiteDb() {
		return sqLiteDatabase;
	}

	static DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
		if (!dbHelper.sqLiteDatabase.isOpen()) {
			dbHelper.sqLiteDatabase = dbHelper.getWritableDatabase();
		}
		return dbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

	int delete(String table, String whereClause, String[] whereArgs) {
		return sqLiteDatabase.delete(table, whereClause, whereArgs);
	}

	Cursor select(String table, String[] columns, String selection,
				  String[] selectionArgs, String groupBy, String having,
				  String orderBy) {
		return sqLiteDatabase.query(table, columns, selection, selectionArgs,
				groupBy, having, orderBy);
	}

}
