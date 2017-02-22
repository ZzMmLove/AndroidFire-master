package com.gdgst.androidfire.ui.main.model;

import com.gdgst.androidfire.app.AppApplication;
import com.gdgst.androidfire.app.AppConstant;
import com.gdgst.androidfire.bean.NewsChannelTable;
import com.gdgst.androidfire.db.NewsChannelTableManager;
import com.gdgst.androidfire.ui.main.contract.NewsMainContract;
import com.gdgst.common.baserx.RxSchedulers;
import com.gdgst.common.commonutils.ACache;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * des:
 * Created by xsf
 * on 2016.09.17:05
 *   ********************************
 *   * 在MVP模式中这个Model层的实现 *  (// FIXME: 2017/2/15 wrote by JenfeeMa)
 *   ********************************
 */
public class NewsMainModel implements NewsMainContract.Model {
    @Override
    public Observable<List<NewsChannelTable>> lodeMineNewsChannels() {

        return Observable.create(new Observable.OnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannelTable>> subscriber) {
                ArrayList<NewsChannelTable> arrayList_newsChannelTableList = (ArrayList<NewsChannelTable>) ACache.get(AppApplication.getAppContext()).getAsObject(AppConstant.CHANNEL_MINE);
               if(arrayList_newsChannelTableList == null){
                   arrayList_newsChannelTableList = (ArrayList<NewsChannelTable>) NewsChannelTableManager.loadNewsChannelsStatic();

                   Logger.i("main model:",arrayList_newsChannelTableList.get(0).getNewsChannelName());
                   ACache.get(AppApplication.getAppContext()).put(AppConstant.CHANNEL_MINE,arrayList_newsChannelTableList);

               }
                subscriber.onNext(arrayList_newsChannelTableList);
                subscriber.onCompleted();
            }
        }).compose(RxSchedulers.<List<NewsChannelTable>>io_main());
    }
}
