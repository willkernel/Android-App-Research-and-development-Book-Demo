package com.willkernel.app.wklib.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        long blockSize = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
        }
        Log.e(TAG, "size=" + size);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG, "getTotalBytes=" + statFs.getTotalBytes());
        }
        Log.e(TAG, "blockSize*statFs.getAvailableBlocksLong()=" + size);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return blockSize * statFs.getAvailableBlocksLong();
        } else {
            return Environment.getExternalStorageDirectory().getFreeSpace();
        }
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

    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(fileName));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}