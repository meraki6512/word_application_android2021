package com.example.choisquidgame;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmMain extends AppCompatActivity {

    public static final int REQUEST_CODE1 = 1000;
    public static final int REQUEST_CODE2 = 1001;
    private AlarmAdapterActivity arrayAdapter;
    private Button addBtn;
    private ListView listView;
    private int hour, minute;
    private int month, enabled=1;
    private int day;
    private String am_pm;
    private Handler handler;
    private SimpleDateFormat mFormat;
    public static AlarmManager alarmManager=null;
    public static PendingIntent alarmIntent=null;
    private Calendar calendar;
    private int adapterPosition;
    public static int count=0;
    public  int vibrate;
    private int recordCount = -1;
    public SQLiteDatabase db;
    public AlarmDBHelper helper;
    private Cursor outCursor;
    private final int onKeyDown = 999;
    String TAG = "오지웅게임팀_데모영상";


    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity_view);

        String TAG = "오지웅게임팀_데모영상";
        Log.d(TAG, "AlarmMain Activity > onCreate");

        arrayAdapter = new AlarmAdapterActivity();
        helper = new AlarmDBHelper(AlarmMain.this, "alarm.db", null,1);
        db = this.openOrCreateDatabase("alarm.db", MODE_PRIVATE, null);
        //db = this.openOrCreateDatabase("mytable", MODE_PRIVATE, null);
        db = helper.getWritableDatabase();
        helper.onCreate(db);
        String SQL = "select _id, hour, minutes, month, day, enabled, "
                + "vibrate, am_pm, alert "
                + "from mytable";

        outCursor = db.rawQuery(SQL, null);
        if(outCursor != null) {
            recordCount = outCursor.getCount();  // 레코드 개수를 리턴
            // 레코드 숫자 만큼 데이터를 가져 온다.
            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();     // 다음 레코드를 가리킨다.
                count = outCursor.getInt(0);
                hour = outCursor.getInt(1);
                minute = outCursor.getInt(2);
                month = outCursor.getInt(3);
                day = outCursor.getInt(4);
                enabled = outCursor.getInt(5);
                vibrate = outCursor.getInt(6);
                am_pm = outCursor.getString(7);
                arrayAdapter.addItem(hour, minute, am_pm, month, day);
                count++;
            }
            outCursor.close();
        }

        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterPosition = position;
                db = helper.getReadableDatabase();
                String SQL = "select * from mytable where _id= " + adapterPosition ;

                Cursor cursor = db.rawQuery(SQL, null);
                cursor.moveToFirst();
                int ID = cursor.getInt(0);
                hour = cursor.getInt(1);
                minute = cursor.getInt(2);
                month = cursor.getInt(3);
                day = cursor.getInt(4);
                enabled = cursor.getInt(5);
                am_pm = cursor.getString(7);
                cursor.close();
                Log.d("TEST", "onItemClick: " + ID+ " " + hour + "시 " +minute + "분 " + month + "월 " + day + "일 " + am_pm);
                Intent intent = new Intent(AlarmMain.this, AlarmSetting.class);
                //arrayAdapter.removeItem(position);
                startActivityForResult(intent, REQUEST_CODE2);
                db.close();
                }
            });
        handler = new Handler() {
            @SuppressLint({"HandlerLeak", "SimpleDateFormat"})
            @Override
            public void handleMessage(@NonNull Message msg) {
                Calendar cal = Calendar.getInstance();

                mFormat = new SimpleDateFormat("HH:mm:ss");
                String strTime = mFormat.format(cal.getTime());
            }
        };

        class NewRunnable implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        }

        NewRunnable runnable = new NewRunnable();
        Thread thread = new Thread(runnable);
        thread.start();

        //TimePicker의 시간 셋팅값을 받기 위한 startActivityForResult()
        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tpIntent = new Intent(getApplicationContext(), AlarmSetting.class);
                tpIntent.putExtra("count", count);
                startActivityForResult(tpIntent, REQUEST_CODE1);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.notification_channel_id); // 채널 아이디
            CharSequence channelName = getString(R.string.notification_channel_name); //채널 이름
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




    //TimePicker 셋팅값 받아온 결과를 arrayAdapter에 추가
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //시간 리스트 추가
        if (requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null) {
            Calendar calendar = new GregorianCalendar();
            hour = data.getIntExtra("hour", calendar.get(Calendar.HOUR_OF_DAY));
            minute = data.getIntExtra("minute", calendar.get(Calendar.MINUTE));
            am_pm = data.getStringExtra("am_pm");
            month = data.getIntExtra("month", calendar.get(Calendar.MONTH)+1);
            day = data.getIntExtra("day", calendar.get(Calendar.DATE));
            enabled = data.getIntExtra("enabled", enabled);

            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.DATE, day);
            calendar.set(Calendar.SECOND, 0);
            alarmManager=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                Intent intent = new Intent(this, AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(this, count, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("_id",count);
                values.put("hour",hour);
                values.put("minutes",minute);
                values.put("month",month);
                values.put("day",day);
                values.put("enabled",enabled);
                values.put("vibrate",vibrate);
                values.put("am_pm", am_pm);
                long result = db.insert("mytable",null,values);
                if (result == -1)
                {
                    Toast.makeText(this.getApplicationContext(), "insert data fail"+ count, Toast.LENGTH_SHORT).show();
                }
                db.close();
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);
                Toast.makeText(this,"Saved.",Toast.LENGTH_SHORT).show();
            }
            arrayAdapter.addItem(hour, minute, am_pm, month, day);
            arrayAdapter.notifyDataSetChanged();
            count++;
            db.close();
        }
        else if (requestCode == REQUEST_CODE2 && resultCode == RESULT_OK && data != null) {//시간 리스트 터치 시 변경된 시간값 저장
            arrayAdapter.removeItem(adapterPosition);
            Calendar calendar = new GregorianCalendar();
            if(data.getIntExtra("click",000)==111)
            {
                hour = data.getIntExtra("hour", calendar.get(Calendar.HOUR_OF_DAY));
                minute = data.getIntExtra("minute", calendar.get(Calendar.MINUTE));
                am_pm = data.getStringExtra("am_pm");
                month = data.getIntExtra("month", calendar.get(Calendar.MONTH)+1);
                day = data.getIntExtra("day", calendar.get(Calendar.DATE));
            }else if(data.getIntExtra("click",000)==110)
            {
                hour = data.getIntExtra("hour", calendar.get(Calendar.HOUR_OF_DAY));
                minute = data.getIntExtra("minute", calendar.get(Calendar.MINUTE));
                am_pm = data.getStringExtra("am_pm");
            }else if(data.getIntExtra("click",000)==101)
            {
                month = data.getIntExtra("month", calendar.get(Calendar.MONTH));
                day = data.getIntExtra("day", calendar.get(Calendar.DATE));
            }

            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("_id",adapterPosition);
            values.put("hour",hour);
            values.put("minutes",minute);
            values.put("month",month);
            values.put("day",day);
            values.put("enabled",enabled);
            values.put("vibrate",vibrate);
            values.put("am_pm", am_pm);
            long result = db.update("mytable", values, "_id = "+adapterPosition, null);
            if (result == -1)
            {
                Toast.makeText(this.getApplicationContext(), "update date fail", Toast.LENGTH_SHORT).show();
            }
            db.close();

            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.DATE, day);
            calendar.set(Calendar.SECOND, 0);
            alarmManager=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                Intent intent = new Intent(this, AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(this, adapterPosition, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);
                Toast.makeText(this,"Saved.",Toast.LENGTH_LONG).show();
                Log.d("myMessage", " 변경된 포지션 = " + adapterPosition + " ");
            }
            arrayAdapter.addItem(hour, minute, am_pm, month, day);
            arrayAdapter.notifyDataSetChanged();
            db.close();
        }
        else if (requestCode == REQUEST_CODE2 && resultCode == RESULT_CANCELED){
            arrayAdapter.removeItem(adapterPosition);
            db = helper.getWritableDatabase();
            db.execSQL("DELETE FROM mytable WHERE _id = '" + adapterPosition + "';");
            alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), adapterPosition, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(alarmIntent);
            alarmIntent.cancel();
            alarmManager = null;
            alarmIntent = null;

            arrayAdapter.notifyDataSetChanged();
            Toast.makeText(this," 삭제되었습니다", Toast.LENGTH_SHORT).show();

            String SQL = "select * from mytable";
            Cursor cursor = db.rawQuery(SQL, null);
            count = cursor.getCount();
            db.execSQL("DELETE FROM mytable WHERE _id = " + adapterPosition);

            for (int i=adapterPosition+1; i<=count; i++) {
                ContentValues cv = new ContentValues();
                cv.put("_id", i-1);
                db.update("mytable", cv, "_id=" + i, null);
            }
            db.close();
        }
    }
}
