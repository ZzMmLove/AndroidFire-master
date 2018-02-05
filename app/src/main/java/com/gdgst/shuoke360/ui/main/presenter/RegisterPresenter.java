package com.gdgst.shuoke360.ui.main.presenter;

import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.RegisterContract;
import com.gdgst.common.baserx.RxSubscriber;

/**
 * Created by Administrator on 9/19 0019.
 */

public class RegisterPresenter extends RegisterContract.Presenter {

    @Override
    public void getRegisterResult(String token, String mob, String password) {
        mRxManage.add(mModel.loadRegisterResult(token, mob, password)
        .subscribe(new RxSubscriber<Result<UpdateUserinfo>>(mContext, true) {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading("请稍等…");
            }

            @Override
            protected void _onNext(Result<UpdateUserinfo> result) {
                mView.returnRegisterResult(result);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
