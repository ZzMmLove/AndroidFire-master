package com.gdgst.androidfire.ui.main.presenter;

import com.gdgst.androidfire.app.AppConstant;
import com.gdgst.androidfire.bean.NewsChannelTable;
import com.gdgst.androidfire.ui.main.contract.NewsMainContract;
import com.gdgst.common.baserx.RxSubscriber;

import java.util.List;

import rx.functions.Action1;

/**
 * des:
 * Created by xsf
 * on 2016.09.17:43
 * 在MVP模式中这个Presenter层的实现
 */
public class NewsMainPresenter extends NewsMainContract.Presenter{

    @Override
    public void onStart() {
        super.onStart();
        //监听新闻频道变化刷新
        mRxManage.on(AppConstant.NEWS_CHANNEL_CHANGED, new Action1<List<NewsChannelTable>>() {

            @Override
            public void call(List<NewsChannelTable> newsChannelTables) {
                if(newsChannelTables!=null){
                    mView.returnMineNewsChannels(newsChannelTables);
                }
            }
        });
    }

    @Override
    public void lodeMineChannelsRequest() {
        //执行网络请求
        mRxManage.add(mModel.lodeMineNewsChannels().subscribe(new RxSubscriber<List<NewsChannelTable>>(mContext,false) {
            @Override
            protected void _onNext(List<NewsChannelTable> newsChannelTables) {
                mView.returnMineNewsChannels(newsChannelTables);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
