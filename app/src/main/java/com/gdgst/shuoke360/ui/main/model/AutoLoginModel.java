package com.gdgst.shuoke360.ui.main.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.bean.Avatar;
import com.gdgst.shuoke360.bean.CheckUpdate;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.shuoke360.ui.main.contract.AutoLoginContract;
import com.gdgst.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Administrator on 9/14 0014.
 */

public class AutoLoginModel implements AutoLoginContract.Model {
    @Override
    public Observable<Result<User>> loadUserData(String token, String accessToken) {
        return Api.getDefault(1)
                .getAutoLogin(token, accessToken)
                .compose(RxSchedulers.<Result<User>>io_main());
    }

    @Override
    public Observable<Result<Avatar>> loadAvatarResult(String token, String accessToken, String base64) {
        return Api.getDefault(1)
                .getAvatarResult(token, accessToken, base64)
                .compose(RxSchedulers.<Result<Avatar>>io_main());
    }

    /**
     * 检查更新
     * @param versionCode
     * @return
     */
    @Override
    public Observable<Result<CheckUpdate>> loadCheckUpdateResult(String token, String versionCode) {
        return Api.getDefault(1)
                .getCheckUpdateResult(token, versionCode)
                .compose(RxSchedulers.<Result<CheckUpdate>>io_main());
    }
}
