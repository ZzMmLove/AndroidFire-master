package com.gdgst.shuoke360.app;

import com.gdgst.shuoke360.BuildConfig;
import com.gdgst.common.baseapp.BaseApplication;
import com.gdgst.common.commonutils.LogUtils;
import com.mob.MobSDK;

/**
 * APPLICATION
 */
public class AppApplication extends BaseApplication {

    protected String a() {
        return null;
    }

    protected String b() {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化logger
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
        MobSDK.init(this, this.a(), this.b());
    }


}
