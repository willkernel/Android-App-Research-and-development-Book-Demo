package com.willkernel.app.wklib;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.willkernel.app.wklib.cache.CacheManager;

/**
 * Created by willkernel on 2017/7/29.
 * mail:willkerneljc@gmail.com
 */
public class WKApp extends MultiDexApplication {
    private static WKApp instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        CacheManager.getInstance().initCacheDir();
    }

    public static WKApp getInstance() {
        return instance;
    }
}
