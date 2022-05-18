package com.furkanakcakaya.workerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.BroadcastReceiver;
import android.os.Bundle;

import com.furkanakcakaya.workerapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static ActivityMainBinding binding;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PlugBroadcastReceiver plugBroadcastReceiver = new PlugBroadcastReceiver();

        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(plugBroadcastReceiver.oneTimeWorkRequest.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        binding.tvWork.setText(workInfo.getOutputData().getString("message"));
                    } else {
                        binding.tvWork.setText("Work is not finished yet");
                    }
                });

        registerReceiver(plugBroadcastReceiver, plugBroadcastReceiver.getIntentFilter());

    }
}