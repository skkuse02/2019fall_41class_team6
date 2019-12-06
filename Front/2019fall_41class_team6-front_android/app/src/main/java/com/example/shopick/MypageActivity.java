package com.example.shopick;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;

public class MypageActivity extends AppCompatActivity {
    SharedPreferences loginPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Button fbLogoutButton = findViewById(R.id.logout);
        fbLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPref = getSharedPreferences("login", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginPref.edit();
                editor.remove("login");
                editor.commit();
                Intent loginIntent = new Intent(MypageActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
