package com.example.choisquidgame;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerActivity extends AppCompatActivity {

    private int mYear =0, mMonth=0, mDay=0;
    String TAG = "오지웅게임팀_데모영상";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_date_picker);
        Log.d(TAG, "DatePicker Activity > onCreate");
        Calendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePicker datePicker = findViewById(R.id.vDatePicker);
        datePicker.init(mYear, mMonth, mDay,mOnDateChangedListener);
    }

    public void mOnClick(View v){
        Intent intent = new Intent();
        mMonth = mMonth+1;
        intent.putExtra("mYear",mYear);
        intent.putExtra("mMonth", mMonth);
        intent.putExtra("mDay", mDay);
        setResult(RESULT_OK, intent);
        String msg = mYear + "년 " + mMonth + "월 " + mDay + "일";
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        finish();
    }
    DatePicker.OnDateChangedListener mOnDateChangedListener = new DatePicker.OnDateChangedListener(){
        @Override
        public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {
            mYear = yy;
            mMonth = mm;
            mDay = dd;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}