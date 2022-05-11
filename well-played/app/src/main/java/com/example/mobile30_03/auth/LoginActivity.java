package com.example.mobile30_03.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile30_03.R;
import com.example.mobile30_03.models.AppUsers;

public class LoginActivity extends AppCompatActivity {
    private int loginAttempts = 0;
    Button btn_login;
    TextView tv_login_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppUsers.populateUsers();

        EditText et_username = findViewById(R.id.et_username);
        EditText et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_login_error = findViewById(R.id.tv_login_error);
        Button btn_signup = findViewById(R.id.btn_signup);


        btn_signup.setOnClickListener(view -> registerClicked());
        btn_login.setOnClickListener(view -> loginClicked(et_username.getText().toString(), et_password.getText().toString()));
    }

    private void registerClicked() {
        Intent signUpIntent = new Intent(this, RegisterActivity.class);
        startActivity(signUpIntent);
        finish();
    }

    private void loginClicked(String username, String password) {
        Log.d("btn_login", "Login clicked!");
        if (loginAttempts++ >= 2){
            btn_login.setVisibility(View.GONE);
            tv_login_error.setVisibility(View.VISIBLE);
        }
        int login = AppUsers.tryToLogin(username,password);
        System.out.println(login);
        if (login > 0){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userId",  login);
            intent.putExtra("type",  "Giriş");
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
        }

    }
}

