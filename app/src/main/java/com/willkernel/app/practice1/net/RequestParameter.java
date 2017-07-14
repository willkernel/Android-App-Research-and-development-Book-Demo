package com.willkernel.app.practice1.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */

public class RequestParameter implements Comparable<Object>, Parcelable {
    public String name;
    public String value;

    public RequestParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.value);
    }

    protected RequestParameter(Parcel in) {
        this.name = in.readString();
        this.value = in.readString();
    }

    public static final Creator<RequestParameter> CREATOR = new Creator<RequestParameter>() {
        @Override
        public RequestParameter createFromParcel(Parcel source) {
            return new RequestParameter(source);
        }

        @Override
        public RequestParameter[] newArray(int size) {
            return new RequestParameter[size];
        }
    };


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestParameter that = (RequestParameter) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        RequestParameter requestParameter = (RequestParameter) o;
        int compared = name.compareTo(requestParameter.name);
        if (compared == 0) {
            compared = value.compareTo(requestParameter.value);
        }
        return compared;
    }
}