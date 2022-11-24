package com.example.choisquidgame;

import android.view.View;
import android.widget.TextView;

public class WordHolder {

    public TextView question;

    public WordHolder(View root){
        question = root.findViewById(R.id.item_question);
    }

}
