package com.willkernel.app.practice1.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.willkernel.app.wklib.utils.WKPrefUtil;

/**
 * Created by willkernel on 2017/7/28.
 * mail:willkerneljc@gmail.com
 */

public class PreferenceUtil extends WKPrefUtil{
    @SuppressLint("CommitPrefEdits")
    public static WKPrefUtil getInstance(Context context) {
        if (user == null) {
            user = new PreferenceUtil(context);
        }
        return user;
    }

    private PreferenceUtil(Context context) {
        sharedPreferences = context.getSharedPreferences("WKPrefUtil", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}