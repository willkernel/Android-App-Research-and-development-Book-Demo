package com.willkernel.app.practice1;

import android.app.Application;

import com.willkernel.app.practice1.net.CacheManager;

/**
 * Created by willkernel on 2017/7/12.
 * mail:willkerneljc@gmail.com
 */

public class CApp extends Application{
    private static CApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        CacheManager.getInstance().initCacheDir();
    }

    public static CApp getInstance() {
        return instance;
    }
}
