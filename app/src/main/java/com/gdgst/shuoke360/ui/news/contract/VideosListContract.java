package com.gdgst.shuoke360.ui.news.contract;

import com.gdgst.shuoke360.bean.Video;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * des:视频列表contract
 * Created by xsf
 * on 2016.09.14:38
 */
public interface VideosListContract {
    interface Model extends BaseModel {


        Observable<List<Video>> getvideolist (Map<String, String> params);
    }

    interface View extends BaseView {
        //返回获取的视频
        void returnVideosListData(List<Video> newsSummaries);
        //返回顶部
        void scrolltoTop();
    }
    abstract static class Presenter extends BasePresenter<View, Model> {

        public abstract void getvideo(Map<String, String> params);
    }
}
