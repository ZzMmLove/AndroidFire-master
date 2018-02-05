package com.gdgst.shuoke360.ui.news.contract;

import com.gdgst.shuoke360.bean.MessageDetail;
import com.gdgst.shuoke360.bean.NewsDetail;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import java.util.List;

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

        Observable<List<NewsDetail>> getOneMessageData (String token, String type, String pid, String id);
        Observable<MessageDetail.DataBean.TopicBean> getOneMessageData02 (String a, String tid, String perpage);
    }

    interface View extends BaseView {
        //返回获取的新闻
//        void returnOneNewsData(NewsDetail newsDetail);

        void returnOneMessageData(MessageDetail.DataBean.TopicBean topicBean);

        void returnOneMessageData(List<NewsDetail> newsDetail);
    }
    abstract static class Presenter extends BasePresenter<View, Model> {

        //public abstract void getOneNewsDataRequest(String postId);
        public abstract  void getOneMessageDataRequest02(String a,String tid,String perpage);
        //发起获取单条新闻请求
        public abstract  void getOneMessageDataRequest(String token,String type,String pid, String id);


    }
}
