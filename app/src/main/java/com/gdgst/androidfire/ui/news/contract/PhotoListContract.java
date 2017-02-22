package com.gdgst.androidfire.ui.news.contract;

import com.gdgst.androidfire.bean.PhotoGirl;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * des:图片列表contract
 * Created by xsf
 * on 2016.09.14:38
 */
public interface PhotoListContract {
    interface Model extends BaseModel {
        //请求获取图片
        Observable <List<PhotoGirl>> getPhotosListData(int size, int page);
    }

    interface View extends BaseView {
        //返回获取的图片
        void returnPhotosListData(List<PhotoGirl> photoGirls);
    }
    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取图片请求
        public abstract void getPhotosListDataRequest(int size, int page);
    }
}
