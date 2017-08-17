package com.willkernel.app.wklib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by willkernel on 2017/8/17.
 * mail:willkerneljc@gmail.com
 */

public class UserBean implements Parcelable{
    public String name;
    public String phone;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
