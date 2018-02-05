package com.gdgst.common.base;

import android.content.Context;

import com.gdgst.common.baserx.RxManager;

/**
 * des:基类presenter
 * Created by xsf
 * on 2016.07.11:55
 */
public abstract class BasePresenter<T,E>{
    public Context mContext;
    public T mView;
    public E mModel;
    public RxManager mRxManage = new RxManager();

    /**
     * 实质上把T类型变量和E类型变量赋值给T_mView,E_mModel
     * @param v
     * @param m
     */
    public void setVM(T v, E m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart(){

    }

    public void onDestroy() {
        mRxManage.clear();
    }
}
