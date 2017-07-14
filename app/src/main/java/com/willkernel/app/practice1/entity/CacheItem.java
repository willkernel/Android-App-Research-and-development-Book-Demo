package com.willkernel.app.practice1.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by willkernel on 2017/7/12.
 * mail:willkerneljc@gmail.com
 */

public class CacheItem implements Parcelable,Serializable{

    public String key;
    public String data;
    public long expires;

    public CacheItem(String key, String result, long expires) {
        this.key = key;
        this.data = result;
        this.expires = System.currentTimeMillis() +expires;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.data);
        dest.writeLong(this.expires);
    }

    protected CacheItem(Parcel in) {
        this.key = in.readString();
        this.data = in.readString();
        this.expires = in.readLong();
    }

    public static final Creator<CacheItem> CREATOR = new Creator<CacheItem>() {
        @Override
        public CacheItem createFromParcel(Parcel source) {
            return new CacheItem(source);
        }

        @Override
        public CacheItem[] newArray(int size) {
            return new CacheItem[size];
        }
    };
}