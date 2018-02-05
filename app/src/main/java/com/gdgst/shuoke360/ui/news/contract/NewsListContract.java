package com.gdgst.shuoke360.ui.news.contract;

import com.gdgst.shuoke360.bean.CopyBook;
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

        Observable<List<CopyBook>> getCopyBookList(String token, int page, String type, String pid);
    }

    interface View extends BaseView {

        void returnCopyBookList(List<CopyBook> copyBooks);
        //返回顶部
        void scrolltoTop();
    }


    abstract static class Presenter extends BasePresenter<View, Model> {

        public abstract void getCopyBookList(String token, int page, String type, String pid);

    }
}
