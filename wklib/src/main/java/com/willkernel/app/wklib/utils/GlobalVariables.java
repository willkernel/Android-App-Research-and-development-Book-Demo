package com.willkernel.app.wklib.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.willkernel.app.wklib.cache.CacheManager;
import com.willkernel.app.wklib.entity.UserBean;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by willkernel on 2017/8/17.
 * mail:willkerneljc@gmail.com
 * <p>
 * <PRE>
 * private void writeObject(java.io.ObjectOutputStream out)
 * throws IOException
 * private void readObject(java.io.ObjectInputStream in)
 * throws IOException, ClassNotFoundException;
 * private void readObjectNoData()
 * throws ObjectStreamException;
 * </PRE>
 * <p>
 * 序列化对象在每次反序列化的时候会新建一个对象，不再是对原有对象的引用，需要在单例类中增加readResolve,readObject方法,并实现Cloneable接口
 */
public class GlobalVariables implements Parcelable, Cloneable {
//    private static final long serialVersionUID = 10L;
    private static final String TAG = "GlobalVariables";
    private static GlobalVariables instance;
    private UserBean user;

    private GlobalVariables() {
    }

    public static GlobalVariables getInstance() {
        if (instance == null) {
            Object object = FileUtil.restoreObject(CacheManager.cachePath + TAG);
            if (object == null) {//APP 首次启动，文件不存在，新建对象保存
                object = new GlobalVariables();
                FileUtil.saveObject(CacheManager.cachePath + TAG, object);
            }
            instance = (GlobalVariables) object;
        }
        return instance;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
        FileUtil.saveObject(CacheManager.cachePath + TAG, this);
    }

    public void reset() {
        user = null;
        FileUtil.saveObject(CacheManager.cachePath + TAG, this);
    }

    //-----------以下方法用于序列化-----------
    public GlobalVariables readResolve() throws CloneNotSupportedException {
        instance = (GlobalVariables) this.clone();
        return instance;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }

    public Object Clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
    }

    private GlobalVariables(Parcel in) {
        this.user = in.readParcelable(UserBean.class.getClassLoader());
    }

    public static final Creator<GlobalVariables> CREATOR = new Creator<GlobalVariables>() {
        @Override
        public GlobalVariables createFromParcel(Parcel source) {
            return new GlobalVariables(source);
        }

        @Override
        public GlobalVariables[] newArray(int size) {
            return new GlobalVariables[size];
        }
    };
}
