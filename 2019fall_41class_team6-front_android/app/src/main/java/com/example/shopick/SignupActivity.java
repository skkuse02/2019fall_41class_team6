package com.example.shopick;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    EditText id;
    EditText name;
    EditText pw;
    EditText pwc;
    Button btnDone;
    CheckBox check;
    EditText instaID;
    EditText instaPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        id=findViewById(R.id.id);
        name=findViewById(R.id.name);
        pw=findViewById(R.id.pw);
        pwc=findViewById(R.id.pwc);

        btnDone=findViewById(R.id.done);

        check=findViewById(R.id.checkBox);
        instaID=findViewById(R.id.instaID);
        instaPW=findViewById(R.id.instaPW);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // 체크되면 모두 보이도록 설정
                if (check.isChecked() == true) {
                    instaID.setVisibility(android.view.View.VISIBLE);
                    instaPW.setVisibility(android.view.View.VISIBLE);
                } else {
                    instaID.setVisibility(android.view.View.INVISIBLE);
                    instaPW.setVisibility(android.view.View.INVISIBLE);
                }
            }
        });

        pwc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = pw.getText().toString();
                String confirm = pwc.getText().toString();

                if( password.equals(confirm) ) {
                    pw.setBackgroundColor(Color.parseColor("#A3E4D7"));
                    pwc.setBackgroundColor(Color.parseColor("#A3E4D7"));
                } else {
                    pw.setBackgroundColor(Color.parseColor("#EC7063"));
                    pwc.setBackgroundColor(Color.parseColor("#EC7063"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=name.getText().toString();
                String userID=id.getText().toString();
                String userPW=pw.getText().toString();
                String instagramID=instaID.getText().toString();
                String instagramPW=instaPW.getText().toString();

                if( userName.length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "Write the name", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }
                if( userID.length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "Write the ID", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                    return;
                }
                // 비밀번호 입력 확인
                if( userPW.length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "Write the password", Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                    return;
                }

                // 비밀번호 확인 입력 확인
                if( pwc.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "Write the password check", Toast.LENGTH_SHORT).show();
                    pwc.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if( !pw.getText().toString().equals(pwc.getText().toString()) ) {
                    Toast.makeText(SignupActivity.this, "Password not identical", Toast.LENGTH_SHORT).show();
                    pw.setText("");
                    pwc.setText("");
                    pw.requestFocus();
                    return;
                }
                if(check.isChecked()){
                    if( instagramID.length() == 0 ) {
                        Toast.makeText(SignupActivity.this, "Write the Instagram ID", Toast.LENGTH_SHORT).show();
                        pw.requestFocus();
                        return;
                    }
                    if( instagramPW.length() == 0 ) {
                        Toast.makeText(SignupActivity.this, "Write the Instagram password", Toast.LENGTH_SHORT).show();
                        pw.requestFocus();
                        return;
                    }
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            Log.d("SinupActivity",jsonResponse.toString());
                            if(success){
                                Intent result = new Intent();
                                Toast.makeText(SignupActivity.this, "Signup success!", Toast.LENGTH_SHORT).show();
                                // 자신을 호출한 Activity로 데이터를 보낸다.
                                result.putExtra("id", id.getText().toString());
                                setResult(RESULT_OK, result);
                                finish();
                            }
                            else
                            {
                                boolean exist = jsonResponse.getBoolean("exist");
                                if(exist){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                    AlertDialog dialog=builder.setMessage("Already existed ID. Choose another one.").setPositiveButton("OK",null).create();
                                    dialog.show();
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                    AlertDialog dialog = builder.setMessage("Fail to Signup").setPositiveButton("OK", null).create();
                                    dialog.show();
                                }
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                SignupRequest signupRequest = new SignupRequest(userName, userID,userPW,instagramID, instagramPW,responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(signupRequest);
            }
        });
    }
}

