package com.example.shopick;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ChangePWActivity extends AppCompatActivity {

    EditText pw;
    EditText pwc;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        pw=findViewById(R.id.pw);
        pwc=findViewById(R.id.pwc);
        done=findViewById(R.id.done);
        SharedPreferences prefs =getSharedPreferences("login", MODE_PRIVATE);
        final String result = prefs.getString("login", "0");
        Log.d("ID:",result);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( pw.length() == 0 ) {
                    Toast.makeText(ChangePWActivity.this, "Write the password", Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                    return;
                }

                // 비밀번호 확인 입력 확인
                if( pwc.getText().toString().length() == 0 ) {
                    Toast.makeText(ChangePWActivity.this, "Write the password check", Toast.LENGTH_SHORT).show();
                    pwc.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if( !pw.getText().toString().equals(pwc.getText().toString()) ) {
                    Toast.makeText(ChangePWActivity.this, "Password not identical", Toast.LENGTH_SHORT).show();
                    pw.setText("");
                    pwc.setText("");
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
                                Intent result = new Intent();
                                Toast.makeText(ChangePWActivity.this, "Success to change Password", Toast.LENGTH_SHORT).show();
                                // 자신을 호출한 Activity로 데이터를 보낸다.
                                setResult(RESULT_OK, result);
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePWActivity.this);
                                AlertDialog dialog=builder.setMessage("Fail to change PW").setPositiveButton("OK",null).create();
                                dialog.show();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                ChangePWRequest changePWRequest = new ChangePWRequest(result,pw.getText().toString(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(ChangePWActivity.this);
                queue.add(changePWRequest);
            }
        });
    }
}
