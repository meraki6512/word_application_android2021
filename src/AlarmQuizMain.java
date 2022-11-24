package com.example.choisquidgame;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AlarmQuizMain extends AppCompatActivity {

    private static final String DB_NAME = "dic1800.db";
    private ArrayList<String> wordList;
    private ArrayList<String> meaningList;
    private ArrayList<String> subList;
    private TextView questionText;
    public int index;
    private int score = 0;
    TextView score_view;
    ArrayAdapter<String> adapter_ver;
    ArrayList<String> quiz = new ArrayList<>();
    ListView listView;
    MediaPlayer mediaPlayer;
    String test1, test2, word;
    String TAG = "오지웅게임팀_데모영상";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = (TextView) findViewById(R.id.word);
        score_view = (TextView) findViewById(R.id.count_num);
        listView = (ListView) findViewById(R.id.listView_ver);
        questionText = (TextView) findViewById(R.id.word);

        Log.d(TAG, "AlarmQuizMain > onCreate");

        mediaPlayer = MediaPlayer.create(this, R.raw.wakeup);
        mediaPlayer.setLooping(true);
        int maxVolume = 50;
        int currVolume = 10;
        float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
        mediaPlayer.setVolume(log1,log1);
        mediaPlayer.start();


        QuizAdapterActivity mDbHelper = new QuizAdapterActivity(this);
        mDbHelper.createDatabase();
        mDbHelper.open();
        wordList = mDbHelper.getWordList(this);
        meaningList = mDbHelper.getMeaningList(this);
        quiz = mDbHelper.getMeaningList(this);
        mDbHelper.close();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        onWindowFocusChanged(true);
        initListView();

    }

    public void onBackPressed() {
        //super.onBackPressed();
        Log.d(TAG, "AlarmQuizMain > onBackPressd: cannot use back key.");
        //Toast.makeText(this, "back키 사용불가.", Toast.LENGTH_SHORT).show();
    }

    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
        Log.d(TAG, "AlarmQuizMain > onPause: cannot use back key.");
        //Toast.makeText(this, "메뉴키 사용불가.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        Intent intent = getIntent();
        startActivity(intent);
    }

    public void shakeQUIZ() {

        long seed = System.currentTimeMillis();
        Random rand = new Random(seed);
        index = rand.nextInt(1800);

        word = wordList.get(index);
        questionText.setText(word);

        Log.d(TAG, "AlarmQuizMain > makeQuiz() : 지워질 단어 " + wordList.get(index) + " 뜻 " + meaningList.get(index));
        quiz.remove(index);
        Collections.shuffle(quiz);


        subList = new ArrayList<>(quiz.subList(0, 4));

        subList.add(meaningList.get(index));
        Collections.shuffle(subList);//답을 넣고 다시 섞는다

        Log.d(TAG, "AlarmaQuizMain > makeQuiz() : 정상적으로작동해야할 단어 " + wordList.get(index) + " 뜻 " + meaningList.get(index));

        adapter_ver = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, subList);
        listView.setAdapter(adapter_ver);
    }


    public void initListView() {

        shakeQUIZ();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();//listview에서 선택한 위치는 parent.getitemposition
                if (meaningList.get(index).equals(str)) {
                    Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                    score++;
                    score_view.setText("" + score);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong.\nAnswer: " + meaningList.get(index), Toast.LENGTH_SHORT).show();
                }
                if (score == 5) {
                    finish();
                }
                shakeQUIZ();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

