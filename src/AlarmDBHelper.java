package com.example.choisquidgame;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDBHelper extends SQLiteOpenHelper {

    public AlarmDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS mytable (" +
                "_id INTEGER, " +
                "hour INTEGER, " +
                "minutes INTEGER, " +
                "month INTEGER, " +
                "day INTEGER, " +
                "enabled INTEGER, " +
                "vibrate INTEGER, " +
                "am_pm TEXT, " +
                "alert TEXT);");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 버전이 증가하면 해당 테이블을 삭제하고 다시 생성합니다.
        db.execSQL("DROP TABLE IF EXISTS mytable");
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }





}