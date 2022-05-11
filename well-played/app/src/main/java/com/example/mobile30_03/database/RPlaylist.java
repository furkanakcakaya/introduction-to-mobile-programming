package com.example.mobile30_03.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mobile30_03.R;

@Entity
public class RPlaylist  {
    @PrimaryKey(autoGenerate = true)
    public int playlistId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "artwork_url")
    public int artworkUrl;

    @ColumnInfo(name = "username")
    public String username;

    //constructor
    public RPlaylist(String name, String description) {
        this.name = name;
        this.description = description;
        this.artworkUrl = R.drawable.playlist_clip;
        this.username = "Admin";  //deleting the field was a pain in Room Library, so I just set it to "Admin"
    }
}
