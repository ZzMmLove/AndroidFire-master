package com.gdgst.shuoke360.ui.main.contract;

import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import rx.Observable;

/**
 * Created by Administrator on 9/12 0012.
 */

public interface LoginContract {

    interface Model extends BaseModel{
        Observable<Result<User>> loadUserData(String token, String mob, String password);
    }

    interface View extends BaseView{
        void returnUserData(User user);

    }

    abstract  static class Presenter extends BasePresenter<View, Model>{
        public abstract void getUserData(String token, String mob, String password);
    }
}
