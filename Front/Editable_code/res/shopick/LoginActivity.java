package com.example.shopick;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {            //로그인 화면
    private CallbackManager callbackManager;
    LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        loginButton =  findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {          //로그인성공
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            @Override
            public void onCancel() {
                disconnectFromFacebook();                    //로그인취소
                LoginManager.getInstance().logOut();
            }
            @Override
            public void onError(FacebookException error) {          //로그인 실패
                Log.e("LoginErr",error.toString());            //로그출력
            }
        });
    }
    public void disconnectFromFacebook() {            //로그아웃(오류?  한번로그인한뒤 계속 세션이 저장돼있음)
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

