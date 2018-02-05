package com.gdgst.shuoke360.ui.main.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.gdgst.common.baserx.RxBus;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.shuoke360.ui.main.contract.LoginContract;
import com.gdgst.shuoke360.utils.ThreadPoolManager;
import com.gdgst.common.baserx.RxSubscriber;
import com.gdgst.common.commonutils.ToastUitl;

/**
 *
 * Created by Administrator on 9/12 0012.
 */

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void getUserData(String token, String mob, String password) {
        mRxManage.add(mModel.loadUserData(token, mob, password)
        .subscribe(new RxSubscriber<Result<User>>(mContext, true) {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading("正在登陆……");
            }

            @Override
            protected void _onNext(final Result<User> userResult) {
                //Log.e("TAG", "onNext_user:"+user.toString());
                if (userResult.getError_code() == 0){
                    if (userResult.getData() != null){
                        mView.returnUserData(userResult.getData());
                        mView.stopLoading();
                        RxBus.getInstance().post("access_token", userResult.getData().getAccess_token());
                    }
                    ThreadPoolManager.getmInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveUserInfo(userResult.getData());
                        }
                    });
                }else {
                    ToastUitl.show(userResult.getMessage(), 0);
                    mView.stopLoading();
                }
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
