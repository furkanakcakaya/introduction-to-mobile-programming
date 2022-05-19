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
    private Sensor lightSensor;
    private TextView tvResult;
    private Button btnNext;
    private static final double proximityE = 0.9;
    private static final double linearAccE = 0.055;
    private boolean isLightBelowThreshold = false;
    private boolean isLinearAccBelowThreshold = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        Log.d(TAG, "onCreate: " + lightSensor.getResolution());
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
        sensorManager.registerListener( this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener( this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        Log.i(TAG, "onDestroy: unregister listener");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)    {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            if (sensorEvent.values[0] < proximityE && !isLightBelowThreshold) {
                isLightBelowThreshold = true;
                update();
            }else if (sensorEvent.values[0] >= proximityE && isLightBelowThreshold) {
                isLightBelowThreshold = false;
                update();
            }
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            double x = Math.sqrt(sensorEvent.values[0] * sensorEvent.values[0] + sensorEvent.values[1] * sensorEvent.values[1] + sensorEvent.values[2] * sensorEvent.values[2]);
            if (x < linearAccE && !isLinearAccBelowThreshold) {
                isLinearAccBelowThreshold = true;
                update();
            }else if(x >= linearAccE && isLinearAccBelowThreshold){
                isLinearAccBelowThreshold = false;
                update();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    void update(){
        Intent intent = new Intent();
        intent.setAction("com.furkanakcakaya.onfoot.UPDATE");
        if (isLightBelowThreshold) {
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
        Log.i(TAG, "update: sending broadcast");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        getApplicationContext().sendBroadcast(intent);
    }
}