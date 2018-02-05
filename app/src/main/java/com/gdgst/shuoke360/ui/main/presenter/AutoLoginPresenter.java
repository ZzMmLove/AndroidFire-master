package com.gdgst.shuoke360.ui.main.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.Avatar;
import com.gdgst.shuoke360.bean.CheckUpdate;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.shuoke360.ui.main.contract.AutoLoginContract;
import com.gdgst.shuoke360.utils.ThreadPoolManager;
import com.gdgst.common.baserx.RxSubscriber;
import com.gdgst.common.commonutils.ToastUitl;

/**
 * Created by Administrator on 9/14 0014.
 */

public class AutoLoginPresenter extends AutoLoginContract.Presenter {
    @Override
    public void getUserData(String token, String accessToken) {
        mRxManage.add(mModel.loadUserData(token, accessToken)
                .subscribe(new RxSubscriber<Result<User>>(mContext, false) {
                    @Override
                    protected void _onNext(final Result<User> userResult) {
                        mView.retunUserData(userResult.getData());
                        mView.stopLoading();
                        ThreadPoolManager.getmInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                saveUserInfo(userResult.getData());
                            }
                        });
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUitl.show(message, 0);
                    }
                }));
    }

    @Override
    public void getAvatarResult(String token, String accessToken, String base64) {
        mRxManage.add(mModel.loadAvatarResult(token, accessToken, base64)
        .subscribe(new RxSubscriber<Result<Avatar>>(mContext, false) {
            @Override
            protected void _onNext(Result<Avatar> avatarResult) {
                mView.returnAvatarResult(avatarResult.getData().getImg_url());
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    /**
     * 检查更新
     * @param versionCode
     */
    @Override
    public void getCheckUpdateResult(String token, String versionCode) {
        mRxManage.add(mModel.loadCheckUpdateResult(token, versionCode)
        .subscribe(new RxSubscriber<Result<CheckUpdate>>(mContext, false) {
            @Override
            protected void _onNext(Result<CheckUpdate> updateResult) {
                mView.retrunCheckUpdateResult(updateResult);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    private void saveUserInfo(User data) {
        SharedPreferences userInfo = mContext.getSharedPreferences(AppConstant.SHAREPREFRENECE_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("mob", data.getMob());
        editor.putString("email", data.getEmail());
        editor.putString("access_token", data.getAccess_token());
        editor.putString("nickname", data.getNickname());
        editor.putString("name", data.getName());
        editor.putString("avatar", data.getAvatar());
        editor.putInt("sex", data.getSex());
        editor.putString("addr", data.getAddr());
        editor.putString("school", data.getSchool());
        editor.putString("className", data.getClassName());
        editor.putString("remark", data.getRemark());
        editor.putString("type", data.getType());
        editor.commit();
    }

}
