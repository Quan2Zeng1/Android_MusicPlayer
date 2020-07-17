package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //声明各个组件,集合
    private List<Music> musics;
    private ListView lvMusicList;
    private ImageButton ibtnPlayOrPause;
    private ImageButton ibtnNext;
    private ImageButton ibtnprevious;
    private ImageButton ibtnGetPosition;
    private RadioButton rbtnPlayRandown;
    private RadioButton rbtnPlaySequence;
    private RadioButton rbtnPlaySingle;
    private MusicAdapter adapter;
    private TextView tvTip;
    private Context context;

    //声明 MediaPlayer
    private MediaPlayer player;
    //暂停时的播放位置
    private int pausePosition;
    //当前播放的歌曲的索引
    private int currentMusicIndex;

    @Override
    //主函数
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        initView();
        //获取歌曲列表
        loadData();
        //列出列表
        initListView();
        //配置监听器
        setListener();
        //重置
        player.reset();
    }

    //初始化控件及布局文件等
    private void initView() {
        player = new MediaPlayer();
        lvMusicList = (ListView) findViewById(R.id.lv_musiclist);
        ibtnPlayOrPause = (ImageButton) findViewById(R.id.ibtn_playorpause);
        ibtnNext = (ImageButton) findViewById(R.id.ibtn_next);
        ibtnprevious = (ImageButton) findViewById(R.id.ibtn_previous);
        ibtnGetPosition = (ImageButton) findViewById(R.id.ibtn_getposition);
        rbtnPlayRandown = (RadioButton) findViewById(R.id.rbtn_playrandowm);
        rbtnPlaySequence = (RadioButton) findViewById(R.id.rbtn_playsequence);
        rbtnPlaySingle = (RadioButton) findViewById(R.id.rbtn_playsingle);
        tvTip = (TextView) findViewById(R.id.tv_musictip);
        context = MainActivity.this;
    }

    //获取歌曲数据
    private void loadData() {
        //获取音乐文件的内容
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        // 获取歌曲列表
        musics = new ArrayList<>();
        // 1. 检查sdcard是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 2. 获取sdcard下Music文件夹的File对象
            File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            // 3. 检查Music文件夹是否存在
            if (musicDir.exists()) {
                // 4. 通过File类的listFiles()方法，获取Music文件夹下所有子级File对象
                File[] files = musicDir.listFiles();
                // 5. 检查获取到的File列表是否有效（数组是否为null，或数组长度是否为0）
                if (files != null && files.length > 0) {
                    // 6. 遍历File列表
                    for (int i = 0; i < files.length; i++) {
                        //下一个文件
                        cursor.moveToNext();
                        //获取艺术家内容
                        String artist = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        // 6.1. 通过File类的isFile()方法，检查是否是文件
                        if (files[i].isFile()) {
                            // 6.2. 通过File类的getName()方法获取文件名，
                            // 结合String类的endsWith()方法，检查是否是mp3文件
                            String fileName = files[i].getName();
                            if (fileName.toUpperCase(Locale.CHINA).endsWith(".MP3")) {


                                // 6.3. 创建Music对象，并封装必要的属性
                                Music music = new Music();
                                music.setName(fileName.substring(0, fileName.length() - 4));
                                music.setArtist(artist);
                                music.setPath(files[i].getAbsolutePath());
                                // 6.4. 将新创建的Music对象添加到集合中
                                musics.add(music);

                            }

                        }

                    }
                }
            }
        }
    }

    //初始化ListView，即列出列表
    private void initListView() {
        adapter = new MusicAdapter(getApplicationContext(), musics);
        lvMusicList.setAdapter(adapter);
    }

    //播放音乐
    private void play() {
        //重置
        player.reset();

        try {
            //设置音乐文件来源
            player.setDataSource(musics.get(currentMusicIndex).getPath());
            //准备（缓冲文件）
            player.prepare();
            //将进度设置到“音乐进度”
            player.seekTo(pausePosition);
            //获取音乐进度
            player.getCurrentPosition();
            //播放开始
            player.start();
            //用当前界面的 TextView显示当前播放的音乐
            tvTip.setText(musics.get(currentMusicIndex).getName() + "");
            //设置按钮图片为暂停图标
            ibtnPlayOrPause.setImageResource(android.R.drawable.ic_media_pause);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //暂停
    private void pause() {
        //直接调用MediaPlay 中的暂停方法
        player.pause();
        //获取暂停的位置（音乐进度）
        pausePosition = player.getCurrentPosition();
        //切换为播放的按钮（按钮为android系统自带的按钮，可直接用）
        ibtnPlayOrPause.setImageResource(android.R.drawable.ic_media_play);
    }

    //单曲循环
    private void single() {
        currentMusicIndex++;
        currentMusicIndex--;
        pausePosition = 0;
        play();
    }

    //随机播放方法
    private void randown() {
        currentMusicIndex = new Random().nextInt(musics.size());
        Toast.makeText(MainActivity.this, "随机", Toast.LENGTH_SHORT).show();
        pausePosition = 0;
        play();
    }

    //列表循环
    private void sequence() {
        currentMusicIndex++;
        if (currentMusicIndex >= musics.size()) {
            currentMusicIndex = 0;
        }
        pausePosition = 0;
        play();
    }

    //获取当前播放的音乐文件的名字，必先是在另一个布局中；
    private void getPosition() {
        //声明一个意图,用来切换布局和传递数据
        Intent intent = new Intent();
        //定义要传递的数据
        String extra = musics.get(currentMusicIndex).getName() + "";
        //传递数据
        intent.putExtra("currentMusic", extra);
        //切换布局
        intent.setClass(MainActivity.this, Main2Activity.class);
        //执行此系列活动
        MainActivity.this.startActivity(intent);
    }

    //播放上一曲
    private void previous() {
        //判断是否为第一首歌曲，若为第一首歌曲，则播放最后一首
        if (rbtnPlaySingle.isChecked()) {
            single();
        } else {
            //当前音乐播放位置--（上一曲）
            currentMusicIndex--;
            if (currentMusicIndex <= 0) {
                Toast.makeText(MainActivity.this, "已经是第一首了", Toast.LENGTH_SHORT).show();
                return;
            } else {

                //音乐进度置为0
                pausePosition = 0;
                //播放
                play();
            }
        }
    }

    //播放下一曲（与上一曲类似）
    private void next() {
        if (rbtnPlayRandown.isChecked()) {
            randown();
        } else if (rbtnPlaySingle.isChecked()) {
            single();
        } else if (rbtnPlaySequence.isChecked()) {
            sequence();
        } else {
            currentMusicIndex++;
            if (currentMusicIndex >= musics.size()) {
                Toast.makeText(MainActivity.this, "已经是最后一首了", Toast.LENGTH_SHORT).show();
                return;
            } else {
                pausePosition = 0;
                play();
            }
        }
    }

    //播放ListView 点击的音乐
    private class InnerItemOnCLickListener implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取点击的列表的中音乐的位置，赋值给当前播放音乐
            currentMusicIndex = position;
            //令暂停的进度为0（即为从头播放）
            pausePosition = 0;
            //播放
            play();
        }
    }

    //主要按钮的播放监听器
    private class InnerOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 播放/暂停按钮即执行的方法
                case R.id.ibtn_playorpause:
                    //当音乐在播放时
                    if (player.isPlaying()) {
                        //暂停
                        pause();
                    } else {
                        //不在播放，则播放
                        play();
                    }
                    break;
                //下一首按钮即执行方法
                case R.id.ibtn_next:
                    next();
                    break;
                //上一首按钮即执行方法
                case R.id.ibtn_previous:
                    previous();
                    break;
                //获取当前播放音乐即执行方法
                case R.id.ibtn_getposition:
                    getPosition();
                    break;


            }

        }
    }

    private final class InnerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    //设置各种监听器
    private void setListener() {
        InnerOnclickListener listener = new InnerOnclickListener();
        //暂停播放
        ibtnPlayOrPause.setOnClickListener(listener);
        //下一首
        ibtnNext.setOnClickListener(listener);
        //上一首
        ibtnprevious.setOnClickListener(listener);
        //获取当前播放音乐
        ibtnGetPosition.setOnClickListener(listener);

        InnerItemOnCLickListener listener2 = new InnerItemOnCLickListener();
        //给 ListView 添加点击（点击音乐即可播放）
        lvMusicList.setOnItemClickListener(listener2);
        // MediaPlay 自动播放（因为就一条，无需加变量）
        player.setOnCompletionListener(new InnerOnCompletionListener());
    }

}
