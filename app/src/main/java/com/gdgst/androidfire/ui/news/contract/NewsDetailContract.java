package com.gdgst.androidfire.ui.news.contract;

import com.gdgst.androidfire.bean.MessageDetail;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import rx.Observable;

/**
 * des:新闻详情contract
 * Created by xsf
 * on 2016.09.14:38
 */
public interface NewsDetailContract {
    interface Model extends BaseModel {
        //请求获取新闻
//        Observable <NewsDetail> getOneNewsData(String postId);

        Observable<MessageDetail.DataBean.TopicBean> getOneMessageData (String a,String tid,String perpage);
    }

    interface View extends BaseView {
        //返回获取的新闻
//        void returnOneNewsData(NewsDetail newsDetail);

        void returnOneMessageData(MessageDetail.DataBean.TopicBean topicBean);
    }
    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取单条新闻请求
//        public abstract void getOneNewsDataRequest(String postId);

        public abstract  void getOneMessageDataRequest(String a,String tid,String perpage);
    }
}
