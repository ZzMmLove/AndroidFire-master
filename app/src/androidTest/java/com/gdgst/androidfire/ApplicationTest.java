package com.gdgst.androidfire;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.gdgst.common.baserx.RxBus;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    //RxBus rxBus = new RxBus();//这是错误的写法，因为RxBus是单例模式不同通过构造函数创建对象,其构造函数是私有的
    RxBus rxBus = RxBus.getInstance();

    public ApplicationTest() {
        super(Application.class);
    }
}