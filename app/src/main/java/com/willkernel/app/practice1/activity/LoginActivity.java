package com.willkernel.app.practice1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.willkernel.app.practice1.net.AbstractRequestCallback;
import com.willkernel.app.practice1.net.RequestCallback;

/**
 * Created by willkernel on 2017/7/28.
 * mail:willkerneljc@gmail.com
 * 登录页面，不同业务需求，登录成功后处理不同
 * 登录成功后保存cookie，加密序列化保存本地，请求时设置统一请求头
 */

public class LoginActivity extends AppCompatActivity {
    private boolean callback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = getIntent().getBooleanExtra("callback", false);
        loginRequest();
    }

    private void loginRequest() {
        RequestCallback loginCallback = new AbstractRequestCallback() {
            @Override
            public void onSuccess(String content) {
                if (callback) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
            }
        };
    }
}
