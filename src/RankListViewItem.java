package com.example.choisquidgame;

import android.widget.ImageView;

public class RankListViewItem {

    //private ImageView isRankIn;
    private int score ;
    //private int rank ;
    private String name ;
    private String wrongAns;


    public String getWrongAns() {
        return wrongAns;
    }

    public void setWrongAns(String wrongAns) {
        this.wrongAns = wrongAns;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
