package com.willkernel.app.wklib.net;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */

public interface RequestCallback {
    void onSuccess(String content);
    void onFail(String errorMessage);
    void onCookieExpired();
}
