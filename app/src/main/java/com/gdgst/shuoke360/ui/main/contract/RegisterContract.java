package com.gdgst.shuoke360.ui.main.contract;

import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import rx.Observable;

/**
 * Created by Administrator on 9/19 0019.
 */

public interface RegisterContract {

    interface Model extends BaseModel {
        Observable<Result<UpdateUserinfo>> loadRegisterResult(String token, String mob, String password);
    }

    interface View extends BaseView {
        void returnRegisterResult(Result<UpdateUserinfo> result);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        public abstract void getRegisterResult(String token, String mob, String password);
    }
}
