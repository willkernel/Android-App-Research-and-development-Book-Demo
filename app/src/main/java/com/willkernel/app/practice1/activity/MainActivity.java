package com.willkernel.app.practice1.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.willkernel.app.practice1.CApp;
import com.willkernel.app.practice1.R;
import com.willkernel.app.practice1.entity.Weather;
import com.willkernel.app.wklib.net.HttpRequest;
import com.willkernel.app.wklib.net.RemoteService;
import com.willkernel.app.wklib.net.RequestCallback;
import com.willkernel.app.wklib.net.RequestParameter;
import com.willkernel.app.wklib.net.request.RequestAsyncTask;

import java.util.ArrayList;

public class MainActivity extends BActivity {
    private TextView textView;

    @Override
    protected void initViews() {
        textView = (TextView) findViewById(R.id.text);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setListeners() {
        loginBtn();
    }

    private void loginBtn() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferenceUtil.isLogin()) {
                    go2News();
                } else {
                    go2Login();
                }
            }

            private void go2Login() {
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class).putExtra("callback", true), 100);
            }

            private void go2News() {

            }
        });

        findViewById(R.id.button_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Date=" + HttpRequest.getSeverTime());
            }
        });
    }

    protected void loadData() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        RequestCallback weatherCallback = new AbstractRequestCallback() {

            @Override
            public void onSuccess(String content) {
                Weather weather = JSON.parseObject(content, Weather.class);
                Weather.WeatherinfoBean weatherinfoBean = weather.getWeatherinfo();
                if (weatherinfoBean != null) {
                    Log.e(TAG, weatherinfoBean.toString());
                    textView.setText(weatherinfoBean.getCity());
                }
                progressDialog.cancel();
            }
        };
        ArrayList<RequestParameter> parameters = new ArrayList<>();
        RequestParameter requestParameter1 = new RequestParameter("cityId", "111");
        RequestParameter requestParameter2 = new RequestParameter("cityName", "Beijing");
        parameters.add(requestParameter1);
        parameters.add(requestParameter2);

        RemoteService.getInstance().invoke(CApp.getInstance(), requestManager, "getWeatherInfo", parameters, weatherCallback, true);
    }

    private void loadData1() {
        String url = "http://www.weather.com.cn/data/sk/101010100.html";
        RequestAsyncTask task = new RequestAsyncTask() {
            @Override
            protected void onSuccess(String content) {
                Weather weather = JSON.parseObject(content, Weather.class);
                Weather.WeatherinfoBean weatherinfoBean = weather.getWeatherinfo();
                Log.e(TAG, weatherinfoBean.toString());
//                WeatherinfoBean{city='北京', cityid='101010100', temp='18', WD='东南风', WS='1级', SD='17%', WSE='1', time='17:05', isRadar='1', Radar='JC_RADAR_AZ9010_JB', njd='暂无实况', qy='1011', rain='0'}
            }

            @Override
            protected void onFail(String errorMessage) {
                Log.e(TAG, errorMessage);
            }
        };
        task.execute(url);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadData();
            wkHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 2000);
        }
    }
}