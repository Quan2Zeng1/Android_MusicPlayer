package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

//集合：显示音乐文件的名称、路径，显示在 ListView 中
public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<Music> musics;

    public MusicAdapter(Context context, List<Music> musics) {
        this.context = context;
        this.musics = musics;
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当个文件的位置
        Music music=musics.get(position);
        //将文件布置在模板中
        if (convertView==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.music_item,null);
        }
        //初始化控件
        TextView tvName= (TextView) convertView.findViewById(R.id.tv_music_title);
        TextView tvArtist= (TextView) convertView.findViewById(R.id.tv_music_path);
        //TextView tvPath= (TextView) convertView.findViewById(R.id.tv_music_path);
        //显示文件内容
        //显示歌曲名
        tvName.setText(music.getName());
        //tvPath.setText(music.getPath());
        //显示艺术家名字
        tvArtist.setText(music.getArtist());
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
