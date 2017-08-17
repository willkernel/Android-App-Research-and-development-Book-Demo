package com.willkernel.app.wklib.crash;

import android.content.Context;

import com.willkernel.app.wklib.R;
import com.willkernel.app.wklib.WKApp;
import com.willkernel.app.wklib.utils.FileUtil;
import com.willkernel.app.wklib.utils.ToastUtil;

/**
 * Created by willkernel on 2017/8/17.
 * mail:willkerneljc@gmail.com
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    public static final String APP_CACHE_PATH = (!FileUtil.isExternalStorageWritable() ?
            WKApp.getInstance().getCacheDir() : WKApp.getInstance().getExternalCacheDir()).getAbsolutePath() + "/crash/";
    private CrashHandler mDefaultHandler;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息,发送错误报告
     */
    private boolean handleException(Throwable e) {
        if (e == null)
            return false;
        sendCrashToServer(WKApp.getContext(), e);
        ToastUtil.shortShow(R.string.app_crash);
        saveCrashInfo(e);
        return true;
    }

    private void saveCrashInfo(Throwable e) {

    }

    private void sendCrashToServer(Context context, Throwable e) {

    }
}