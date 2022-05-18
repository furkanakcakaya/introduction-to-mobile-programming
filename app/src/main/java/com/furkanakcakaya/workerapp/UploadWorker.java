package com.furkanakcakaya.workerapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadWorker extends Worker {
    private static final String TAG = "UploadWorker";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "Uploading...");

        int count = 0;

        for (int i = 1; i <= 500000000; i++) {
            if (i % 50000000 == 0){
                count += 10;
                Log.i(TAG, "doWork: " + count);
            }
        }

        Data outputData = new Data.Builder().putString("message", "File uploaded.").build();
        return Result.success(outputData);
    }
}
