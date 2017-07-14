package com.willkernel.app.practice1.net;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */
public class RemoteService {
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
        if (update) data.expires = 0;
        HttpRequest request = requestManager.createRequest(data, parameters, callback);
        defaultThreadPool.execute(request);
    }
}