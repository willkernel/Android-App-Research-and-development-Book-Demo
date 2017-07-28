package com.willkernel.app.practice1.net;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.willkernel.app.practice1.mockdata.MockService;
import com.willkernel.app.practice1.net.response.Response;

import java.util.ArrayList;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */
public class RemoteService {
    private static final String TAG = "RemoteService";
    private static RemoteService remoteService;
    private static DefaultThreadPool defaultThreadPool;

    private RemoteService() {
    }

    public static RemoteService getInstance() {
        if (remoteService == null) {
            remoteService = new RemoteService();
            defaultThreadPool = DefaultThreadPool.getInstance();
        }
        return remoteService;
    }

    public void invoke(Activity activity, RequestManager requestManager, String key, ArrayList<RequestParameter> parameters, RequestCallback callback, boolean update) {
        URLData data = UrlConfigManager.findURL(activity, key);
        if (data == null) return;

        if (data.mockClass != null) {//反射获取MockService类，获取本地模拟数据
            Log.e(TAG, "data=" + data);
            try {
                MockService mockService = (MockService) Class.forName(data.mockClass).newInstance();
                String response = mockService.getJsonData();
                Response weather = JSON.parseObject(response, Response.class);
                if (callback != null) {
                    if (weather.hasError) {
                        callback.onFail(weather.errorMessage);
                    } else {
                        callback.onSuccess(weather.result);
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {//没有MockClass值，网络请求获取
            if (update) data.expires = 0;//强制刷新
            HttpRequest request = requestManager.createRequest(data, parameters, callback);
            defaultThreadPool.execute(request);//线程池网络请求
        }
    }
}