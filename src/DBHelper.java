package com.example.choisquidgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    private static String DB_PATH = "";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DBHelper(Context context, String DB_NAME) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        DB_PATH = myContext.getDatabasePath(DB_NAME).toString();
        Log.d("myMessage", "FR) DB_PATH: " + DB_PATH);
    }

    public void createDatabase(String DB_NAME) throws IOException {
        boolean dbExist = checkDatabase();
        Log.d("myMessage", "checkDB != null: "+dbExist);
        if (dbExist) {
            //do nothing
            Log.d("myMessage", "dbExist");
        } else {
            this.getWritableDatabase();
            try {
                copyDatabase(DB_NAME);
            } catch (IOException e) {
                throw new Error("Error copying databases");
            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH;
            Log.d("myMessage", "JW. check DB_PATH: "+DB_PATH);
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e("myMessage", "" + e);
        }
        if (checkDB != null) {
            Log.e("myMessage", "checkDB!=null" );
            checkDB.close();
        }
        return checkDB != null;
    }
    private void copyDatabase(String DB_NAME) throws IOException {
    //private void copyDatabase() throws IOException {
        Log.d("myMessage", "copyDatabase");
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH;
        Log.d("myMessage", "JW. copy DB_PATH: "+DB_PATH);
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDatabase() throws IOException {
        String myPath = DB_PATH;
        Log.d("myMessage", "JW. open DB_PATH: "+DB_PATH);
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        Log.d("myMessage", "openDatabase");
    }

    public synchronized void close() {
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void updateWord(int isMyWord, int position){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("오지웅게임팀", "JW. update DB_PATH: "+db.getPath());
        try {
            ContentValues cv = new ContentValues();
            cv.put("isMyWord", isMyWord);
            db.update("dict_tbl", cv, "_id=" + (position+1), null);
            Cursor cursor = db.rawQuery("select * from dict_tbl WHERE _id = " + (position+1), null);
            //cursor.moveToFirst();
            Log.d("오지웅게임팀", "isMyWord: " + cursor.getInt(3));
            Log.d("오지웅게임팀", "success: executed SQL: Update dict_tbl set isMyWord");
            db.close();
        } catch (Exception e){
            e.printStackTrace();
            Log.d("오지웅게임팀", "failed: executed SQL: Update dict_tbl set isMyWord");
        }
    }

    boolean addUser(String userName, int score, int[] wrongWordIdx, int isAddwrong)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", userName);
        cv.put("score", score);
        cv.put("wrongWordIdx1", wrongWordIdx[0]);
        cv.put("wrongWordIdx2", wrongWordIdx[1]);
        cv.put("wrongWordIdx3", wrongWordIdx[2]);

        long result = db.insert("user_tbl", null, cv);
        if (result == -1)
        {
            Log.d("myMessage", "addUser Fail");
            Toast.makeText(myContext, "addUser Fail.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            Log.d("myMessage", "addUser Success");
            Toast.makeText(myContext, "addUser Success!", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
