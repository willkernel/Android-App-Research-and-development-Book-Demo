package com.willkernel.app.practice1.net;

import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */

public class RequestManager {
    private CopyOnWriteArrayList<HttpRequest> requestList;
    private static final String TAG="RequestManager";
    public RequestManager() {
        requestList = new CopyOnWriteArrayList<>();
    }

    private void addRequest(HttpRequest request) {
        checkNotNull(requestList, "requestList cannot be null!");
        requestList.add(request);
    }

    public void cancelRequest() {
        if ((requestList != null) && (requestList.size() > 0)) {
            for (final HttpRequest request : requestList) {
                if (request.getRequest() != null) {
                    try {
                        request.getRequest().disconnect();
                        requestList.remove(request);
                        Log.e(TAG,"cancel");
                    } catch (final UnsupportedOperationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public HttpRequest createRequest(URLData data, RequestCallback callback) {
        return createRequest(data, null, callback);
    }

    public HttpRequest createRequest(URLData data, List<RequestParameter> list, RequestCallback callback) {
        HttpRequest httpRequest = new HttpRequest(data, list, callback);
        addRequest(httpRequest);
        return httpRequest;
    }
}