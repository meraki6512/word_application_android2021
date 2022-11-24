package com.example.choisquidgame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

//public class FlashcardGameActivity5 extends AppCompatActivity {
public class FlashcardGameActivity extends MainActivity {

    private ImageView heart_image_1;
    private ImageView heart_image_2;
    private ImageView heart_image_3;
    private TextView flashcard_text_view;
    private TextView flashcard_score;

    private int score = 0;
    private int wrongCount = 0;
    private int[] wrongWordIdx;
    private ArrayList<String> wordList;
    private ArrayList<String[]> meaningList;
    private int currentId = 0;

    private boolean isWholeWord;
    private boolean isAddWrong;

    //String wrongAns;
    String wrongAns1;
    String wrongAns2;
    String wrongAns3;

    long initTime;

    //Database
    DBHelper wordDBHelper;
    String DB_NAME = "dic1800.db";
    private int myWordCount;

    //Sound
    private SoundPool soundPool;
    private FlashcardGameSoundManager soundManager;
    private int playSoundId;

    String userName;

    String TAG = "오지웅게임팀_데모영상";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_game);

        startService(new Intent(getApplicationContext(), FlashcardGameBGMService.class));

        Log.d(TAG, "Flashcard Game Activity > onCreate");

        flashcard_text_view = findViewById(R.id.flashCardTextView);
        flashcard_score = findViewById(R.id.flashCardScore);
        heart_image_1 = findViewById(R.id.heartImage1);
        heart_image_2 = findViewById(R.id.heartImage2);
        heart_image_3 = findViewById(R.id.heartImage3);

        Intent rcvIntent = getIntent();
        isWholeWord = rcvIntent.getBooleanExtra("isWholeWord", true);
        myWordCount = rcvIntent.getIntExtra("myWordCount", 0);
        userName = rcvIntent.getStringExtra("userName");
        Log.d("myMessage", "isWholeWord: "+isWholeWord);
        Log.d("myMessage", "myWordCount: "+myWordCount);
        Log.d("myMessage", "userName: "+userName);

        wordDBHelper = new DBHelper(this, DB_NAME);
        wordList = new ArrayList<>();
        meaningList = new ArrayList<>();
        wrongWordIdx = new int[3];
        fetchData();

        FlashcardSettingActivity settingActivity = new FlashcardSettingActivity();
        isAddWrong = settingActivity.isAddWrong();
        Log.d("myMessage", "isAddWrong: " + isAddWrong);


        soundPool = new SoundPool.Builder().build();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().build();
        } else{ //롤리팝 이하 버전일 경우
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC,0); // new SoundPool(음악 파일 갯수,스트림 타입,음질)
        }*/
        soundManager = new FlashcardGameSoundManager(this,soundPool);
        soundManager.addSound(0,R.raw.sound_effect_correct);
        soundManager.addSound(1,R.raw.sound_effect_wrong_2);
        updateQuestion();
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

    @Override
    protected void onDestroy() {
        stopService(new Intent(getApplicationContext(), FlashcardGameBGMService.class));
        wordDBHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        //event
        //System.exit(0);
        stopService(new Intent(getApplicationContext(), FlashcardGameBGMService.class));
    }

    public void onClick(View view){
        if (view.getId()==R.id.flashCardNextButton){
            checkAnswer();
            //soundManager.pauseSound(playSoundId);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  //액션바나 메뉴로 구현으로 변경해도 됨.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - initTime > 3000) {
                Toast.makeText(this, "한 번 더 누르면 게임이 종료됩니다.", Toast.LENGTH_SHORT).show();
                initTime = System.currentTimeMillis();
            } else {
                stopService(new Intent(getApplicationContext(), FlashcardGameBGMService.class));
                startActivity(new Intent(this, FlashcardMainActivity.class));
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void updateQuestion() {
        Random rand = new Random();
        if (isWholeWord) {
            currentId = rand.nextInt(1800);
        } else{
            currentId = rand.nextInt(myWordCount);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flashcard_text_view.setText(String.join("\n", meaningList.get(currentId)));
        }
    }

    private void checkAnswer() {
        //get editText
        EditText editText = findViewById(R.id.flashCardAnswer);
        String userAnswer = editText.getText().toString();
        editText.setText(null);

        //check isCorrect
        if (userAnswer.equals("") ||!userAnswer.equals(wordList.get(currentId))) {
            Thread wrongSoundThread = new Thread("WrongSoundThread"){
                public void run(){
                    playSoundId = soundManager.playSound(1);
                }
            };
            wrongSoundThread.start();
            Toast.makeText(this, userAnswer + ": incorrect.\nanswer: "+wordList.get(currentId), Toast.LENGTH_SHORT).show(); //일단 임시로 토스트.
            wrongWordIdx[wrongCount++]=currentId;
            if (wrongCount==1){
                wrongAns1 = wordList.get(currentId)+": "+meaningList.get(currentId)[0];
                heart_image_3.setImageResource(R.drawable.heart_empty);
            }else if (wrongCount==2){
                wrongAns2 = wordList.get(currentId)+": "+meaningList.get(currentId)[0];
                heart_image_2.setImageResource(R.drawable.heart_empty);
            }else if (wrongCount==3){
                wrongAns3 = wordList.get(currentId)+": "+meaningList.get(currentId)[0];
                heart_image_1.setImageResource(R.drawable.heart_empty);
            }
            if (isAddWrong){
                wordDBHelper.updateWord(1, currentId);
            }
        }
        else if(userAnswer.equals(wordList.get(currentId))){
            Thread correctSoundThread = new Thread("CorrectSoundThread"){
                public void run(){
                    playSoundId = soundManager.playSound(0);
                }
            };
            correctSoundThread.start();
            Toast.makeText(this, userAnswer + ": correct!", Toast.LENGTH_SHORT).show(); //일단 임시로 토스트.
            score++;
            String scoreString = Integer.toString(score);
            flashcard_score.setText(scoreString);
        }

        //check isGameOver
        if (isGameOver()){
            stopService(new Intent(getApplicationContext(), FlashcardGameBGMService.class));
            Intent intent = new Intent(this, FlashcardGameOverActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("wrongWordIdx", wrongWordIdx);
            intent.putExtra("isWholeWord", isWholeWord);
            intent.putExtra("userName", userName);
            startActivity(intent);
            finish();
        }
        else{
            updateQuestion();
        }
    }

    private boolean isGameOver(){
        return wrongCount >= 3;
    }
}
