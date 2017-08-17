package com.willkernel.app.wklib.cache;


import android.util.Log;

import com.willkernel.app.wklib.WKApp;
import com.willkernel.app.wklib.entity.CacheItem;
import com.willkernel.app.wklib.utils.FileUtil;
import com.willkernel.app.wklib.utils.MD5Util;

import java.io.File;

/**
 * Created by willkernel on 2017/7/12.
 * mail:willkerneljc@gmail.com
 */
public class CacheManager {
    private static final String TAG = "CacheManager";
    private static final long SDCARD_MIN_SPACE = 1024 * 1024 * 10;
    private static CacheManager mInstance;
    @SuppressWarnings("ConstantConditions")
    public static final String cachePath = (!FileUtil.isExternalStorageWritable() ? WKApp.getInstance().getCacheDir() : WKApp.getInstance().getExternalCacheDir()).getAbsolutePath() + "/data/";
    private File cacheFile;

    public static CacheManager getInstance() {
        if (mInstance == null) {
            mInstance = new CacheManager();
        }
        return mInstance;
    }

    public void putFileCache(String url, String result, long expires) {
        String md5Key = MD5Util.md5(url);
        CacheItem cacheItem = new CacheItem(md5Key, result, expires);
        Log.e(TAG, "put2Cache  " + put2Cache(cacheItem));
    }

    private synchronized boolean put2Cache(CacheItem cacheItem) {
        if (FileUtil.getSdSize() > SDCARD_MIN_SPACE) {
            FileUtil.saveObject(cachePath + cacheItem.key, cacheItem);
            return true;
        }
        return false;
    }

    public synchronized CacheItem getFromCache(String key) {
        CacheItem findItem = (CacheItem) FileUtil.restoreObject(cachePath + key);
        if (findItem == null) return null;
        if (findItem.expires > System.currentTimeMillis()) return findItem;
        return findItem;
    }

    /**
     * 小于10M 清除缓存
     * 缓冲不存在创建
     */
    public void initCacheDir() {
        if (FileUtil.isExternalStorageWritable()) {
            if (FileUtil.getSdSize() < SDCARD_MIN_SPACE) {
                clearAllData();
            } else {
                File cacheDir = new File(cachePath);
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
            }
        }
    }

    void clearAllData() {
        File file = null;
        File[] files = null;
        if (FileUtil.isExternalStorageWritable()) {
            file = new File(cachePath);
            files = file.listFiles();
            for (File f : files) {
                //noinspection ResultOfMethodCallIgnored
                f.delete();
            }
        }
    }
}