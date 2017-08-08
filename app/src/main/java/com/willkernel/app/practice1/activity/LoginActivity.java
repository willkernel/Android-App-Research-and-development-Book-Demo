package com.willkernel.app.practice1.activity;

import android.content.Intent;

import com.willkernel.app.practice1.R;
import com.willkernel.app.wklib.net.RequestCallback;


/**
 * Created by willkernel on 2017/7/28.
 * mail:willkerneljc@gmail.com
 * 登录页面，不同业务需求，登录成功后处理不同
 * 登录成功后保存cookie，加密序列化保存本地，请求时设置统一请求头
 */

public class LoginActivity extends BActivity {
    private boolean callback;

    @Override
    protected void initViews() {
        callback = getIntent().getBooleanExtra("callback", false);
        loginRequest();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setListeners() {

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