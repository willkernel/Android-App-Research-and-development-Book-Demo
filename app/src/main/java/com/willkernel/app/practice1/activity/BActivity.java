package com.willkernel.app.practice1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.willkernel.app.practice1.utils.PreferenceUtil;
import com.willkernel.app.wklib.activity.WKActivity;
import com.willkernel.app.wklib.net.RequestCallback;
import com.willkernel.app.wklib.utils.WKPrefUtil;

public abstract class BActivity extends WKActivity {
    protected WKPrefUtil preferenceUtil;

    abstract class AbstractRequestCallback implements RequestCallback {

        @Override
        public void onFail(String errorMessage) {
            progressDialog.dismiss();
            new AlertDialog.Builder(BActivity.this).setTitle("出错啦")
                    .setMessage(errorMessage).setPositiveButton("确定", null)
                    .show();
        }

        @Override
        public void onCookieExpired() {
            progressDialog.dismiss();
            new AlertDialog.Builder(BActivity.this).setTitle("Error").setMessage("Cookie expired,login again").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(BActivity.this, LoginActivity.class).putExtra("callback", true));
                }
            }).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        preferenceUtil = PreferenceUtil.getInstance(this);
        initViews();
        setListeners();
    }

    protected abstract void initViews();

    protected abstract int getLayoutId();

    protected abstract void setListeners();
}