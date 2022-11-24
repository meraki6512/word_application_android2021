package com.example.choisquidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class FlashcardGameOverActivity extends AppCompatActivity {

    long initTime;

    private String userName;
    private String userNameTmp;
    private int isAddWrong;
    private String scoreString;
    private int score;
    private int[] wrongWordIdx;

    private ArrayList<String> wordList;
    private ArrayList<String[]> meaningList;

    //Database
    DBHelper wordDBHelper;
    DBHelper userDBHelper;
    String DB_NAME= "dic1800.db";
    boolean isWholeWord;

    Dialog dialog;

    String TAG = "오지웅게임팀_데모영상";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Log.d(TAG, "Flashcard GameOver Activity > onCreate");

        Intent rcvIntent = getIntent();
        isWholeWord = rcvIntent.getBooleanExtra("isWholeWord", true);
        wrongWordIdx = rcvIntent.getIntArrayExtra("wrongWordIdx");
        userNameTmp = rcvIntent.getStringExtra("userName");
        score = rcvIntent.getIntExtra("score", 0);
        scoreString = Integer.toString(score);
        Button flashcard_game_over_score = findViewById(R.id.flashCardGameOverScore);
        String tempString = "Score: " + scoreString + "\n (check your rank!)";
        if (scoreString!=null) flashcard_game_over_score.setText(tempString);

        wordDBHelper = new DBHelper(this, DB_NAME);
        wordList = new ArrayList<>();
        meaningList = new ArrayList<>();
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
                String word = cursor.getString(1);
                String meaningTmp = cursor.getString(2);
                String[] meaning = meaningTmp.split(",");
                //wordList.add(word);
                //meaningList.add(meaning);
                int isMyWord = cursor.getInt(3); //onChecked:가져오고 아니면 안가져와
                if (isWholeWord){
                    wordList.add(word);
                    meaningList.add(meaning);
                }
                else{
                    if (isMyWord==1){
                        wordList.add(word);
                        meaningList.add(meaning);
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private boolean addUser(){
        FlashcardSettingActivity settingActivity = new FlashcardSettingActivity();
        if (settingActivity.isAddWrong()) {
            isAddWrong = 1;
        }
        else{
            isAddWrong = 0;
        }
        userDBHelper = new DBHelper(this, "user.db");
        return userDBHelper.addUser(userName, score, wrongWordIdx, isAddWrong);
    }

    void showDialog(){
        String[] wrongWord = new String[3];
        wrongWord[0] = wordList.get(wrongWordIdx[0]);
        wrongWord[1] = wordList.get(wrongWordIdx[1]);
        wrongWord[2] = wordList.get(wrongWordIdx[2]);
        String[] wrongWordMeaning = {"", "", ""};
        for (int i=0; i< meaningList.get(wrongWordIdx[0]).length; i++){
            wrongWordMeaning[0] += meaningList.get(wrongWordIdx[0])[i] + "\n";
        }
        for (int i=0; i< meaningList.get(wrongWordIdx[1]).length; i++){
            wrongWordMeaning[1] += meaningList.get(wrongWordIdx[1])[i] + "\n";
        }
        for (int i=0; i< meaningList.get(wrongWordIdx[2]).length; i++){
            wrongWordMeaning[2] += meaningList.get(wrongWordIdx[2])[i] + "\n";
        }
        String message = "\n1. " + wrongWord[0] + "\n " + wrongWordMeaning[0] + "\n"
                + "2. " + wrongWord[1] + "\n " + wrongWordMeaning[1] + "\n"
                + "3. " + wrongWord[2] + "\n " + wrongWordMeaning[2] + "\n";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Check Your Wrong Answers!")
                .setMessage(message)
                .setIcon(R.drawable.wrong)
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

    public void onClick(View view) {
        if (view.getId()==R.id.userNameBtn){
            EditText editText = findViewById(R.id.userName);
            userName = editText.getText().toString();
            if (!userName.equals("")) {
                if (addUser()) {
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                    editText.setText(null);
                    editText.setEnabled(false);
                }
            }
            else {
                userName = userNameTmp;
                if (addUser()) {
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                    editText.setEnabled(false);
                }
            }
        }
        else if (view.getId()==R.id.flashCardGameOverScore){
            startActivity(new Intent(this, FlashcardRankActivity.class));
        }
        else if (view.getId()==R.id.flashCardRetryButton){
            startActivity(new Intent(this, FlashcardMainActivity.class));
            finish();
        }
        else if (view.getId()==R.id.flashCardMainMenuButton){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else if (view.getId()==R.id.flashCardWrongAnsButton){
            showDialog();
        }
        else if (view.getId()==R.id.flashCardShareButton){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "[Flash Card Game]\nMy Score is "+scoreString+"!");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  //액션바나 메뉴로 구현으로 변경해도 됨.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - initTime > 3000) {
                Toast.makeText(this, "Press once more to exit.", Toast.LENGTH_SHORT).show();
                initTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}