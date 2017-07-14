package com.willkernel.app.practice1.net;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */

public interface RequestCallback {
    void onSuccess(String content);
    void onFail(String errorMessage);
}
