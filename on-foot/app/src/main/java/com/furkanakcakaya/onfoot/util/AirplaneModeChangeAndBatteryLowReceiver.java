package com.furkanakcakaya.onfoot.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AirplaneModeChangeAndBatteryLowReceiver extends BroadcastReceiver {
    private static final String TAG = "AirplaneModeChangeReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_AIRPLANE_MODE_CHANGED:
                boolean airplaneMode = intent.getBooleanExtra("state", false);
                if (airplaneMode) {
                    Toast.makeText(context, "Airplane mode is on", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Airplane mode is off", Toast.LENGTH_SHORT).show();
                }
                break;
            case Intent.ACTION_BATTERY_LOW:
                Toast.makeText(context, "Battery is low", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
