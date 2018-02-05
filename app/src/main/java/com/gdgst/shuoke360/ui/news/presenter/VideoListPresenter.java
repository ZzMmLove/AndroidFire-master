package com.gdgst.shuoke360.ui.news.presenter;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.Video;
import com.gdgst.shuoke360.ui.news.contract.VideosListContract;
import com.gdgst.common.baserx.RxSubscriber;

import java.util.List;
import java.util.Map;

import rx.functions.Action1;

/**
 * des:
 * Created by xsf
 * on 2016.09.14:53
 */
public class VideoListPresenter extends VideosListContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
        //监听返回顶部动作
       mRxManage.on(AppConstant.NEWS_LIST_TO_TOP, new Action1<Object>() {
           @Override
           public void call(Object o) {
            mView.scrolltoTop();
           }
       });
    }

    public void getvideo(Map<String, String> params){
        mRxManage.add(mModel.getvideolist(params)
                .subscribe(new RxSubscriber<List<Video>>(mContext,false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }
            //在这里把数据返回给Fragment进行视图更新
            @Override
            protected void _onNext(List<Video> videoDatas) {
                mView.returnVideosListData(videoDatas);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }


    }));

    }
}
