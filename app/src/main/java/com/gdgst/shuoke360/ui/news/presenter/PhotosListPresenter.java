package com.gdgst.shuoke360.ui.news.presenter;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.bean.CopyBookGlide;
import com.gdgst.shuoke360.ui.news.contract.PhotoListContract;
import com.gdgst.common.baserx.RxSubscriber;

import java.util.List;

/**
 * des:图片列表
 * Created by xsf
 * on 2016.09.12:01
 */
public class PhotosListPresenter extends PhotoListContract.Presenter{


    @Override
    public void getCopyBookGlideRequest(String token, int page, String id, String type) {
        mRxManage.add(mModel.getCopyBookGlide(token, page, id, type)
                .subscribe(new RxSubscriber<List<CopyBookGlide>>(mContext, false) {

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(List<CopyBookGlide> copyBookGlides) {
                mView.returnCopyBookGlide(copyBookGlides);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

}
