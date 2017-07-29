package com.willkernel.app.wklib.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by willkernel on 2017/7/29.
 * mail:willkerneljc@gmail.com
 */

public class WKHandler {
    private final Handler.Callback mCallback;
    private final WKHandler.ExecHandler mExec;
    private Lock mLock = new ReentrantLock();
    final WKHandler.ChainedRef mRunnables;

    public WKHandler() {
        this.mRunnables = new WKHandler.ChainedRef(this.mLock, (Runnable) null);
        this.mCallback = null;
        this.mExec = new WKHandler.ExecHandler();
    }

    public WKHandler(@Nullable Handler.Callback callback) {
        this.mRunnables = new WKHandler.ChainedRef(this.mLock, (Runnable) null);
        this.mCallback = callback;
        this.mExec = new WKHandler.ExecHandler(new WeakReference(callback));
    }

    public WKHandler(@NonNull Looper looper) {
        this.mRunnables = new WKHandler.ChainedRef(this.mLock, (Runnable) null);
        this.mCallback = null;
        this.mExec = new WKHandler.ExecHandler(looper);
    }

    public WKHandler(@NonNull Looper looper, @NonNull Handler.Callback callback) {
        this.mRunnables = new WKHandler.ChainedRef(this.mLock, (Runnable) null);
        this.mCallback = callback;
        this.mExec = new WKHandler.ExecHandler(looper, new WeakReference(callback));
    }

    public final boolean post(@NonNull Runnable r) {
        return this.mExec.post(this.wrapRunnable(r));
    }

    public final boolean postAtTime(@NonNull Runnable r, long uptimeMillis) {
        return this.mExec.postAtTime(this.wrapRunnable(r), uptimeMillis);
    }

    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return this.mExec.postAtTime(this.wrapRunnable(r), token, uptimeMillis);
    }

    public final boolean postDelayed(Runnable r, long delayMillis) {
        return this.mExec.postDelayed(this.wrapRunnable(r), delayMillis);
    }

    public final boolean postAtFrontOfQueue(Runnable r) {
        return this.mExec.postAtFrontOfQueue(this.wrapRunnable(r));
    }

    public final void removeCallbacks(Runnable r) {
        WKHandler.WeakRunnable runnable = this.mRunnables.remove(r);
        if (runnable != null) {
            this.mExec.removeCallbacks(runnable);
        }

    }

    public final void removeCallbacks(Runnable r, Object token) {
        WKHandler.WeakRunnable runnable = this.mRunnables.remove(r);
        if (runnable != null) {
            this.mExec.removeCallbacks(runnable, token);
        }

    }

    public final boolean sendMessage(Message msg) {
        return this.mExec.sendMessage(msg);
    }

    public final boolean sendEmptyMessage(int what) {
        return this.mExec.sendEmptyMessage(what);
    }

    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return this.mExec.sendEmptyMessageDelayed(what, delayMillis);
    }

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return this.mExec.sendEmptyMessageAtTime(what, uptimeMillis);
    }

    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        return this.mExec.sendMessageDelayed(msg, delayMillis);
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return this.mExec.sendMessageAtTime(msg, uptimeMillis);
    }

    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        return this.mExec.sendMessageAtFrontOfQueue(msg);
    }

    public final void removeMessages(int what) {
        this.mExec.removeMessages(what);
    }

    public final void removeMessages(int what, Object object) {
        this.mExec.removeMessages(what, object);
    }

    public final void removeCallbacksAndMessages(Object token) {
        this.mExec.removeCallbacksAndMessages(token);
    }

    public final boolean hasMessages(int what) {
        return this.mExec.hasMessages(what);
    }

    public final boolean hasMessages(int what, Object object) {
        return this.mExec.hasMessages(what, object);
    }

    public final Looper getLooper() {
        return this.mExec.getLooper();
    }

    private WKHandler.WeakRunnable wrapRunnable(@NonNull Runnable r) {
        if (r == null) {
            throw new NullPointerException("Runnable can\'t be null");
        } else {
            WKHandler.ChainedRef hardRef = new WKHandler.ChainedRef(this.mLock, r);
            this.mRunnables.insertAfter(hardRef);
            return hardRef.wrapper;
        }
    }

    static class ChainedRef {
        @Nullable
        WKHandler.ChainedRef next;
        @Nullable
        WKHandler.ChainedRef prev;
        @NonNull
        final Runnable runnable;
        @NonNull
        final WKHandler.WeakRunnable wrapper;
        @NonNull
        Lock lock;

        public ChainedRef(@NonNull Lock lock, @NonNull Runnable r) {
            this.runnable = r;
            this.lock = lock;
            this.wrapper = new WKHandler.WeakRunnable(new WeakReference(r), new WeakReference(this));
        }

        public WKHandler.WeakRunnable remove() {
            this.lock.lock();

            try {
                if (this.prev != null) {
                    this.prev.next = this.next;
                }

                if (this.next != null) {
                    this.next.prev = this.prev;
                }

                this.prev = null;
                this.next = null;
            } finally {
                this.lock.unlock();
            }

            return this.wrapper;
        }

        public void insertAfter(@NonNull WKHandler.ChainedRef candidate) {
            this.lock.lock();

            try {
                if (this.next != null) {
                    this.next.prev = candidate;
                }

                candidate.next = this.next;
                this.next = candidate;
                candidate.prev = this;
            } finally {
                this.lock.unlock();
            }

        }

        @Nullable
        public WKHandler.WeakRunnable remove(Runnable obj) {
            this.lock.lock();

            try {
                for (WKHandler.ChainedRef curr = this.next; curr != null; curr = curr.next) {
                    if (curr.runnable == obj) {
                        return curr.remove();
                    }
                }
            } finally {
                this.lock.unlock();
            }
            return null;
        }
    }

    static class WeakRunnable implements Runnable {
        private final WeakReference<Runnable> mDelegate;
        private final WeakReference<ChainedRef> mReference;

        WeakRunnable(WeakReference<Runnable> delegate, WeakReference<ChainedRef> reference) {
            this.mDelegate = delegate;
            this.mReference = reference;
        }

        public void run() {
            Runnable delegate = (Runnable) this.mDelegate.get();
            WKHandler.ChainedRef reference = (WKHandler.ChainedRef) this.mReference.get();
            if (reference != null) {
                reference.remove();
            }

            if (delegate != null) {
                delegate.run();
            }

        }
    }

    private static class ExecHandler extends Handler {
        private final WeakReference<Callback> mCallback;

        ExecHandler() {
            this.mCallback = null;
        }

        ExecHandler(WeakReference<Callback> callback) {
            this.mCallback = callback;
        }

        ExecHandler(Looper looper) {
            super(looper);
            this.mCallback = null;
        }

        ExecHandler(Looper looper, WeakReference<Callback> callback) {
            super(looper);
            this.mCallback = callback;
        }

        public void handleMessage(@NonNull Message msg) {
            if (this.mCallback != null) {
                Callback callback = (Callback) this.mCallback.get();
                if (callback != null) {
                    callback.handleMessage(msg);
                }
            }
        }
    }
}
