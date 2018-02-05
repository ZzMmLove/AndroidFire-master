package com.gdgst.shuoke360.utils;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类
 * Created by Administrator on 7/17 0017.
 */
public class ThreadPoolManager {

    private static ThreadPoolProxy mInstance;

    public static ThreadPoolProxy getmInstance(){
        if (mInstance == null){
            synchronized (ThreadPoolManager.class){
                if (mInstance == null){
                    mInstance = new ThreadPoolProxy(2,5);
                }
            }
        }
        return mInstance;
    }


    /**
     * 线程池代理类
     */
    public static class ThreadPoolProxy{
        private ThreadPoolExecutor mThreadPoolExecutor;
        public ThreadPoolProxy(int corePoolSize, int maxPoolSize){
            initThreadPoolProxy(corePoolSize, maxPoolSize);
        }

        private void initThreadPoolProxy(int corePoolSize, int maxPoolSize) {
            if (mThreadPoolExecutor == null){
                //阻塞缓冲队列
                BlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<>();
                //线程工厂
                ThreadFactory threadFactory = Executors.defaultThreadFactory();
                RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
                mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 0, TimeUnit.MICROSECONDS, workQueue, threadFactory, handler);
            }
        }

        public void submit(Runnable task){
            if(mThreadPoolExecutor!=null){
                mThreadPoolExecutor.submit(task);
            }
        }

        public void execute(Runnable task){
            if(mThreadPoolExecutor!=null){
                mThreadPoolExecutor.execute(task);
            }
        }

        public void remove(Runnable task){
            if(mThreadPoolExecutor!=null){
                mThreadPoolExecutor.remove(task);
            }
        }
    }

}
