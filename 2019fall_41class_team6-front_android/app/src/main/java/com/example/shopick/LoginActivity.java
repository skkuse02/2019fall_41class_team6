package com.example.shopick;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {            //로그인 화면
    EditText id;
    EditText pw;
    Button login;
    TextView signup;

    SharedPreferences loginPref;

    String shakey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = findViewById(R.id.ID);
        pw=findViewById(R.id.password);

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Context context = this;
        loginPref = context.getSharedPreferences( "login", Context.MODE_PRIVATE);


        final SharedPreferences.Editor editor = loginPref.edit();
        final String defaultValue = loginPref.getString("login", null);
        if (defaultValue != null) {
            startActivity(intent);
            finish();
        };

        login = findViewById(R.id.Login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID=id.getText().toString();
                String userPW=pw.getText().toString();
                if( userID.length() == 0 ) {
                    Toast.makeText(LoginActivity.this, "Write the ID", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                    return;
                }
                if( userPW.length() == 0 ) {
                    Toast.makeText(LoginActivity.this, "Write the PW", Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                shakey = id.getText().toString();
                                Log.d("shakey", shakey);
                                editor.putString("login", shakey);
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                AlertDialog dialog=builder.setMessage("Fail to Login").setPositiveButton("OK",null).create();
                                dialog.show();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID,userPW,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

        signup = findViewById(R.id.Signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), SignupActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent2, 1000);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // setResult를 통해 받아온 요청번호, 상태, 데이터
        Log.d("RESULT", requestCode + "");
        Log.d("RESULT", resultCode + "");
        Log.d("RESULT", data + "");

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            id.setText(data.getStringExtra("id"));
        }
    };
}

