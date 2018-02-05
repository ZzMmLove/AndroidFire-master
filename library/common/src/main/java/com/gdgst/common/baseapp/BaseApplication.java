package com.gdgst.common.baseapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.gdgst.common.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * APPLICATION
 */
public class BaseApplication extends Application {

    private static BaseApplication baseApplication;
    /**屏幕的高度*/
    public static int screenH;
    /**广告轮播的图片库*/
    public static List<?> images;



    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        initBanner();
    }

    /**
     * 初始化广告轮播
     */
    private void initBanner() {
        screenH = getScreenHeight();
        //加载广告轮播的图片
        Integer[] urls = {R.drawable.gst_01,
                          R.drawable.gst_01,
                          R.drawable.gst_01};
       // String[] tips = getResources().getStringArray(R.array.title);
        List list = Arrays.asList(urls);
        images = new ArrayList<>(list);
        //titles= Arrays.asList(tips);

    }

    /**
     * 获取手机屏幕的高度
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = getAppContext().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;

    }

    /**
     * 获取屏幕的宽度
     * @return
     */
    public static int getScreanWidth(){
        DisplayMetrics displayMetrics = getAppContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static Context getAppContext() {
        return baseApplication;
    }
    public static Resources getAppResources() {
        return baseApplication.getResources();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 分包
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
