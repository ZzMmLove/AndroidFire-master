package com.gdgst.shuoke360.ui.main.contract;

import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import rx.Observable;

/**
 * Created by Administrator on 9/15 0015.
 */

public interface UpdateUserInfoContract {

    interface Model extends BaseModel{
        Observable<Result<UpdateUserinfo>> loadUpdateResult(String token, String accessToken, String body);
    }
    interface View extends BaseView{
        void returnUpdateResult(Result<UpdateUserinfo> result);
    }
     abstract static class Presenter extends BasePresenter<View, Model>{
         public abstract void loadUpdateResult(String token, String accessToken, String body);
     }
}
