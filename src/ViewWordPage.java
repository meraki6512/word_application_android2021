package com.example.choisquidgame;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewWordPage extends AppCompatActivity {

    String DB_NAME;
    String TAG = "오지웅게임팀_데모영상";
    private boolean isWholeWord = true; //get intent

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.word_detail);

        Log.d(TAG, "ViewWordPage > onCreate");

        Intent intent = getIntent();
        String question = intent.getStringExtra("question");

        if (isWholeWord) {
            DB_NAME = "dic1800.db";
        }
        /*
        else {
            DB_NAME = "myWordList.db";
        }
        */
        DBHelper dbHelper = new DBHelper(this, DB_NAME);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db. rawQuery("SELECT answer FROM dict_tbl WHERE question=?", new String[]{question});

        ArrayList<String> data = new ArrayList<>();

        while (cursor.moveToNext()){
            data.add(cursor.getString(0));
        }

        db.close();

        TextView word = findViewById(R.id.question_detail);
        TextView meaning1 = findViewById(R.id.answer_detail1);

        word.setText(question);
        meaning1.setText(data.toString());

        setResult(RESULT_OK, intent);

        finish();
    }
}