package com.gdgst.shuoke360.ui.main.model;


import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.shuoke360.ui.main.contract.LoginContract;
import com.gdgst.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Administrator on 9/12 0012.
 */

public class LoginModel implements LoginContract.Model {

    @Override
    public Observable<Result<User>> loadUserData(String token, String mob, String password) {
        return Api.getDefault(1)
                .getUserData(token, mob, password)
                .compose(RxSchedulers.<Result<User>>io_main());
    }
}
