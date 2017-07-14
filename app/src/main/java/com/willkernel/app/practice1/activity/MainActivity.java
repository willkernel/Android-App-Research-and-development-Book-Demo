package com.willkernel.app.practice1.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.willkernel.app.practice1.R;
import com.willkernel.app.practice1.entity.Weather;
import com.willkernel.app.practice1.net.AbstractRequestCallback;
import com.willkernel.app.practice1.net.RemoteService;
import com.willkernel.app.practice1.net.RequestCallback;
import com.willkernel.app.practice1.net.RequestManager;
import com.willkernel.app.practice1.net.RequestParameter;
import com.willkernel.app.practice1.net.request.RequestAsyncTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RequestManager requestManager;
    private ProgressDialog progressDialog;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        requestManager = new RequestManager();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private void loadData2() {
        progressDialog.show();
        RequestCallback weatherCallback = new AbstractRequestCallback() {

            @Override
            public void onSuccess(String content) {
                Weather weather = JSON.parseObject(content, Weather.class);
                Weather.WeatherinfoBean weatherinfoBean = weather.getWeatherinfo();
                Log.e(TAG, weatherinfoBean.toString());
                progressDialog.dismiss();
            }

            @Override
            public void onFail(String errorMessage) {
                super.onFail(errorMessage);
                progressDialog.dismiss();
                //做其他处理
            }
        };
        ArrayList<RequestParameter> parameters = new ArrayList<>();
        RequestParameter requestParameter1 = new RequestParameter("cityId", "111");
        RequestParameter requestParameter2 = new RequestParameter("cityName", "Beijing");
        parameters.add(requestParameter1);
        parameters.add(requestParameter2);

        RemoteService.getInstance().invoke(this, requestManager, "getWeatherInfo", parameters, weatherCallback, false);
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
        if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
            loadData2();
            text.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData2();
                }
            }, 2000);
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