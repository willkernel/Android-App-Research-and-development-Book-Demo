package com.willkernel.app.practice1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

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

    public void gotoAnyWhere(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("gotoMovieDetail:")) {
                String strMovieId = url.substring(24);
                int movieId = Integer.valueOf(strMovieId);
                Intent intent = new Intent(this, MovieDetailActivity.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            } else if (url.startsWith("gotoNewsList:")) {
                Intent intent = new Intent(this, NewsListActivity.class);
                String strCityId = url.substring(13);
                String strCityName = url.substring(28);
                int cityId = Integer.valueOf(strCityId);
                intent.putExtra("cityId", cityId);
                intent.putExtra("cityName", strCityName);
                startActivity(intent);
            } else if (url.startsWith("gotoPersonCenter")) {
                Intent intent = new Intent(this, PersonCenterActivity.class);
                startActivity(intent);
            } else if (url.startsWith("gotoUrl:")) {
                String strUrl = url.substring(8);
//                wv.loadUrl(strUrl);
            }
        }
    }

    public String getAndroidPageName(String key) {
        int pos = key.indexOf(",");
        if (pos == -1) {
            return key;
        } else {
            return key.substring(0, pos);
        }
    }

    public void gotoAnyWhere2(String url) {
        if (TextUtils.isEmpty(url)) return;
        String pageName = getAndroidPageName(url);
        if (TextUtils.isEmpty(pageName)) return;
        Intent intent = new Intent();
        int pos = url.indexOf(":");
        if (pos > 0) {
            String params = url.substring(pos);
            String[] pairs = params.split("&");
            for (String pair : pairs) {
                String[] arr = pair.split("=");
                String key = arr[0];
                String value = arr[1];
                if (value.startsWith("(int)")) {
                    intent.putExtra(key, Integer.valueOf(value.substring(5)));
                } else if (value.startsWith("(Double)")) {
                    intent.putExtra(key, Integer.valueOf(value.substring(8)));
                } else {
                    intent.putExtra(key, value);
                }
            }
        }

        try {
            intent.setClass(this, Class.forName(pageName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }
}