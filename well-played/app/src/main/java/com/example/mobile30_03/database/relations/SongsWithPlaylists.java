package com.example.mobile30_03.database.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.mobile30_03.database.RPlaylist;
import com.example.mobile30_03.database.RSong;

import java.util.List;

public class SongsWithPlaylists {
    @Embedded
    public RSong rSongs;

    @Relation(parentColumn = "songId", entityColumn = "playlistId", associateBy = @Junction(SongPlaylistCrossRef.class))
    public List<RPlaylist> rPlaylists;
}
