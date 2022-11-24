package com.example.choisquidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.Switch;

public class FlashcardSettingActivity extends AppCompatActivity {

    //Database
    DBHelper wordDBHelper;
    String DB_NAME = "dic1800.db";

    private boolean isAddWrong;
    public boolean isAddWrong() {
        return isAddWrong;
    }

    String TAG = "오지웅게임팀_데모영상";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_setting);

        Log.d(TAG, "Flashcard Setting Activity > onCreate");

        Switch addWrongSwitch = findViewById(R.id.addWrongSwitch);
        addWrongSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAddWrong = isChecked;
            }
        });

        wordDBHelper = new DBHelper(this, DB_NAME);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}