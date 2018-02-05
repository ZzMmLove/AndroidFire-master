package com.gdgst.shuoke360.ui.main.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.UpdateUserInfoContract;
import com.gdgst.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Administrator on 9/15 0015.
 */

public class UpdateUserInfoModel implements UpdateUserInfoContract.Model {
    @Override
    public Observable<Result<UpdateUserinfo>> loadUpdateResult(String token, String accessToken, String body) {
        return Api.getDefault(1)
                .getUpdateUserInfo(token, accessToken, body)
                .compose(RxSchedulers.<Result<UpdateUserinfo>>io_main());
    }
}
