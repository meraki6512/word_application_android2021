package com.example.choisquidgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AlarmQuizDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "오지웅게임팀_데모영상";

    private static File DB_FILE;
    private static String DB_NAME = "dic1800.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public AlarmQuizDBHelper(Context context){
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        DB_FILE = myContext.getDatabasePath(DB_NAME);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        Log.d("myMessage", "50. DB_PATH: "+DB_FILE);
        if (dbExist) {
            //do nothing
        } else {
            this.getReadableDatabase();
            this.close();
            try {
                copyDatabase();
                Log.d(TAG, "createDataBase: 카피중");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }
    private boolean checkDataBase() {
        return DB_FILE.exists();
    }

    private void copyDatabase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(DB_FILE);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            mOutput.write(buffer, 0, length);
        }
        mOutput.flush();
        mOutput.close();
        myInput.close();
    }

    public boolean openDataBase(){
        myDataBase = SQLiteDatabase.openDatabase(DB_FILE.toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        // mDataBase = SQLiteDatabase.openDatabase(DB_FILE, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return myDataBase != null;
    }


    @Override
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
}
