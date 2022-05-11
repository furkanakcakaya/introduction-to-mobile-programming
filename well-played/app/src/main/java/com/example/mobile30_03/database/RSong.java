package com.example.mobile30_03.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "uri", unique = true)})
public class RSong {
    @PrimaryKey(autoGenerate = true)
    public int songId;

    @ColumnInfo(name = "uri")
    public String uri;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "artist")
    public String artist;

    @ColumnInfo(name = "duration")
    public int duration;

    //Constructor
    public RSong(String uri, String name, String artist, int duration) {
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
    }

}
