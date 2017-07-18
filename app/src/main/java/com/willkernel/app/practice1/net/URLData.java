package com.willkernel.app.practice1.net;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */

public class URLData {
    public String key;
    public long expires;
    public String netType;
    public String url;
    public String mockClass;

    @Override
    public String toString() {
        return "URLData{" +
                "key='" + key + '\'' +
                ", expires=" + expires +
                ", netType='" + netType + '\'' +
                ", url='" + url + '\'' +
                ", mockClass='" + mockClass + '\'' +
                '}';
    }
}
