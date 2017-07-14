package com.willkernel.app.practice1.net;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.willkernel.app.practice1.entity.CacheItem;
import com.willkernel.app.practice1.net.response.Response;
import com.willkernel.app.practice1.utils.MD5Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */
public class HttpRequest implements Runnable {
    private static final String TAG = "HttpRequest";
    private Handler mHandler;
    private RequestCallback requestCallback;
    private List<RequestParameter> parameters;
    private Response response = new Response();
    private URLData urlData;
    private HttpURLConnection httpURLConnection;

    HttpRequest(URLData data, List<RequestParameter> parameters, RequestCallback callback) {
        urlData = data;
        this.parameters = parameters;
        requestCallback = callback;
        mHandler = new Handler();
    }

    @Override
    public void run() {
        try {
            Log.e(TAG, "urldata=" + urlData);
            urlData.url = sort(urlData.url);
            if (urlData.expires > 0) {
                CacheItem cacheItem = CacheManager.getInstance().getFromCache(MD5Util.md5(urlData.url));
                Log.e(TAG, "cacheItem=" + cacheItem);
                if (cacheItem != null && requestCallback != null) {
                    requestCallback.onSuccess(cacheItem.data);
                    Log.e(TAG, "cache=" + cacheItem.data);
                    return;
                }
            }
            fetchByNet();
        } catch (IOException e) {
            e.printStackTrace();
            handleNetworkError("网络异常");
        }
    }

    private void fetchByNet() throws IOException {
        URL url = new URL(urlData.url);
        URLConnection urlConnection = url.openConnection();
        httpURLConnection = (HttpURLConnection) urlConnection;
        httpURLConnection.setRequestMethod(urlData.netType);
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setReadTimeout(20000);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
        //POST 不使用缓存
        httpURLConnection.setUseCaches(false);
        if (urlData.netType.equals("POST")) {
            httpURLConnection.setDoOutput(true);
        }
        for (RequestParameter parameter : parameters) {
            httpURLConnection.addRequestProperty(parameter.name, parameter.value);
        }
        InputStream inStrm = httpURLConnection.getInputStream();
        //无需回调
        if (requestCallback == null) return;

        response.result = inputStreamToString(inStrm);
        if (TextUtils.isEmpty(response.result)) {
            handleNetworkError("网络异常");
        } else {
            response.errorMessage = "";
            response.errorType = 0;
            response.hasError = false;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    HttpRequest.this.requestCallback
                            .onSuccess(response.result);
                    Log.e(TAG, "network=" + response.result);
                }
            });
            if (urlData.netType.equals("GET") && urlData.expires > 0) {
                CacheManager.getInstance().putFileCache(urlData.url, response.result, urlData.expires);
            }
        }
    }

    private String sort(String url) {
        int index = url.indexOf("?");
        if (index > 0) {
            String url_pre = url.substring(0, index + 1);
            String params = url.substring(index + 1);
            if (params.indexOf("&") > 0) {
                String[] keyValue = params.split("&");
                Arrays.sort(keyValue);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < keyValue.length; i++) {
                    if (i < keyValue.length - 1) {
                        stringBuilder.append(keyValue[i]).append("&");
                    } else {
                        stringBuilder.append(keyValue[i]);
                    }
                }
                return url_pre + stringBuilder;
            }
        }
        Log.e(TAG, "sort url=" + url);
        return url;
    }

    private void handleNetworkError(final String errorMsg) {
        if (requestCallback == null) return;
        response.errorMessage = "error";
        response.errorType = -1;
        response.hasError = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpRequest.this.requestCallback.onFail(errorMsg);
            }
        });
    }

    private String inputStreamToString(final InputStream is)
            throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8096];
        int i;
        while ((i = is.read(buffer)) != -1) {
            baos.write(buffer, 0, i);
        }
        return baos.toString("UTF-8");
    }

    HttpURLConnection getRequest() {
        return httpURLConnection;
    }
}
