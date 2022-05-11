package com.example.mobile30_03.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mobile30_03.database.RSong;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HelperFunctions {
    public static String milliSecondsToTimer(long milliSeconds){
        String timerString="";
        String secondString;
        int hour=(int) (milliSeconds/(1000*60*60));
        int minutes=(int)(milliSeconds%(1000*60*60))/(1000*60);
        int seconds=(int)((milliSeconds%(1000*60*60))%(1000*60)/1000);
        if(hour>0){
            timerString=hour+":";
        }
        if(seconds<10){
            secondString="0"+seconds;
        }
        else {
            secondString=""+seconds;
        }
        timerString=timerString+minutes+":"+secondString;
        return timerString;
    }

    public static RSong[] audioMediaOperations(Context context){
        List<RSong> songs = new ArrayList<>();

        Uri collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM
        };
        String selection = MediaStore.Audio.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES))
        };
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        try (Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int songColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int artistColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String song_name = cursor.getString(songColumn);
                String artist_name = cursor.getString(artistColumn);
                int duration = cursor.getInt(durationColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                songs.add(new RSong(contentUri.toString(), song_name, artist_name, duration));
            }
        }
        return songs.toArray(new RSong[songs.size()]);
    }

    public static Bitmap getBitmapFromContentURI(Context context, Uri uri){
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context, uri);
            byte[] data = mmr.getEmbeddedPicture();
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getRandomIndex(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }
}
