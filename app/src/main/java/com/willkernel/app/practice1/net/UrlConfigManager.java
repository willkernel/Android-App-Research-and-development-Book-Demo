package com.willkernel.app.practice1.net;

import android.app.Activity;
import android.content.res.XmlResourceParser;

import com.willkernel.app.practice1.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */

class UrlConfigManager {
    private static List<URLData> urlDatas;

    static URLData findURL(Activity activity, String key) {
        if (urlDatas == null || urlDatas.isEmpty()) findURLFromXml(activity);
        for (URLData urlData : urlDatas) {
            if (key.equals(urlData.key))
                return urlData;
        }
        return null;
    }

    private static URLData findURLFromXml(Activity activity) {
        urlDatas=new ArrayList<>();
        XmlResourceParser xmlResourceParser = activity.getApplication().getResources().getXml(R.xml.url);
        try {
            int eventCode = xmlResourceParser.getEventType();
            while (eventCode != XmlPullParser.END_DOCUMENT) {
                switch (eventCode) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Node".equals(xmlResourceParser.getName())) {
                            URLData urlData = new URLData();
                            urlData.key = xmlResourceParser.getAttributeValue(null, "Key");
                            urlData.netType = xmlResourceParser.getAttributeValue(null, "NetType");
                            urlData.expires = Long.parseLong(xmlResourceParser.getAttributeValue(null, "Expires"));
                            urlData.url = xmlResourceParser.getAttributeValue(null, "Url");
                            urlData.mockClass = xmlResourceParser.getAttributeValue(null, "MockClass");
                            urlDatas.add(urlData);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventCode = xmlResourceParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static URLData findURLFromXml(Activity activity, String findKey) {
        XmlResourceParser xmlResourceParser = activity.getApplication().getResources().getXml(R.xml.url);
        try {
            int eventCode = xmlResourceParser.getEventType();
            while (eventCode != XmlPullParser.END_DOCUMENT) {
                switch (eventCode) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Node".equals(xmlResourceParser.getName())) {
                            final String key = xmlResourceParser.getAttributeValue(null, "Key");
                            if (key.trim().equals(findKey)) {
                                URLData urlData = new URLData();
                                urlData.key = key;
                                urlData.netType = xmlResourceParser.getAttributeValue(null, "NetType");
                                urlData.expires = Long.parseLong(xmlResourceParser.getAttributeValue(null, "Expires"));
                                urlData.url = xmlResourceParser.getAttributeValue(null, "Url");
                                return urlData;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventCode = xmlResourceParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}