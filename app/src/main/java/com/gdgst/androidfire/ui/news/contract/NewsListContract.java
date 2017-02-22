package com.gdgst.androidfire.ui.news.contract;

import com.gdgst.androidfire.bean.MessageData;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * des:新闻列表contract
 * Created by xsf
 * on 2016.09.14:38
 */
public interface NewsListContract {
    interface Model extends BaseModel {
        //请求获取新闻
//        Observable <List<NewsSummary>> getNewsListData(String type, final String id, int startPage);

        //请求每日资讯
        Observable<List<MessageData.DataBean.ListBean>> getMessageData(String a, int page, String perpage, String type, String sort, String fid);
    }

    interface View extends BaseView {
        //返回获取的新闻
//        void returnNewsListData(List<NewsSummary> newsSummaries);
        void returnMessageData(List<MessageData.DataBean.ListBean> newsSummaries);
        //返回顶部
        void scrolltoTop();
    }
    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取新闻请求
//        public abstract void getNewsListDataRequest(String type, final String id, int startPage);

        public abstract void getMessageDataRequest( String a,int page,String perpage,String type,String sort,String fid);
    }
}
