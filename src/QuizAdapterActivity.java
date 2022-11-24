package com.example.choisquidgame;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class QuizAdapterActivity {

    protected static final String TAG = "오지웅게임팀_데모영상  DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private AlarmQuizDBHelper mDbHelperQuiz;
    private SQLiteOpenHelper sqLiteOpenHelper;

    public QuizAdapterActivity(Context context) {
        this.mContext = context;
        mDbHelperQuiz = new AlarmQuizDBHelper(mContext);
    }

    public QuizAdapterActivity createDatabase() throws SQLException {
        try {
            mDbHelperQuiz.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public QuizAdapterActivity open() throws SQLException {
        try {
            mDbHelperQuiz.openDataBase();
            //mDbHelperQuiz.close();
            mDb = mDbHelperQuiz.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelperQuiz.close();
    }

    public ArrayList<String> getWordList(Activity activity)
    {
        sqLiteOpenHelper = new AlarmQuizDBHelper(activity);
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ArrayList<String> wordList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM dict_tbl", null);
        if (cursor.moveToFirst()) {
            do {
                wordList.add(cursor.getString(1));

            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public ArrayList<String> getMeaningList(Activity activity)
    {
        sqLiteOpenHelper = new AlarmQuizDBHelper(activity);
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ArrayList<String> meaningList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM dict_tbl", null);
        if (cursor.moveToFirst()) {
            do {
                meaningList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return meaningList;
    }

}