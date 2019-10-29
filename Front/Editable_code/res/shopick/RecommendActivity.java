package com.example.shopick;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;


public class RecommendActivity extends AppCompatActivity {         //recommend 버튼을 누른후 추천 Fashion 들의 목록 출력 액티비티

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);        //버튼변수설정안하고 일단 레이아웃만 해놨는데 이미지버튼오류
    }
}
