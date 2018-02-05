package com.gdgst.shuoke360.ui.main.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.UpdatePwdContract;
import com.gdgst.shuoke360.utils.ThreadPoolManager;
import com.gdgst.common.baserx.RxSubscriber;

/**
 * Created by Administrator on 9/16 0016.
 */

public class UpdatePwdPresenter extends UpdatePwdContract.Presenter {
    @Override
    public void loadUpdatePwdResult(String token, String accessToken, String oldPwd, String newPwd) {
        mRxManage.add(mModel.loadUpdatePwdResult(token, accessToken, oldPwd, newPwd)
                  .subscribe(new RxSubscriber<Result<UpdateUserinfo>>(mContext, true) {

                      @Override
                      public void onStart() {
                          super.onStart();
                          mView.showLoading("请稍等…");
                      }

                      @Override
                      protected void _onNext(Result<UpdateUserinfo> result) {
                          if (result.getError_code() == 0){
                              ThreadPoolManager.getmInstance().execute(new Runnable() {
                                  @Override
                                  public void run() {
                                      removeUserInfo();
                                  }
                              });
                              mView.returnUpdatePwdResult(result);
                              mView.stopLoading();
                          }
                      }

                      @Override
                      protected void _onError(String message) {
                          mView.showErrorTip(message);
                      }
                  }));
    }

    private void removeUserInfo() {
        SharedPreferences userInfo = mContext.getSharedPreferences(AppConstant.SHAREPREFRENECE_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.remove("mob");
        editor.remove("email");
        editor.remove("access_token");
        editor.remove("nickname");
        editor.remove("name");
        editor.remove("avatar");
        editor.remove("sex");
        editor.remove("addr");
        editor.remove("school");
        editor.remove("className");
        editor.remove("remark");
        editor.remove("type");
        editor.commit();
    }
}
