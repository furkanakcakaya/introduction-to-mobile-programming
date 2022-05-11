package com.example.mobile30_03.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobile30_03.database.relations.SongPlaylistCrossRef;

@Database(
        entities = {RUser.class, RPlaylist.class, RSong.class, SongPlaylistCrossRef.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "wellplay_db";
    public static final String USER_TABLE = "Ruser";
    public static final String SONG_TABLE = "Rsong";
    public static final String PLAYLIST_TABLE = "Rplaylist";

    public abstract WPDao wpDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context mContext) {
        if (instance == null) {
            instance = Room.databaseBuilder(mContext.getApplicationContext(),
                    AppDatabase.class,DATABASE_NAME).allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}