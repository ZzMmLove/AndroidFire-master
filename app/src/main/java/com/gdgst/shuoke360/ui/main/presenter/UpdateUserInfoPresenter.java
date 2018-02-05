package com.gdgst.shuoke360.ui.main.presenter;

import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.UpdateUserInfoContract;
import com.gdgst.common.baserx.RxSubscriber;

/**
 *
 * Created by Administrator on 9/15 0015.
 */

public class UpdateUserInfoPresenter extends UpdateUserInfoContract.Presenter{
    @Override
    public void loadUpdateResult(String token, String accessToken, String body) {
        mRxManage.add(mModel.loadUpdateResult(token, accessToken, body)
        .subscribe(new RxSubscriber<Result<UpdateUserinfo>>(mContext, true) {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading("请稍等……");
            }

            @Override
            protected void _onNext(Result<UpdateUserinfo> result) {
                mView.returnUpdateResult(result);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
