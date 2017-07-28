package com.willkernel.app.practice1.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by willkernel on 2017/7/28.
 * mail:willkerneljc@gmail.com
 */

public class User {
    private static User user = null;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public static User getInstance(Context context) {
        if (user == null) {
            user = new User(context);
        }
        return user;
    }

    private User(Context context) {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean("login", false);
    }

    public void setLogin(boolean isLogin) {
        editor.putBoolean("login", isLogin).apply();
    }
}