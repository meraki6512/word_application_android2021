package com.example.choisquidgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AlarmSetting extends AppCompatActivity {

    private int hour, minute, Year, month, day, enabled=1, vibrate=0;
    private int count;
    private int click_timebtn=0, click_datebtn=0;
    private String am_pm;
    private final int onKeyDown = 999;
    String TAG = "오지웅게임팀_데모영상";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);

        Log.d(TAG, "AlarmSetting Activity > onCreate");

        Button button1 = findViewById(R.id.time_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent time_intent = new Intent(getApplicationContext(), TimePickerActivity.class);
                startActivityForResult(time_intent,10000);
            }
        });


        Button dateBtn = findViewById(R.id.date_button);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent date_intent = new Intent(getApplicationContext(), DatePickerActivity.class);
                startActivityForResult(date_intent,10001);
            }
        });

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v->{
            Intent sendIntent = new Intent(AlarmSetting.this, AlarmMain.class);

            if(click_timebtn == 1&& click_datebtn==1){
                sendIntent.putExtra("hour", hour);
                sendIntent.putExtra("minute", minute);
                sendIntent.putExtra("am_pm", am_pm);
                sendIntent.putExtra("month", month);
                sendIntent.putExtra("day", day);
                sendIntent.putExtra("Year", Year);
                sendIntent.putExtra("click", 111);
            }else if(click_timebtn == 1&& click_datebtn==0){
                sendIntent.putExtra("hour", hour);
                sendIntent.putExtra("minute", minute);
                sendIntent.putExtra("am_pm", am_pm);
                sendIntent.putExtra("click", 110);
            }else if(click_timebtn == 0&& click_datebtn==1){
                sendIntent.putExtra("month", month);
                sendIntent.putExtra("day", day);
                sendIntent.putExtra("Year", Year);
                sendIntent.putExtra("click", 101);
            }
            setResult(RESULT_OK, sendIntent);
            finish();
        });

        Button removeBtn = findViewById(R.id.removeBtn);
        removeBtn.setOnClickListener(v->{
            Intent sendIntent = new Intent(AlarmSetting.this, AlarmMain.class);
            setResult(RESULT_CANCELED, sendIntent);
            finish();
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10000) {
            if (resultCode != Activity.RESULT_OK && data !=null) {
                return;
            }
            am_pm = data.getStringExtra("am_pm");
            hour = data.getIntExtra("hour", 0);
            minute = data.getIntExtra("minute", 0);
            click_timebtn = 1;
        }
        else if(requestCode == 10001){
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            month = data.getIntExtra("mMonth", 0);
            day = data.getIntExtra("mDay", 0);
            Year = data.getIntExtra("mYear", 0);
            click_datebtn = 1;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  //액션바나 메뉴로 구현으로 변경해도 됨.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent sendIntent = new Intent(AlarmSetting.this, AlarmMain.class);
            setResult(onKeyDown, sendIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
