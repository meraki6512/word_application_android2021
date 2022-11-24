package com.example.choisquidgame;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resId;
    private ArrayList<WordItem> data;

    String DB_NAME = "dic1800.db";

    public WordAdapter(Context context, int resID, ArrayList<WordItem> data) {
        super(context, resID);
        this.context = context;
        this.resId = resID;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WordHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);

            holder = new WordHolder(convertView);
            convertView.setTag(holder);
        }

        holder = (WordHolder) convertView.getTag();

        TextView question = holder.question;
        WordItem word = data.get(position);
        question.setText(word.question);

        DBHelper dbHelper = new DBHelper(context.getApplicationContext(), DB_NAME);
        final CheckBox checkBox = convertView.findViewById(R.id.checkbox_all);

        if (word.isMyWorld==1){
            checkBox.setChecked(true);
        }
        else if (word.isMyWorld==0){
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dbHelper.updateWord(1, position);
                    Log.d("오지웅게임팀", "position: "+position);
                }
                else{
                    dbHelper.updateWord(0, position);
                }
            }
        });

        return convertView;
    }

}