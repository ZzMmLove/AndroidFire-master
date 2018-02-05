package com.gdgst.shuoke360.ui.main.contract;

import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import rx.Observable;

/**
 * Created by Administrator on 9/16 0016.
 */

public interface UpdatePwdContract {
    interface Model extends BaseModel{
        Observable<Result<UpdateUserinfo>> loadUpdatePwdResult(String token, String accessToken, String oldPwd, String newPwd);
    }
    interface View extends BaseView{
        void returnUpdatePwdResult(Result<UpdateUserinfo> result);
    }
    public abstract static class Presenter extends BasePresenter<View, Model>{
        public abstract void loadUpdatePwdResult(String token, String accessToken, String oldPwd, String newPwd);
    }
}
