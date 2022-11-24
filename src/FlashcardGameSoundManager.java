package com.example.choisquidgame;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class FlashcardGameSoundManager {

    private final SoundPool mSoundPool;
    private final HashMap<Integer,Integer> mSoundPoolMap;
    private final AudioManager mAudioManager;
    private final Context mContext;

    public FlashcardGameSoundManager(Context mContext, SoundPool mSoundPool){
        this.mContext = mContext;
        this.mSoundPool = mSoundPool;
        mSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public void addSound(int index,int soundId){ //효과음 추가
        mSoundPoolMap.put(index,mSoundPool.load(mContext,soundId,1));
    }

    public int playSound(int index){ //효과음 재생
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return mSoundPool.play(mSoundPoolMap.get(index),streamVolume,streamVolume,1,0,1f);
    }

    public void stopSound(int streamId){ //효과음 정지
        mSoundPool.stop(streamId);
    }

    public void pauseSound(int streamId){ //효과음 일시정지
        mSoundPool.pause(streamId);
    }

    public void resumeSound(int streamId){ //효과음 재시작
        mSoundPool.resume(streamId);
    }
}

