package com.example.mobile30_03;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mobile30_03.databinding.ActivityWellPlayedBinding;
import com.example.mobile30_03.utils.OnFootBroadcastReceiver;

public class WellPlayedActivity extends AppCompatActivity {
    private ActivityWellPlayedBinding binding;
    private OnFootBroadcastReceiver onFootBroadcastReceiver = new OnFootBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWellPlayedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHost);
        NavigationUI.setupWithNavController(binding.bottomBarWP, navHostFragment.getNavController());
    }

    @Override
    protected void onStart() {
        super.onStart();
        String intentAction = "com.furkanakcakaya.onfoot.UPDATE";
        IntentFilter filter = new IntentFilter(intentAction);
        registerReceiver(onFootBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(onFootBroadcastReceiver);
    }
}