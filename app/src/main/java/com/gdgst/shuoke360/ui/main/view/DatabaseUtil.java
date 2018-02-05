package com.gdgst.shuoke360.ui.main.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * SQLite数据库基本操作的工具类
 * @author grace
 *
 */
public class DatabaseUtil {
	private static SQLiteDatabase db = null;
	private static DatabaseHelper helper = null;

	//通过API来插入数据
	public static long insert(Context context, String tableName, String id, ContentValues values) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		long rows = db.insert(tableName,id,values);
		closeDatabase();
		return rows;
	}
	
	//通过delete方法来删除
	public static int delete(Context context, String tableName, String where, String[] whereArgs) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		int rows = db.delete(tableName,where,whereArgs);
		closeDatabase();
		return rows;
	}
	
	//通过SQL语句来删除
	public static void delete(Context context, String sql) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		db.execSQL(sql);
		closeDatabase();
	}

	//通过API来更新数据
	public static int update(Context context, String tableName, ContentValues values, String where, String[] whereArgs) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		int rows = db.update(tableName,values,where,whereArgs);
		closeDatabase();
		return rows;
	}
	
	//查询
	public static Cursor query(Context context, String table, 
			String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		helper = new DatabaseHelper(context);
		db = helper.getReadableDatabase();
		return db.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
	}

	//关闭数据库
	public static void closeDatabase() {
		db.close();
	}
}
