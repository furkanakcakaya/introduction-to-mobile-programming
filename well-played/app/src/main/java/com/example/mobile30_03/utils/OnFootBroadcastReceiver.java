package com.example.mobile30_03.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnFootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "OnFootBroadcastReceiver";
    String intentAction = "com.furkanakcakaya.onfoot.UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        if (intent.getAction().equals(intentAction)) {
            switch (intent.getStringExtra("mode")) {
                case "sport":
                    Log.i(TAG, "onReceive: sport");
                    break;
                case "meeting":
                    Log.i(TAG, "onReceive: meeting");
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
