package com.gdgst.androidfire.app;

import com.gdgst.androidfire.BuildConfig;
import com.gdgst.common.baseapp.BaseApplication;
import com.gdgst.common.commonutils.LogUtils;

/**
 * APPLICATION
 */
public class AppApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化logger
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
    }





}
