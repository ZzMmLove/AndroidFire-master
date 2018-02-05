package com.gdgst.shuoke360.ui.main.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.ForgetPwdContract;
import com.gdgst.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Administrator on 9/19 0019.
 */

public class ForgetPwdModel implements ForgetPwdContract.Model{
    @Override
    public Observable<Result<UpdateUserinfo>> loadForgetPwdResult(String token, String mob, String password) {
        return Api.getDefault(1)
                .findPassword(token, mob, password)
                .compose(RxSchedulers.<Result<UpdateUserinfo>>io_main());
    }
}
