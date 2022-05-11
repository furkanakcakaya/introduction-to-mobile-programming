package com.furkanakcakaya.onfoot;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.furkanakcakaya.onfoot.util.AirplaneModeChangeAndBatteryLowReceiver;

public class BroadcastActivity extends AppCompatActivity {
    private static final String TAG = "BroadcastActivity";
    AirplaneModeChangeAndBatteryLowReceiver airplaneModeChangeAndBatteryLowReceiver = new AirplaneModeChangeAndBatteryLowReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        TextView tvBroadcastResult = findViewById(R.id.tvBroadcastResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        registerReceiver(airplaneModeChangeAndBatteryLowReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneModeChangeAndBatteryLowReceiver);
    }
}