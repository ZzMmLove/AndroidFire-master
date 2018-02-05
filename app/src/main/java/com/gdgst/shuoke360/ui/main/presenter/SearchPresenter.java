package com.gdgst.shuoke360.ui.main.presenter;

import com.gdgst.shuoke360.bean.SearchBean;
import com.gdgst.shuoke360.ui.main.contract.SearchContract;
import com.gdgst.common.baserx.RxSubscriber;

import java.util.List;

/**
 * Created by Administrator on 8/31 0031.
 */

public class SearchPresenter extends SearchContract.Presenter {

    @Override
    public void loadSearchResult(String token, String searchKey, int page) {
        mRxManage.add(mModel.loadSearch(token, searchKey, page)
        .subscribe(new RxSubscriber<List<SearchBean>>(mContext, false) {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading("请稍等…");
            }

            @Override
            protected void _onNext(List<SearchBean> searchBeen) {
                mView.returnSearchResult(searchBeen);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
