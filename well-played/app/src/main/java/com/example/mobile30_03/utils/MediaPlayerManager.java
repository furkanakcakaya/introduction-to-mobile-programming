package com.example.mobile30_03.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.example.mobile30_03.database.AppDatabase;
import com.example.mobile30_03.database.RPlaylist;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.database.relations.PlaylistsWithSongs;

import java.util.List;

public class MediaPlayerManager {
    private static MediaPlayerManager instance;
    private MediaPlayer mediaPlayer;
    private boolean isShuffle;
    private boolean isLooping;
    private List<RSong> allRSongs;
    private List<RSong> currentlyPlaying;
    private int currentSongIndex;

    private int currentPlaylistId;


    private MediaPlayerManager() {
        mediaPlayer = new MediaPlayer();
        isShuffle = false;
        currentSongIndex = 0;
    }

    public static MediaPlayerManager getInstance() {
        if (instance == null) {
            instance = new MediaPlayerManager();
        }
        return instance;
    }

    public void play() {
        if (!mediaPlayer.isPlaying()) mediaPlayer.start();
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(Context context, int index) {
        try {
            mediaPlayer.release();
        }catch (Exception e) {
            e.printStackTrace();
        }
        this.currentSongIndex = index;
        this.mediaPlayer = MediaPlayer.create(context, Uri.parse(currentlyPlaying.get(index).uri));
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isPause() {
        return isShuffle;
    }

    public void updateSonglist(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        RSong[] rsongs = HelperFunctions.audioMediaOperations(context);
        db.wpDao().insertSongs(rsongs);
    }

    public void fetchPlayer(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        allRSongs = db.wpDao().getAllSongs();
        currentlyPlaying = allRSongs;
    }

    public List<RSong> getAllRSongs() {
        return allRSongs;
    }

    public List<RSong> getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public void setPlaylist(int id, Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        if (id > 0){
            if (AppDatabase.getInstance(context).wpDao().
                    getSongsOfPlaylist(id).get(0).rSongs.size() > 0) {
                //play from playlist
                currentlyPlaying = AppDatabase.getInstance(context).wpDao().getSongsOfPlaylist(id)
                        .get(0).rSongs;
                currentPlaylistId = id;
                setMediaPlayer(context, 0);
            }else {
                Toast.makeText(context, "No songs in this playlist", Toast.LENGTH_SHORT).show();
                //play from all songs
                currentlyPlaying = allRSongs;
                currentPlaylistId = -1;
            }
        }else{
            //play from all songs
            currentlyPlaying = allRSongs;
            currentPlaylistId = -1;
        }
    }
}