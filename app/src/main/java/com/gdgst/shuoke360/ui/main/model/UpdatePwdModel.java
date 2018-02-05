package com.gdgst.shuoke360.ui.main.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.UpdatePwdContract;
import com.gdgst.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Administrator on 9/16 0016.
 */

public class UpdatePwdModel implements UpdatePwdContract.Model{
    @Override
    public Observable<Result<UpdateUserinfo>> loadUpdatePwdResult(String token, String accessToken, String oldPwd, String newPwd) {
        return Api.getDefault(1)
                .getUpdatePassword(token, accessToken, oldPwd, newPwd)
                .compose(RxSchedulers.<Result<UpdateUserinfo>>io_main());
    }
}
