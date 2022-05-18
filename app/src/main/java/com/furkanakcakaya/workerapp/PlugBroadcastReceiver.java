package com.furkanakcakaya.workerapp;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class PlugBroadcastReceiver extends BroadcastReceiver {
    private IntentFilter intentFilter;
    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class).build();

    public PlugBroadcastReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            WorkManager.getInstance(context).enqueue(oneTimeWorkRequest);
            Log.d(TAG, "onReceive: " + intent.getAction());
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Log.d(TAG, "onReceive: " + intent.getAction());
        }
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }
}
