package com.example.musicplayer;

//Music 类型文件，用于集合musics
public class Music {
    //歌曲名
    private String name;
    //存放路径
    private String path;
    //艺术家名字
    private String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Music{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
