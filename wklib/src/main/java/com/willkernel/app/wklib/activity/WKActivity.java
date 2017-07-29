package com.willkernel.app.wklib.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.willkernel.app.wklib.net.RequestManager;
import com.willkernel.app.wklib.utils.WKHandler;

/**
 * Created by willkernel on 2017/7/29.
 * mail:willkerneljc@gmail.com
 */

public abstract class WKActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected RequestManager requestManager;
    protected ProgressDialog progressDialog;// 用通知或者ProgressBar代替
    protected WKHandler wkHandler = new WKHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestManager = new RequestManager();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (requestManager != null) {
            requestManager.cancelRequest();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestManager != null) {
            requestManager.cancelRequest();
        }
    }
}