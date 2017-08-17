package com.willkernel.app.wklib.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by willkernel on 2017/8/17.
 * mail:willkerneljc@gmail.com
 */

public class Dispatcher {
    private static final String TAG = "Dispatcher";

    private static String getAndroidPageName(String key) {
        int pos = key.indexOf(",");
        if (pos == -1) {
            return key;
        } else {
            return key.substring(0, pos);
        }
    }

    public static void gotoAnyWhere2(Context context, Class clz, WebView webView, String url) {
        if (url.startsWith("gotoMovieDetail:")) {
            String strMovieId = url.substring(24);
            int movieId = Integer.valueOf(strMovieId);
            Intent intent = new Intent(context,
                    clz);
            intent.putExtra("movieId", movieId);
            context.startActivity(intent);
        } else if (url.startsWith("gotoNewsList:")) {
            Intent intent = new Intent(context, clz);
            context.startActivity(intent);
        } else if (url.startsWith("gotoPersonCenter")) {
            Intent intent = new Intent(context, clz);
            context.startActivity(intent);
        } else if (url.startsWith("gotoUrl:")) {
            String strUrl = url.substring(8);
            webView.loadUrl(strUrl);
        } else {
            String pageName = getAndroidPageName(url);
            if (TextUtils.isEmpty(pageName)) return;
            Intent intent = new Intent();
            int pos = url.indexOf(":");
            if (pos > 0) {
                String params = url.substring(pos);
                String[] pairs = params.split("&");
                for (String pair : pairs) {
                    String[] arr = pair.split("=");
                    String key = arr[0];
                    String value = arr[1];
                    if (value.startsWith("(int)")) {
                        intent.putExtra(key, Integer.valueOf(value.substring(5)));
                    } else if (value.startsWith("(Double)")) {
                        intent.putExtra(key, Integer.valueOf(value.substring(8)));
                    } else {
                        intent.putExtra(key, value);
                    }
                }
            }

            try {
                intent.setClass(context, Class.forName(pageName));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            context.startActivity(intent);
        }
    }

    public static void gotoAnyWhereByProtocol(Context context, String url, int id) {
        if (TextUtils.isEmpty(url)) return;
        String findKey = null;
        Intent intent = new Intent();
        int pos = url.indexOf(":");
        if (pos == -1) findKey = url;
        else {
            findKey = url.substring(0, pos);

            String strParams = url.substring(pos + 1);//书中代码没有pos+1
            String[] pairs = strParams.split("&");
            for (String strKeyAndValue : pairs) {
                String[] arr = strKeyAndValue.split("=");
                String key = arr[0];
                String value = arr[1];
                if (value.startsWith("(int)")) {
                    intent.putExtra(key, Integer.valueOf(value.substring(5)));
                } else if (value.startsWith("(Double)")) {
                    intent.putExtra(key, Double.valueOf(value.substring(8)));
                } else {
                    intent.putExtra(key, value);
                }
                Log.e(TAG, "getExtras=" + intent.getExtras());
            }
        }
        ProtocolData protocolData = ProtocolManager.findProtocol(findKey, context, id);
        try {
            intent.setClass(context, Class.forName(protocolData.target));
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
