package com.willkernel.app.practice1.net;

import android.util.Log;

/**
 * Created by willkernel on 2017/7/12.
 * mail:willkerneljc@gmail.com
 */

public abstract class AbstractRequestCallback implements RequestCallback{
    public abstract void onSuccess(String content);

    @Override
    public void onFail(String errorMessage) {
        Log.e("onFail",errorMessage);
    }

    @Override
    public void onCookieExpired() {

    }
}
