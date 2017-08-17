package com.willkernel.app.wklib.utils;

import android.content.res.Resources;

import com.willkernel.app.wklib.WKApp;

/**
 * Created by willkernel on 2017/8/17.
 * mail:willkerneljc@gmail.com
 */

public class UnitUtil {
    public static int dp2px(float dp) {
        return (int) (WKApp.getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    public static int px2dp(float px) {
        return (int) (px / WKApp.getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int sp2px(float sp) {
        return (int) (Resources.getSystem().getDisplayMetrics().scaledDensity * sp + 0.5);
    }

    public static int px2sp(float px) {
        final float scale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
