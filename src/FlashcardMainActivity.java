package com.example.choisquidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class FlashcardMainActivity extends AppCompatActivity {

    long initialTime;

    //Database
    DBHelper wordDBHelper;
    String DB_NAME = "dic1800.db";
    private int myWordCount = 0;
    String userName;
    String TAG = "오지웅게임팀_데모영상";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_main);
        Log.d(TAG, "Flashcard Main Activity > onCreate");
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        wordDBHelper = new DBHelper(this, DB_NAME);
        fetchData();
    }


    public void fetchData() {
        try {
            wordDBHelper.createDatabase(DB_NAME);
            wordDBHelper.openDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase sql = wordDBHelper.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM dict_tbl", null);
        if (cursor.moveToFirst()) {
            do {
                int isMyWord = cursor.getInt(3);
                if (isMyWord==1){
                    myWordCount++;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    public void onClick(View view) {
        if (view.getId()==R.id.wholeWordsButton) {
            Intent intent = new Intent(this, FlashcardGameActivity.class);
            intent.putExtra("isWholeWord", true);
            intent.putExtra("userName", userName);
            startActivity(intent);
            finish();
        } else if (view.getId()==R.id.myWordsButton){
            //isMyWord==1인개수가 3미만이면 실행x
            if (myWordCount>=3) {
                Intent intent = new Intent(this, FlashcardGameActivity.class);
                intent.putExtra("isWholeWord", false);
                intent.putExtra("myWordCount", myWordCount);
                intent.putExtra("userName", userName);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "Add at least 3 words in myWordList.", Toast.LENGTH_SHORT).show();
            }

        }
        else if (view.getId()==R.id.flashCardRankButton) {
            startActivity(new Intent(this, FlashcardRankActivity.class));
        }
        else if (view.getId()==R.id.flashCardSettingsButton){
            startActivity(new Intent(this, FlashcardSettingActivity.class));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis()-initialTime>3000){
                Toast.makeText(this, "Press once more to exit.", Toast.LENGTH_SHORT).show();
                initialTime=System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}