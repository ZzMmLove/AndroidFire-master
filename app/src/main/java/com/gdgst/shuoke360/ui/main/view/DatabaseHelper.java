package com.gdgst.shuoke360.ui.main.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

/**
 * �������ݿ��һ���࣬��ɴ���Record���ɾ��Record��Ĳ���
 * @author grace
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	private static int DATABASE_VERSION = 1;  //���ݿ�汾
	private static String DATABASE_NAME = "lzshow"; //���ݿ�����
	
	//����Record��
	private static String CREATE_RECORD_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + Record.TABLE_NAME + "("
		+ Record.ID + " INTEGER PRIMARY KEY,"
		+ Record.DATE + " TEXT,"
		+ Record.FONTNUM + " INTEGER)";
	
	private static String CREATE_USER_RECORD_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "+UserRecord.TABLE_NAME+"("
	+UserRecord.ID+" INTEGER PRIMARY KEY,"
	+UserRecord.FONTLIB_NAME+" TEXT,"
	+UserRecord.UPDATE_INDEX+" INTEGER,"
	+UserRecord.UPDATE_TIME+" TEXT)";
	
	//ɾ��Record��
	private static String DROP_RECORD_TABLE_SQL = "DROP TABLE IF EXISTS " + Record.TABLE_NAME;
	
	private static String DROP_USER_RECORD_TABLE_SQL = "DROP TABLE IF EXISTS " + UserRecord.TABLE_NAME;
	
	
	public DatabaseHelper(Context context) {
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}
	
	/**
	 * ����Record��
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_RECORD_TABLE_SQL); //����Record��
		db.execSQL(CREATE_USER_RECORD_TABLE_SQL);
		autoInsertData(db);
	}

	/**
	 * ����Record��
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_RECORD_TABLE_SQL); //ɾ��Record��
		db.execSQL(DROP_USER_RECORD_TABLE_SQL);
		onCreate(db);//����Record��
	}
	
	private ContentValues autoGenerateValues(String date, int fontNum) {
		ContentValues values = new ContentValues();
		values.put(Record.DATE, date);
		values.put(Record.FONTNUM, fontNum);
		return values;
	}
	
	private void autoInsertData(SQLiteDatabase db) {
		// start from 2012-6-1 2012-7-18
		String date = "2012-06-";
		Random random = new Random();
		int fontNum = 0;
		int min = 20;
		int max = 200;
		for(int day=1; day<31; day++)
		{
			fontNum = random.nextInt(max)%(max-min+1) + min;
			db.insert(Record.TABLE_NAME, Record.ID, autoGenerateValues(date+(day<10?"0"+day:day), fontNum));
		}
		date = "2012-07-";
		for(int day=1; day<19; day++)
		{
			fontNum = random.nextInt(max)%(max-min+1) + min;
			db.insert(Record.TABLE_NAME, Record.ID, autoGenerateValues(date+(day<10?"0"+day:day), fontNum));
		}
	}
}
