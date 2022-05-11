package com.furkanakcakaya.onfoot;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener  {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor linearAccelerationSensor;
    private Sensor proximitySensor;
    private TextView tvResult;
    private Button btnNext;
    private static final double proximityE = 0.9;
    private static final double linearAccE = 0.055;
    private boolean isProximityBelowThreshold = false;
    private boolean isLinearAccBelowThreshold = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        Log.d(TAG, "onCreate: " + proximitySensor.getResolution());
        Log.d(TAG, "onCreate: " + linearAccelerationSensor.getResolution());

        tvResult = findViewById(R.id.tvResult);
        tvResult.setText("Sensorler devreye aliniyor...");

        btnNext = findViewById(R.id.btnInt);
        btnNext.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, BroadcastActivity.class);
            startActivity(intent);

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener( this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener( this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)    {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (sensorEvent.values[0] < proximityE) {
                isProximityBelowThreshold = true;
            }else{
                isProximityBelowThreshold = false;
            }
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            double x = Math.sqrt(sensorEvent.values[0] * sensorEvent.values[0] + sensorEvent.values[1] * sensorEvent.values[1] + sensorEvent.values[2] * sensorEvent.values[2]);
            if (x < linearAccE) {
                isLinearAccBelowThreshold = true;
            }else {
                isLinearAccBelowThreshold = false;
            }
        }
        update();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    void update(){
        Intent intent = new Intent();
        intent.setAction("com.furkanakcakaya.onfoot.UPDATE");
        if (isProximityBelowThreshold) {
            if (isLinearAccBelowThreshold) {
                tvResult.setText("CEPTE, HAREKETSIZ");
                intent.putExtra("mode", "default");
            }else{
                tvResult.setText("CEPTE, HAREKETLI");
                //Spor modu
                intent.putExtra("mode", "sport");
            }
        }else if (isLinearAccBelowThreshold) {
            tvResult.setText("CEPTE DEGIL, HAREKETSIZ");
            //Toplanti modu
            intent.putExtra("mode", "meeting");
        }else{
            tvResult.setText("CEPTE DEGIL, HAREKETLI");
            intent.putExtra("mode", "default");
        }
        sendBroadcast(intent);
    }
}