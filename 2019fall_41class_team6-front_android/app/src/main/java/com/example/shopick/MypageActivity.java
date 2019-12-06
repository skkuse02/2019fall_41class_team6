package com.example.shopick;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;

public class MypageActivity extends AppCompatActivity {
    SharedPreferences loginPref;
    TextView id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        id=findViewById(R.id.id);
        SharedPreferences prefs =getSharedPreferences("login", MODE_PRIVATE);
        String result = prefs.getString("login", "0");
        Log.d("ID:",result);
        id.setText(result);

        Button logout_btn = findViewById(R.id.logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
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
