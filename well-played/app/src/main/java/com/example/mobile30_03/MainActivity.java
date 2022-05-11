package com.example.mobile30_03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile30_03.auth.LoginActivity;
import com.example.mobile30_03.auth.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_login = findViewById(R.id.btn_login);
        Button btn_signup = findViewById(R.id.btn_signup);
        Button btn_guest = findViewById(R.id.btn_guest);

        btn_login.setOnClickListener(view -> {
            Log.d("btn_login", "Login clicked!");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });

        btn_signup.setOnClickListener(view -> {
            Log.d("btn_login", "Login clicked!");
            Intent signUpIntent = new Intent(this, RegisterActivity.class);
            startActivity(signUpIntent);
            finish();
        });


    }
}

