package com.example.shopick;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;


public class MainActivity extends AppCompatActivity {                  //카메라, recommend 버튼이 있는 화면

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.cameraBtn);                     //카메라 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, PreViewActivity.class);
                startActivity(loginIntent);
            }
        });
        Button fbLogoutButton = findViewById(R.id.btn_fb_logout);
        fbLogoutButton.setOnClickListener(new View.OnClickListener() {           //로그인화면으로 가면서 로그아웃하는 버튼  임시로만들어놓음
            @Override
            public void onClick(View view) {
                disconnectFromFacebook();
                LoginManager.getInstance().logOut();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });


        Button recommend = findViewById(R.id.recommendBtn);                   //추천버튼  추천화면으로 넘어감(SNS 사진긁어오는기능 추가필요)
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
                startActivity(intent);
            }
        });

    }
    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

}

