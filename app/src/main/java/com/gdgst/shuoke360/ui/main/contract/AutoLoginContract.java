package com.gdgst.shuoke360.ui.main.contract;

import com.gdgst.shuoke360.bean.Avatar;
import com.gdgst.shuoke360.bean.CheckUpdate;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import rx.Observable;

/**
 * Created by Administrator on 9/14 0014.
 */

public interface AutoLoginContract {
    interface View extends BaseView{
        /**自动登录*/
        void retunUserData(User user);
        /**上传头像*/
        void returnAvatarResult(String imgUrl);
        /**检测更新*/
        void retrunCheckUpdateResult(Result<CheckUpdate> updateResult);
    }


    interface Model extends BaseModel{

        Observable<Result<User>> loadUserData(String token, String accessToken);

        Observable<Result<Avatar>> loadAvatarResult(String token, String accessToken, String base64 );

        Observable<Result<CheckUpdate>> loadCheckUpdateResult(String token, String versionCode);
    }


    abstract static class Presenter extends BasePresenter<View, Model>{

        public abstract void getUserData(String token, String accessToken);

        public abstract void getAvatarResult(String token, String accessToken, String base64);

        public abstract void getCheckUpdateResult(String token, String versionCode);
    }

}
