package com.willkernel.app.practice1.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by willkernel on 2017/7/13.
 * mail:willkerneljc@gmail.com
 */

public class MD5Util {
    private static final String TAG = "MD5";

    /**
     * MD5 32
     */
    public static String md5(String params) {
        //md5
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(params.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            Log.e(TAG, "b=" + b);
            Log.e(TAG, "b & 0xFF  =" + (b & 0xFF));
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));// b=84 , b&OxFF=84, toHex(84)=54=5*16+4 > 54;
            Log.e(TAG,  "hex=" + hex+"\n\n");
        }
        return hex.toString().toUpperCase();
    }
}