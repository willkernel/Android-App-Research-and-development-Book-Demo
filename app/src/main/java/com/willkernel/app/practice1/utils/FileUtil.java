package com.willkernel.app.practice1.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by willkernel on 2017/7/12.
 * mail:willkerneljc@gmail.com
 */
public class FileUtil {
    private static final String TAG = "FileUtil";

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static long getSdSize() {
        long size = Environment.getExternalStorageDirectory().getTotalSpace();
        String str = Environment.getExternalStorageDirectory().getPath();
        StatFs statFs = new StatFs(str);
        long blockSize = statFs.getBlockSizeLong();
        Log.e(TAG, "size=" + size);
        Log.e(TAG, "getTotalBytes=" + statFs.getTotalBytes());
        Log.e(TAG, "blockSize*statFs.getAvailableBlocksLong()=" + size);
        return blockSize * statFs.getAvailableBlocksLong();
    }

    public static void saveObject(String path, Object object) {
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        File f = new File(path);
        try {
            outputStream = new FileOutputStream(f);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (objectOutputStream != null) objectOutputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Log.e(TAG, "save file=" + f.getAbsolutePath());
        Log.e(TAG, "save size=" + f.length());
    }

    public static Object restoreObject(String path) {
        File file = new File(path);
        if (!file.exists()) return null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        Object object = null;
        try {
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) fileInputStream.close();
                if (objectInputStream != null) objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "obj=" + object);
        return object;
    }
}