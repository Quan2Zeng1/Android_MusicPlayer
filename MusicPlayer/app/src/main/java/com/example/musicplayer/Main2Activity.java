package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//显示当前播放的音乐
public class Main2Activity extends AppCompatActivity {
    private TextView tvCurrentMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置当前布局为activity_main2
        setContentView(R.layout.activity_main2);
        //声明TextView控件
        tvCurrentMusic = (TextView) findViewById(R.id.tv_current_music);
        Intent intent = this.getIntent();
        String stringValue = intent.getStringExtra("currentMusic");
        tvCurrentMusic.setText(stringValue);
    }
}
