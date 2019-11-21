package com.example.shopick;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PreViewActivity extends AppCompatActivity {
    private TextureView mCameraTextureView;
    private PreView mPreview;
    private Button mCameraCaptureButton;

    Activity mainActivity = this;

    private static final String TAG = "MAINACTIVITY";

    static final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view);
        mCameraCaptureButton = findViewById(R.id.capture);
        mCameraTextureView = (TextureView) findViewById(R.id.cameraTextureView);
        mPreview = new PreView(this, mCameraTextureView, mCameraCaptureButton);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission();
        switch (requestCode) {
            case REQUEST_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            mCameraTextureView = (TextureView) findViewById(R.id.cameraTextureView);
                            mPreview = new PreView(mainActivity, mCameraTextureView, mCameraCaptureButton);
                            mPreview.openCamera();
                            Log.d(TAG, "mPreview set");
                        } else {
                            Toast.makeText(this, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mPreview.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mPreview.onPause();
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 마시멜로우 버전과 같거나 이상이라면
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);  //마지막 인자는 체크해야될 권한 갯수

            } else {
                Toast.makeText(this, "권한 승인되었음", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
