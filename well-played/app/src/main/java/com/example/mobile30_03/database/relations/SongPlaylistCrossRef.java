package com.example.mobile30_03.database.relations;

import androidx.room.Entity;

@Entity(primaryKeys = {"songId", "playlistId"})
public class SongPlaylistCrossRef {
    public int songId;
    public int playlistId;

    public SongPlaylistCrossRef(int songId, int playlistId) {
        this.songId = songId;
        this.playlistId = playlistId;
    }
}