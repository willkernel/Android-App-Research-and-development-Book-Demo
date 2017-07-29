package com.willkernel.app.practice1;


import com.willkernel.app.wklib.WKApp;
import com.willkernel.app.wklib.net.UrlConfigManager;

/**
 * Created by willkernel on 2017/7/12.
 * mail:willkerneljc@gmail.com
 */

public class CApp extends WKApp {
    @Override
    public void onCreate() {
        super.onCreate();
        UrlConfigManager.findURLFromXml(this,R.xml.url);
    }
}