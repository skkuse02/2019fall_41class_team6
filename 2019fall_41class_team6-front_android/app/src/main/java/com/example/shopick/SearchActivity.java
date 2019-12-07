package com.example.shopick;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SearchActivity extends AppCompatActivity {

    EditText search_content;
    Button search_btn;

    TextView searchContent;
    GridView gv;
    ArrayList<String> imgURL = new ArrayList<String>();
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_content=findViewById(R.id.search);
        search_btn=findViewById(R.id.button2);
        Intent intent = getIntent(); /*데이터 수신*/
        gv=findViewById(R.id.gridView);
        searchContent=findViewById(R.id.tag);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width;
        width = size.x;

        adapter = new MyAdapter (getApplicationContext(),R.layout.imagegridview,imgURL, width);
        gv.setAdapter(adapter);

        String temp=intent.getExtras().getString("searchContent");
        Log.d("넘겨지는 애가 뭔지 확인22",temp);
        search_content.setText(temp); /*String형*/
        search_content.requestFocus();
        searchContent.setText("#"+temp);
        searchContent.setVisibility(View.VISIBLE);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = search_content.getText().toString();
                search_content.setText(temp); /*String형*/
                search_content.requestFocus();
                searchContent.setText("#"+temp);
                searchContent.setVisibility(View.VISIBLE);
                getImageURL();
                adapter.updateContent(imgURL);
            }
        });
        getImageURL();
        adapter.updateContent(imgURL);

    }
    public void getImageURL() {
        final String tag=search_content.getText().toString();
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                try {
                    Log.d("태그가 뭘까요", tag);
                    JSONObject jsonResponse2 = new JSONObject(response2);
                    boolean exists = jsonResponse2.getBoolean("exists");
                    if (exists) {
                        imgURL.clear();
                        Iterator k = jsonResponse2.keys();
                        k.next();
                        while(k.hasNext()){
                            String store_url = jsonResponse2.getString(k.next().toString());
                            final String store_url_next = store_url.replaceAll("\\\\", "");
                            imgURL.add(store_url_next);
                            Log.d("Image success", Integer.toString(imgURL.size()));
                        }
                        adapter.updateContent(imgURL);
                    } else {
                        Log.d("Image ", "Fail");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        GetImageRequest getImageRequest = new GetImageRequest(tag, responseListener2);
        RequestQueue queue2 = Volley.newRequestQueue(SearchActivity.this);
        queue2.add(getImageRequest);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String storeUrl;
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String clothe=searchContent.getText().toString().substring(1)+Integer.toString(position);
                Log.d("보내지는 내용 확인",clothe);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("내용을 알아보자",jsonResponse.toString());
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                storeUrl=jsonResponse.getString("URL");
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl));
                                startActivity(intent);
                                Log.d("가게 URL 확인",storeUrl);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                                AlertDialog dialog=builder.setMessage("URL 가져오기 실패").setPositiveButton("OK",null).create();
                                dialog.show();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                GoToStoreRequest gotoStoreRequest = new GoToStoreRequest(clothe,responseListener);
                RequestQueue queue3 = Volley.newRequestQueue(SearchActivity.this);
                queue3.add(gotoStoreRequest);

            }

        });
    }
}
