package com.example.choisquidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FlashcardRankActivity extends AppCompatActivity {

    //Database
    DBHelper wordDBHelper;
    DBHelper userDBHelper;

    private ArrayList<String> wordList;
    private ArrayList<String[]> meaningList;

    ListView listview ;
    RankListViewAdapter adapter;
    ArrayList<RankListViewItem> itemList = new ArrayList<RankListViewItem>() ;

    String wrongAns;
    String TAG = "오지웅게임팀_데모영상";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_rank);

        Log.d(TAG, "Flashcard Rank Activity > onCreate");

        wordDBHelper = new DBHelper(this, "dic1800.db");
        wordList = new ArrayList<>();
        meaningList = new ArrayList<>();
        fetchWordData();

        adapter = new RankListViewAdapter(itemList);
        fetchUserData();
        sortData();
        listview = findViewById(R.id.flashCardRankListview);
        listview.setAdapter(adapter);
    }

    public void sortData(){
        Comparator<RankListViewItem> scoreDesc = new Comparator<RankListViewItem>() {
            @Override
            public int compare(RankListViewItem item1, RankListViewItem item2) {
                return (item2.getScore() - item1.getScore()) ;
            }
        } ;
        Collections.sort(itemList, scoreDesc) ;
        adapter.notifyDataSetChanged() ;
    }

    public void fetchUserData() {
        try {
            userDBHelper = new DBHelper(this, "user.db");
            userDBHelper.createDatabase("user.db");
            userDBHelper.openDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase sql = userDBHelper.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM user_tbl", null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                int score = cursor.getInt(2);
                int wrongAnsIdx1 = cursor.getInt(3);
                int wrongAnsIdx2 = cursor.getInt(4);
                int wrongAnsIdx3 = cursor.getInt(5);
                String wrongAns1 = wordList.get(wrongAnsIdx1)+": "+meaningList.get(wrongAnsIdx1)[0];
                String wrongAns2 = wordList.get(wrongAnsIdx2)+": "+meaningList.get(wrongAnsIdx2)[0];
                String wrongAns3 = wordList.get(wrongAnsIdx3)+": "+meaningList.get(wrongAnsIdx3)[0];
                wrongAns = wrongAns1+"\n"+wrongAns2+"\n"+wrongAns3;
                adapter.addItem(score, name, wrongAns) ;
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void fetchWordData() {
        try {
            wordDBHelper.createDatabase("dic1800.db");
            wordDBHelper.openDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase sql = wordDBHelper.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM dict_tbl", null);
        if (cursor.moveToFirst()) {
            do {
                String word = cursor.getString(1);
                String meaningTmp = cursor.getString(2);
                String[] meaning = meaningTmp.split(",");
                wordList.add(word);
                meaningList.add(meaning);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}