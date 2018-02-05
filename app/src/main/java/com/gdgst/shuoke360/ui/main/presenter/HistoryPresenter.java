package com.gdgst.shuoke360.ui.main.presenter;

import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.History;
import com.gdgst.shuoke360.ui.main.contract.HistoryContract;
import com.gdgst.common.baserx.RxSubscriber;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Administrator on 9/21 0021.
 */

public class HistoryPresenter extends HistoryContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
        mRxManage.on(AppConstant.NEWS_LIST_TO_TOP, new Action1<Object>() {
            @Override
            public void call(Object o) {
                mView.scrolltoTop();
            }
        });
    }

    @Override
    public void loadHistoryResult(String token, String accessToken, int page) {
        mRxManage.add(mModel.loadHistoryResult(token, accessToken, page)
        .subscribe(new RxSubscriber<List<History>>(mContext, true) {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading("请稍等…");
            }

            @Override
            protected void _onNext(List<History> historyList) {
                mView.returnHistoryResult(historyList);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
