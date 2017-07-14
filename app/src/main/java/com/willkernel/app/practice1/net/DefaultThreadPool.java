package com.willkernel.app.practice1.net;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by willkernel on 2017/7/11.
 * mail:willkerneljc@gmail.com
 */

class DefaultThreadPool {
    private static final int BLOCKING_QUEUE_SIZE = 20;
    private static final int THREAD_POOL_MAX_SIZE = 10;
    private static final int THREAD_POOL_SIZE = 6;

    /**
     * 缓冲BaseRequest任务队列
     */
    private static ArrayBlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(DefaultThreadPool.BLOCKING_QUEUE_SIZE);

    private static DefaultThreadPool instance = null;

    private static AbstractExecutorService pool = new ThreadPoolExecutor(DefaultThreadPool.THREAD_POOL_SIZE,
            DefaultThreadPool.THREAD_POOL_MAX_SIZE, 15L, TimeUnit.SECONDS,
            DefaultThreadPool.blockingQueue, new ThreadPoolExecutor.DiscardOldestPolicy());

    static synchronized DefaultThreadPool getInstance() {
        if (DefaultThreadPool.instance == null) {
            DefaultThreadPool.instance = new DefaultThreadPool();
        }
        return instance;
    }

    public void removeAllTasks() {
        blockingQueue.clear();
    }

    public void removeTask(final Runnable o) {
        blockingQueue.remove(o);
    }

    public void shutDown() {
        if (pool != null) pool.shutdown();
    }

    /**
     * 关闭，立即关闭，并挂起所有正在执行的线程，不接受新任务
     */
    public void shutdownRightnow() {
        if (DefaultThreadPool.pool != null) {
            DefaultThreadPool.pool.shutdownNow();
            try {
                // 设置超时极短，强制关闭所有任务
                DefaultThreadPool.pool.awaitTermination(1, TimeUnit.MICROSECONDS);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(final Runnable runnable) {
        if (runnable != null) {
            DefaultThreadPool.pool.execute(runnable);
        }
    }
}