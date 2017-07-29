package com.willkernel.app.wklib.utils;

import android.content.SharedPreferences;

/**
 * Created by willkernel on 2017/7/28.
 * mail:willkerneljc@gmail.com
 */

public abstract class WKPrefUtil {
    protected static WKPrefUtil user = null;
    protected static SharedPreferences sharedPreferences;
    protected static SharedPreferences.Editor editor;

    public boolean isLogin() {
        return sharedPreferences.getBoolean("login", false);
    }

    public void setLogin(boolean isLogin) {
        editor.putBoolean("login", isLogin).apply();
    }
}