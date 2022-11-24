package com.example.choisquidgame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.Random;

public class FlashcardGameBGMService extends Service {

    MediaPlayer mediaPlayer;
    int r;

    public FlashcardGameBGMService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Random random = new Random();
        r = random.nextInt(4);
        switch (r){
            case 0:
                mediaPlayer = MediaPlayer.create(this, R.raw.music_1);
                break;
            case 1:
                mediaPlayer = MediaPlayer.create(this, R.raw.music_2);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this, R.raw.music_3);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this, R.raw.music_4);
                break;
        }
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }
}