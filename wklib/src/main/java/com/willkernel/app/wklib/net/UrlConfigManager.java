package com.willkernel.app.wklib.net;

import android.app.Application;
import android.content.res.XmlResourceParser;

import com.willkernel.app.wklib.entity.URLData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */

public class UrlConfigManager {
    private static List<URLData> urlDatas;
    private static int xmlId;

    public static URLData findURL(Application application, String key) {
        if (xmlId == 0) return null;
        if (urlDatas == null || urlDatas.isEmpty()) findURLFromXml(application, xmlId);
        for (URLData urlData : urlDatas) {
            if (key.equals(urlData.key))
                return urlData;
        }
        return null;
    }

    public static List<URLData> findURLFromXml(Application application, int xml) {
        xmlId = xml;
        urlDatas = new ArrayList<>();
        XmlResourceParser xmlResourceParser = application.getResources().getXml(xml);
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
            return urlDatas;
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URLData findURLFromXml(Application application, String findKey) {
        if (xmlId == 0) return null;
        XmlResourceParser xmlResourceParser = application.getResources().getXml(xmlId);
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