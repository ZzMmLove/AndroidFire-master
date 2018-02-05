package com.gdgst.shuoke360.ui.news.contract;

import com.gdgst.shuoke360.bean.CopyBookGlide;
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


        Observable<List<CopyBookGlide>> getCopyBookGlide(String token, int page, String id, String type);
    }

    interface View extends BaseView {

        void returnCopyBookGlide(List<CopyBookGlide> copyBookGlides);
    }
    abstract static class Presenter extends BasePresenter<View, Model> {


        public abstract void getCopyBookGlideRequest(String token, int page, String id, String type);
    }
}
