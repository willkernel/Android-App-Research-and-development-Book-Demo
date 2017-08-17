package com.willkernel.app.wklib.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by willkernel on 2017/8/16.
 * mail:willkerneljc@gmail.com
 */

public class ProtocolManager {
    public static ProtocolData findProtocol(String findKey, Context context, int id) {
        ProtocolData findProtocol = null;
        XmlResourceParser xmlResourceParser = context.getResources().getXml(id);
        try {
            int eventCode = xmlResourceParser.getEventType();
            while (eventCode != XmlPullParser.END_DOCUMENT) {
                switch (eventCode) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Node".equals(xmlResourceParser.getName())) {
                            final String key = xmlResourceParser.getAttributeValue(null, "Key");
                            if (key.equals(findKey)) {
                                ProtocolData protocolData = new ProtocolData();
                                protocolData.key = key;
                                protocolData.target = xmlResourceParser.getAttributeValue(null, "Target");
                                findProtocol = protocolData;
                                break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventCode = xmlResourceParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return findProtocol;
    }
}