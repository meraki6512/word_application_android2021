package com.example.choisquidgame;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WordListMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private EditText searchView;
    private Button searchBtn;
    private String search;
    private int searchIdx;

    //DB
    String DB_NAME = "dic1800.db";
    ArrayList<WordItem> data;

    Dialog dialog;

    String TAG = "오지웅게임팀_데모영상";
    private boolean isWholeWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_main);

        Intent rcvIntent = getIntent();
        isWholeWord = rcvIntent.getBooleanExtra("isWholeWord", true);
        Log.d(TAG, "isWholeWord: " + isWholeWord);

        ListView listView = findViewById(R.id.all_word_list);
        listView.setOnItemClickListener(this);

        SQLiteOpenHelper dbHelper = new DBHelper(this, DB_NAME);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(TAG, "DB_PATH = " + db.getPath());
        Cursor cursor = db.rawQuery("select * from dict_tbl", null);

        data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                WordItem word = new WordItem();
                word._id = cursor.getInt(0);
                word.question = cursor.getString(1);
                word.answer = cursor.getString(2);
                word.isMyWorld = cursor.getInt(3);
                if (isWholeWord){
                    data.add(word);
                }
                else{
                    if (word.isMyWorld==1){
                        data.add(word);
                    }
                }
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        WordAdapter adapter = new WordAdapter(this, R.layout.word_list_item, data);
        listView.setAdapter(adapter);

        searchView = findViewById(R.id.search_word_all);
        searchBtn = findViewById(R.id.search_word_btn_all);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = searchView.getText().toString();
                if (search.equals("")){
                    Toast.makeText(WordListMainActivity.this, "Input Word.", Toast.LENGTH_SHORT).show();
                }
                else {
                    searchIdx = searchData(search);
                    if (searchIdx < 0){
                        Toast.makeText(WordListMainActivity.this, "No Such Word.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        showDialog();
                    }
                    searchView.setText("");
                }
            }
        });
    }


    void showDialog(){
        String word = data.get(searchIdx).question;
        String[] meaningTmp = data.get(searchIdx).answer.split(",");
        String meaning = "";
        for (int i=0; i< meaningTmp.length; i++){
            meaning += (i+1) + ". " + meaningTmp[i] + "\n";
        }
        String message = "\n" + word + "\n" + meaning;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search Result")
                .setMessage(message)
                .setIcon(R.drawable.search)
                .setCancelable(false)
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }



    int searchData (String search){
        for(int i=0; i<data.size(); i++){
            if (search.equals(data.get(i).question)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        String question = textView.getText().toString();

        Intent intent = new Intent(this, ViewWordPage.class);
        intent.putExtra("question", question);
        startActivityForResult(intent, 10);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  //액션바나 메뉴로 구현으로 변경해도 됨.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}