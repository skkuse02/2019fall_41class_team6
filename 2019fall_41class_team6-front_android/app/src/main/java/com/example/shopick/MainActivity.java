package com.example.shopick;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {                  //카메라, recommend 버튼이 있는 화면

    private Button button;
    Button search_btn;
    EditText search_content;
    boolean[] checkTag=new boolean[3];

    TextView tag1, tag2, tag3;
    ImageView image;
    String tag;
    ArrayList<String> imgURL = new ArrayList<String>();
    GridView gv;
    MyAdapter adapter;    // 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tag1=findViewById(R.id.tag1);
        tag2=findViewById(R.id.tag2);
        tag3=findViewById(R.id.tag3);
        search_content=findViewById(R.id.search);
        search_btn=findViewById(R.id.button2);
        image=findViewById(R.id.imageView);
        gv= findViewById(R.id.gridView);
        Button fbLogoutButton = findViewById(R.id.mypage);
        button = findViewById(R.id.cameraBtn);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width;
        width = size.x;

        adapter = new MyAdapter (getApplicationContext(),R.layout.imagegridview,imgURL, width);
        gv.setAdapter(adapter);
        SharedPreferences loginPref = getSharedPreferences("login", Activity.MODE_PRIVATE);
        final String userID = loginPref.getString("login", "0");
        Log.d("IDIDIDID", userID);

        //카메라 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, PreViewActivity.class);
                startActivity(loginIntent);
            }
        });

        fbLogoutButton.setOnClickListener(new View.OnClickListener() {           //로그인화면으로 가면서 로그아웃하는 버튼  임시로만들어놓음
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, MypageActivity.class);
                startActivity(loginIntent);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent=search_content.getText().toString();
                Log.d("넘겨지는 애가 뭔지 확인",searchContent);
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                searchIntent.putExtra("searchContent",searchContent);
                startActivity(searchIntent);
            }
        });
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean exists = jsonResponse.getBoolean("exists");
                    if(exists){

                        String tag1_str=jsonResponse.getString("1");
                        String tag2_str=jsonResponse.getString("2");
                        String tag3_str=jsonResponse.getString("3");
                        Log.d("Get tag",tag1_str+tag2_str+tag3_str);
                        if(!tag1_str.equals("-1")){
                            tag1.setText("#"+tag1_str);
                            tag1.setVisibility(View.VISIBLE);
                            checkTag[0]=true;
                            getImageURL(0);
                            adapter.updateContent(imgURL);
                        }
                        if(!tag2_str.equals("-1")){
                            tag2.setText("#"+tag2_str);
                            tag2.setVisibility(View.VISIBLE);

                        }
                        if(!tag3_str.equals("-1")){
                            tag3.setText("#"+tag3_str);
                            tag3.setVisibility(View.VISIBLE);

                        }
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        AlertDialog dialog=builder.setMessage("There's no tag").setPositiveButton("OK",null).create();
                        dialog.show();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        GetTagRequest getTagRequest = new GetTagRequest(userID,responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(getTagRequest);

        Log.d("태그 이미지 갖고오기", "SUCCESS");
        Log.d("가져온 이후",Integer.toString(imgURL.size()));
        adapter.updateContent(imgURL);
        tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTag[0]=true;
                checkTag[1]=false;
                checkTag[2]=false;
                getImageURL(0);
                adapter.updateContent(imgURL);
                Log.d("IMG LENGTHHHHHH",Integer.toString(imgURL.size()));
            }
        });
        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTag[1]=true;
                checkTag[0]=false;
                checkTag[2]=false;
                getImageURL(1);
                adapter.updateContent(imgURL);
                Log.d("IMG LENGTHHHHHH",Integer.toString(imgURL.size()));
            }
        });
        tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTag[2]=true;
                checkTag[0]=false;
                checkTag[1]=false;
                getImageURL(2);
                adapter.updateContent(imgURL);
                Log.d("IMG LENGTHHHHHH",Integer.toString(imgURL.size()));
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String storeUrl;
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String clothe;
                if(checkTag[0]==true){
                    clothe=tag1.getText().toString().substring(1)+Integer.toString(position);
                }else if(checkTag[1]==true){
                    clothe=tag2.getText().toString().substring(1)+Integer.toString(position);
                }else{
                    clothe=tag3.getText().toString().substring(1)+Integer.toString(position);
                }
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                RequestQueue queue3 = Volley.newRequestQueue(MainActivity.this);
                queue3.add(gotoStoreRequest);

            }

        });


    }
    /*private class YourTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            getImageURL(1);
            return "TRUE";
        }

        @Override
        protected void onPostExecute(String result)
        {
            adapter.updateContent(imgURL);
            Log.d("IMG LENGTHHHHHH",Integer.toString(imgURL.size()));
        }
    }*/

    public void getImageURL(int id) {
        if(id==0){
            tag=tag1.getText().toString().substring(1);
        }else if(id==1){
            tag=tag2.getText().toString().substring(1);
        }else{
            tag=tag2.getText().toString().substring(1);
        }
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
        RequestQueue queue2 = Volley.newRequestQueue(MainActivity.this);
        queue2.add(getImageRequest);
    }


}
class MyAdapter extends BaseAdapter {
    Context context;
    int layout;
    String[] img;
    LayoutInflater inf;
    Bitmap bitmap;
    int width;

    public MyAdapter(Context context, int layout, ArrayList<String> img, int width) {
        this.context = context;
        this.layout = layout;
        this.img = img.toArray(new String[img.size()]);
        this.width=width;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("IMG LENGTH",Integer.toString(img.size()));
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return img[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("어댑터 실행","실행은 됨");
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(width/3, width/3));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(6, 4, 6, 4);
        }
        else {
            imageView = (ImageView) convertView;
        }
        if(position<img.length){
            getImage(img[position], imageView);
        }
        return imageView;
    }
    public void updateContent (ArrayList<String> updates) {
        img=updates.toArray(new String[updates.size()]);
        Log.d("DEBUG","Line 126 " + this.img.length);              //--- Shows 3 again --
        this.notifyDataSetChanged();                  //--- Screen still shows original 16 images--
    }
    public void getImage(final String imgurl, ImageView imageView){
        Thread mthread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imgurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {

                }
            }
        };
        mthread.start();
        try {
            mthread.join();
            imageView.setImageBitmap(bitmap);
        }
        catch (InterruptedException e) {

        }
    }
}