package com.gdgst.shuoke360.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 8/30 0030.
 */

public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String name = "search.db";
    private static Integer version = 1;
    private static final String table = "create table search_history(id integer primary key autoincrement, name varchar(200))";

    public RecordSQLiteOpenHelper(Context context) {
        super(context,name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    //创建数据表，这个表只有一列name来存储搜索的历史记录
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
