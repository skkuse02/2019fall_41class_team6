package com.example.shopick;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

public class MypageActivity extends AppCompatActivity {
    SharedPreferences loginPref;
    TextView id;
    TextView name;
    Button chngPW;
    Button logout_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        name=findViewById(R.id.name);
        id=findViewById(R.id.id);
        chngPW=findViewById(R.id.changePW);
        logout_btn = findViewById(R.id.logout);

        SharedPreferences prefs =getSharedPreferences("login", MODE_PRIVATE);
        final String result = prefs.getString("login", "0");
        Log.d("ID:",result);
        id.setText(result);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        String getName=jsonResponse.getString("name");
                        name.setText("Name : "+getName);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MypageActivity.this);
                        AlertDialog dialog=builder.setMessage("Fail to get name").setPositiveButton("OK",null).create();
                        dialog.show();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        GetNameRequest getNameRequest = new GetNameRequest(result,responseListener);
        RequestQueue queue = Volley.newRequestQueue(MypageActivity.this);
        queue.add(getNameRequest);

        chngPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangePWActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1000);
            }
        });
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
