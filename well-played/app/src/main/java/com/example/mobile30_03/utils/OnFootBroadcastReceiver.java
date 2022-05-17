package com.example.mobile30_03.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

public class OnFootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "OnFootBroadcastReceiver";
    String intentAction = "com.furkanakcakaya.onfoot.UPDATE";
    AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (intent.getAction().equals(intentAction)) {
            switch (intent.getStringExtra("mode")) {
                case "sport":
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                    break;
                case "meeting":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMinVolume(AudioManager.USE_DEFAULT_STREAM_TYPE), 0);
                    }
                    break;
                case "default":
                    Log.i(TAG, "onReceive: default");
                    break;
                default:
                    Log.i(TAG, "onReceive: ????");
                    break;
            }
        }
    }
}
