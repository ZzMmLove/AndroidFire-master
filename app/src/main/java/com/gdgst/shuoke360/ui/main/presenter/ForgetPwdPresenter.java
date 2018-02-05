package com.gdgst.shuoke360.ui.main.presenter;

import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.ForgetPwdContract;
import com.gdgst.common.baserx.RxSubscriber;

/**
 * Created by Administrator on 9/19 0019.
 */

public class ForgetPwdPresenter extends ForgetPwdContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
        mView.showLoading("请稍等…");
    }

    @Override
    public void getForgetPwdResult(String token, String mob, String password) {
        mRxManage.add(mModel.loadForgetPwdResult(token, mob, password)
        .subscribe(new RxSubscriber<Result<UpdateUserinfo>>(mContext, true) {
            @Override
            protected void _onNext(Result<UpdateUserinfo> result) {
                mView.returnForgetPwdResult(result);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
