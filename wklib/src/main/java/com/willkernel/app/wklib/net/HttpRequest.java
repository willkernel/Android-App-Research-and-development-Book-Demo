package com.willkernel.app.wklib.net;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.base.Strings;
import com.willkernel.app.wklib.cache.CacheManager;
import com.willkernel.app.wklib.entity.CacheItem;
import com.willkernel.app.wklib.entity.URLData;
import com.willkernel.app.wklib.net.response.Response;
import com.willkernel.app.wklib.utils.MD5Util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */
public class HttpRequest implements Runnable {
    private static final String TAG = "HttpRequest";
    private static final String COOKIES_HEADER = "Set-Cookie";
    private Handler mHandler;
    private RequestCallback requestCallback;
    private List<RequestParameter> parameters;
    private Response response = new Response();
    private URLData urlData;
    private HttpURLConnection httpURLConnection;
    private static long severAndClientTime;

    HttpRequest(URLData data, List<RequestParameter> parameters, RequestCallback callback) {
        urlData = data;
        this.parameters = parameters;
        requestCallback = callback;
        mHandler = new Handler();
    }

    @Override
    public void run() {
        try {
            Log.e(TAG, "urldata=" + urlData);
            urlData.url = sort(urlData.url);
            if (urlData.expires > 0) {
                final CacheItem cacheItem = CacheManager.getInstance().getFromCache(MD5Util.md5(urlData.url));
                Log.e(TAG, "cacheItem=" + cacheItem);
                if (cacheItem != null && requestCallback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "cache data=" + cacheItem.data);
                            requestCallback.onSuccess(cacheItem.data);
                            // com.alibaba.fastjson.JSONException: syntax error, expect {, actual pos 2, json
                            // 此错误抛出是因为gzip压缩导致 ,获取输入流采用new GZIPInputStream(httpURLConnection.getInputStream());
                        }
                    });
                    return;
                }
            }
            fetchByNet();
        } catch (IOException e) {
            e.printStackTrace();
            handleNetworkError("网络异常");
        }
    }

    private void fetchByNet() throws IOException {
        URL url = new URL(urlData.url);
        URLConnection urlConnection = url.openConnection();
        httpURLConnection = (HttpURLConnection) urlConnection;
        httpURLConnection.setRequestMethod(urlData.netType);
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setReadTimeout(20000);
        httpURLConnection.setDoInput(true);

        Log.e(TAG, "RequestProperties=" + httpURLConnection.getRequestProperties());
        setHeaders();

        //POST 不使用缓存
        httpURLConnection.setUseCaches(false);

        if (urlData.netType.equals("POST")) {
            httpURLConnection.setDoOutput(true);
        }
        for (RequestParameter parameter : parameters) {
            httpURLConnection.addRequestProperty(parameter.name, parameter.value);
        }

        avoidRedirect(url);
        avoidHack(url);
        setCookie();

        httpURLConnection.connect();
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            setSeverAndClientTime();
            InputStream inStrm;
//        content-encoding 带有gzip值，采用GZIPInputStream
            String contentEncoding = httpURLConnection.getContentEncoding();
            if (!Strings.isNullOrEmpty(contentEncoding) && contentEncoding.contains("gzip")) {
                inStrm = new GZIPInputStream(httpURLConnection.getInputStream());
                Log.e(TAG, "GZIPInputStream");
            } else {
                inStrm = new BufferedInputStream(httpURLConnection.getInputStream());
                Log.e(TAG, "BufferedInputStream");
            }
            try {
                Log.e(TAG, "contentEncoding=" + contentEncoding);
                Log.e(TAG, "code=" + httpURLConnection.getResponseCode());
                Log.e(TAG, "msg=" + httpURLConnection.getResponseMessage());
                Log.e(TAG, "HeaderFields=" + httpURLConnection.getHeaderFields());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //无需回调
            if (requestCallback == null) return;

            response.result = inputStreamToString(inStrm);
            if (response.errorType == -2) {
                requestCallback.onCookieExpired();
            } else {
                Log.e(TAG, "code=" + httpURLConnection.getResponseCode());
                if (TextUtils.isEmpty(response.result)) {
                    handleNetworkError("网络异常");
                } else {
                    response.errorMessage = "";
                    response.errorType = 0;
                    response.hasError = false;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "network result=" + response.result);
                            HttpRequest.this.requestCallback
                                    .onSuccess(response.result);
                        }
                    });
                    if (urlData.netType.equals("GET") && urlData.expires > 0) {
                        CacheManager.getInstance().putFileCache(urlData.url, response.result, urlData.expires);
                    }
                }
            }
        } else {
            Log.e(TAG, "code=" + httpURLConnection.getResponseCode());
        }
    }

    private void setHeaders() {
//        httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
        //浏览器通用属性 Http Header
//        httpURLConnection.setRequestProperty("Accept", "");
        httpURLConnection.setRequestProperty("Accept-Language", "en,zh");
        httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8,*");
//        httpURLConnection.setRequestProperty("referrer", "");
        httpURLConnection.setRequestProperty("User-Agent", "Android");
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");//压缩减小存储空间 传输时间，response 返回时会有content-encoding字段，带有gzip值，否则没有
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        // 设置客户端与服务连接类型
        httpURLConnection.addRequestProperty("Connection", "Keep-Alive");
    }

    private void setSeverAndClientTime() {
        String severDate = httpURLConnection.getHeaderField("Date");
        Log.e(TAG, "severDate=" + severDate);
        if (!Strings.isNullOrEmpty(severDate)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE,d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            try {
                Date date = simpleDateFormat.parse(severDate);
                severAndClientTime = date.getTime() + 8 * 60 * 60 * 1000 - System.currentTimeMillis();
                Log.e(TAG, "severAndClientTime=" + severAndClientTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static Date getSeverTime() {
        return new Date(System.currentTimeMillis() + severAndClientTime);
    }

    //    防止刷屏，同一IP访问，设置短时间内请求次数，输入验证码等操作
    //    https://stackoverflow.com/questions/9286861/get-ip-address-with-url-string-java
    private void avoidHack(URL url) {
        try {
            InetAddress address = InetAddress.getByName(url.getHost());
            String ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //防止重定向
    private void avoidRedirect(URL url) {
        try {
            if (!url.getHost().equals(httpURLConnection.getURL().getHost())) {
                // we were redirected! Kick the user out to the browser to sign on?
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    private void setCookie() {
        //        验证  Cookie
//        String userCredentials = "username:password";
//        String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
//        myURLConnection.setRequestProperty ("Authorization", basicAuth);
        saveCookie();
        restoreCookie();
    }

    private void saveCookie() {
//        https://developer.android.com/reference/java/net/HttpURLConnection.html
//        https://stackoverflow.com/questions/16150089/how-to-handle-cookies-in-httpurlconnection-using-cookiemanager

//        Authenticator.setDefault(new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("name", "password".toCharArray());
//            }
//        });
//        Unless paired with HTTPS, this is not a secure mechanism for user authentication. In particular, the username, password, request and response are all transmitted over the network without encryption.

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

//        By default, new instances of HttpCookie work only with servers that support RFC 2965 cookies. Many web servers support only the older specification, RFC 2109. For compatibility with the most web servers, set the cookie version to 0.
//
//        For example, to receive www.twitter.com in French:
//
//        HttpCookie cookie = new HttpCookie("lang", "fr");
//        cookie.setDomain("twitter.com");
//        cookie.setPath("/");
//        cookie.setVersion(0);
//        cookieManager.getCookieStore().add(new URI("http://twitter.com/"), cookie);

//        Get Cookies from response header and load them into cookieManager:
        Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
    }

    private void restoreCookie() {
        CookieManager cookieManager = new CookieManager();
        if (cookieManager.getCookieStore().getCookies().size() > 0) {
            httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
        }
    }

    private String sort(String url) {
        int index = url.indexOf("?");
        if (index > 0) {
            String url_pre = url.substring(0, index + 1);
            String params = url.substring(index + 1);
            if (params.indexOf("&") > 0) {
                String[] keyValue = params.split("&");
                Arrays.sort(keyValue);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < keyValue.length; i++) {
                    if (i < keyValue.length - 1) {
                        stringBuilder.append(keyValue[i]).append("&");
                    } else {
                        stringBuilder.append(keyValue[i]);
                    }
                }
                return url_pre + stringBuilder;
            }
        }
        Log.e(TAG, "sort url=" + url);
        return url;
    }

    private void handleNetworkError(final String errorMsg) {
        if (requestCallback == null) return;
        response.errorMessage = "error";
        response.errorType = -1;
        response.hasError = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpRequest.this.requestCallback.onFail(errorMsg);
            }
        });
    }

    private String inputStreamToString(final InputStream is)
            throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8096];
        int i;
        while ((i = is.read(buffer)) != -1) {
            baos.write(buffer, 0, i);
        }
        return baos.toString("UTF-8");
    }

    HttpURLConnection getRequest() {
        return httpURLConnection;
    }
}
