package com.gdgst.shuoke360;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.gdgst.shuoke360.app.AppApplication;
import com.gdgst.shuoke360.utils.MyUtils;
import com.gdgst.common.baserx.RxBus;
import com.gdgst.common.security.AESUtil;

import java.io.IOException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    //RxBus rxBus = new RxBus();//这是错误的写法，因为RxBus是单例模式不同通过构造函数创建对象,其构造函数是私有的
    RxBus rxBus = RxBus.getInstance();

    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * AES加解密算法测试
     * @throws Exception
     */
    public void test01() throws Exception {
        String sSrc = "13456789abc";
        String sSre = "mhCijeZ8KS+Nxf4y8bhp3Q==";
        String result02 = AESUtil.Decrypt(sSre);
        String result = AESUtil.Encrypt(sSrc);
        Log.d("ApplicationTest", "加密结果="+result);
        Log.d("ApplicationTest", "解密结果="+result02);
    }

    public void test02() throws IOException {
        String token = "assets/json/category.json";
        String result = MyUtils.getJson(AppApplication.getAppContext(), token);
        Log.d("ApplicationTest", "返回结果="+result);
    }
}