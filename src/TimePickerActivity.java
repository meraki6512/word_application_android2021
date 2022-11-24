package com.example.choisquidgame;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class TimePickerActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button okBtn, cancelBtn;
    private int hour, minute;
    private String am_pm;
    private Date currentTime;
    private String stMonth, stDay;
    private final int onKeyDown = 999;
    String TAG = "오지웅게임팀_데모영상";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        Log.d(TAG, "TimePicker Activity > onCreate");

        timePicker = (TimePicker)findViewById(R.id.time_picker);

        currentTime = Calendar.getInstance().getTime();

        okBtn = (Button)findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }
                else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                am_pm = AM_PM(hour);
                hour = timeSet(hour);

                Intent sendIntent = new Intent(TimePickerActivity.this, AlarmSetting.class);

                sendIntent.putExtra("hour", hour);
                sendIntent.putExtra("minute", minute);
                sendIntent.putExtra("am_pm", am_pm);
                setResult(RESULT_OK, sendIntent);
                String msg = am_pm+" " + hour + "시 " + minute + "분";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
    private int timeSet(int hour) {
        if(hour > 12) {
            hour-=12;
        }
        if(hour == 0){
            hour = 12;
        }
        return hour;
    }
    private String AM_PM(int hour) {
        if(hour >= 12) {
            am_pm = "오후";
        }
        else {
            am_pm = "오전";
        }
        return am_pm;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent sendIntent = new Intent(this, AlarmSetting.class);
            setResult(onKeyDown, sendIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}