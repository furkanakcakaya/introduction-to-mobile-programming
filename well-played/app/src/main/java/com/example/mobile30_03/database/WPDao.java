package com.example.mobile30_03.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobile30_03.database.relations.PlaylistsWithSongs;
import com.example.mobile30_03.database.relations.SongPlaylistCrossRef;
import com.example.mobile30_03.database.relations.SongsWithPlaylists;

import java.util.List;

@Dao
public interface WPDao {
    @Query("SELECT * FROM RUser")
    List<RUser> getAllUsers();

    @Query("SELECT * FROM RSong ORDER BY name ASC")
    List<RSong> getAllSongs();

    @Query("SELECT * FROM RPlaylist ORDER BY name ASC")
    List<RPlaylist> getAllPlaylists();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(RUser user);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSong(RSong song);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSongs(RSong... songs);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlaylist(RPlaylist playlist);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSongPlaylistCrossRef(SongPlaylistCrossRef crossRef) ;

    @Delete
    void deleteUser(RUser user);

    @Delete
    void deleteSong(RSong song);

    @Delete
    void deletePlaylist(RPlaylist playlist);

    @Transaction
    @Query("SELECT * FROM RUser WHERE username = :username")
    List<RUser> getUserWithPlaylists(String username);

    @Transaction
    @Query("SELECT * FROM RSong WHERE songId = :sid")
    List<SongsWithPlaylists> getPlaylistsOfSong(int sid);

    @Transaction
    @Query("SELECT * FROM RPlaylist WHERE playlistId = :pid")
    List<PlaylistsWithSongs> getSongsOfPlaylist(int pid);

    //delete song from playlist
    @Query("DELETE FROM SongPlaylistCrossRef WHERE songId = :sid AND playlistId = :pid")
    void deleteSongFromPlaylist(int pid, int sid);


    @Query("SELECT * FROM RPlaylist WHERE playlistId = :pid")
    RPlaylist getPlaylistById(int pid);
}