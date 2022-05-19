package com.example.mobile30_03.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class OnFootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "OnFootBroadcastReceiver";
    private static AudioManager audioManager;
    private static int currentVolume;
    private static int maxVolume;

    @Override
    public void onReceive(Context context, Intent intent) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d("OnFootBroadcastReceiver", "currentVolume: " + currentVolume + " maxVolume: " + maxVolume);

        switch (intent.getStringExtra("mode")) {
            case "sport":
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume*3/4, 0);
                break;
            case "meeting":
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                break;
            case "default":
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume/2, 0);
                break;
            default:
                Log.i(TAG, "onReceive: this shouldnt happen");
                break;
        }
    }
}
