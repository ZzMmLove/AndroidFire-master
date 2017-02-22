package com.gdgst.androidfire.ui.news.model;

import com.gdgst.androidfire.api.Api;
import com.gdgst.androidfire.api.HostType;
import com.gdgst.androidfire.bean.MessageData;
import com.gdgst.androidfire.ui.news.contract.NewsListContract;
import com.gdgst.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * des:新闻列表model
 * Created by xsf
 * on 2016.09.14:54
 */
public class NewsListModel implements NewsListContract.Model {

    @Override
    public Observable<List<MessageData.DataBean.ListBean>> getMessageData(String a, int page, String perpage, String type, String sort, String fid) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO)
                .getMessage(a, page,perpage, type, sort, fid)
                .map(new Func1<MessageData, List<MessageData.DataBean.ListBean>>() {
                    @Override
                    public List<MessageData.DataBean.ListBean> call(MessageData messageData) {
                        return messageData.getData().getList();
                    }
                })
                .compose(RxSchedulers.<List<MessageData.DataBean.ListBean>>io_main());

    }

//    /**
//     * 获取新闻列表
//     * @param type
//     * @param id
//     * @param startPage
//     * @return
//     */
//    @Override
//    public Observable<List<NewsSummary>> getNewsListData(final String type, final String id, final int startPage) {
//       return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getNewsList(Api.getCacheControl(),type, id, startPage)
//                .flatMap(new Func1<Map<String, List<NewsSummary>>, Observable<NewsSummary>>() {
//                    @Override
//                    public Observable<NewsSummary> call(Map<String, List<NewsSummary>> map) {
//                        if (id.endsWith(ApiConstants.HOUSE_ID)) {
//                            // 房产实际上针对地区的它的id与返回key不同
//                            return Observable.from(map.get("北京"));
//                        }
//                        return Observable.from(map.get(id));
//                    }
//                })
//                //转化时间
//                .map(new Func1<NewsSummary, NewsSummary>() {
//                    @Override
//                    public NewsSummary call(NewsSummary newsSummary) {
//                        String ptime = TimeUtil.formatDate(newsSummary.getPtime());
//                        newsSummary.setPtime(ptime);
//                        return newsSummary;
//                    }
//                })
//                .distinct()//去重
//                .toSortedList(new Func2<NewsSummary, NewsSummary, Integer>() {
//                    @Override
//                    public Integer call(NewsSummary newsSummary, NewsSummary newsSummary2) {
//                        return newsSummary2.getPtime().compareTo(newsSummary.getPtime());
//                    }
//                })
//                //声明线程调度
//                .compose(RxSchedulers.<List<NewsSummary>>io_main());
//    }


}
