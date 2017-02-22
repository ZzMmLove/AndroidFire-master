package com.gdgst.androidfire.ui.main.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * �������ݿ���࣬������ݱ���롢ɾ������ѯ�Լ��޸������ֶε���ز���
 * @author grace
 *
 */
public class DatabaseUtil {	
	private static SQLiteDatabase db = null;
	private static DatabaseHelper helper = null;

	//�������ֶ�
	public static long insert(Context context, String tableName, String id, ContentValues values) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		long rows = db.insert(tableName,id,values);
		closeDatabase();
		return rows;
	}
	
	//���h���ֶ�
	public static int delete(Context context, String tableName, String where, String[] whereArgs) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		int rows = db.delete(tableName,where,whereArgs);
		closeDatabase();
		return rows;
	}
	
	//ʹ��SQL���ɾ���ֶ�
	public static void delete(Context context, String sql) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		db.execSQL(sql);
		closeDatabase();
	}
	
	//����޸��ֶ�
	public static int update(Context context, String tableName, ContentValues values, String where, String[] whereArgs) {
		helper = new DatabaseHelper(context);
		db = helper.getWritableDatabase();
		int rows = db.update(tableName,values,where,whereArgs);
		closeDatabase();
		return rows;
	}
	
	//����ѯ����
	public static Cursor query(Context context, String table, 
			String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		helper = new DatabaseHelper(context);
		db = helper.getReadableDatabase();
		return db.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
	}
	
	public static void closeDatabase() {
		db.close();
	}
}
