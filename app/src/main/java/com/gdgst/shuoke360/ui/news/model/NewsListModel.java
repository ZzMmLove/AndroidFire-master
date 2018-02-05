package com.gdgst.shuoke360.ui.news.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.api.ApiService;
import com.gdgst.shuoke360.api.HostType;
import com.gdgst.shuoke360.bean.CopyBook;
import com.gdgst.shuoke360.bean.HttpResult;
import com.gdgst.shuoke360.ui.news.contract.NewsListContract;
import com.gdgst.common.baserx.RxSchedulers;


import java.util.List;


import rx.Observable;

import rx.functions.Func1;


/**
 * des:新闻列表model   层是真正去发起请求获取数据局
 * Created by xsf
 * on 2016.09.14:54
 */
public class NewsListModel implements NewsListContract.Model {

    /**
     * 在Model层发起请求从网络上、数据库的数据源过去数据
     * @param token
     * @param page
     * @return
     */
   @Override
    public Observable<List<CopyBook>> getCopyBookList(String token, int page, String type, String pid) {
       ApiService aDefault = Api.getDefault(HostType.NETEASE_NEWS_VIDEO);
       //Log.d("DeBug","1 已经执行到这里");
       Observable<HttpResult<CopyBook>> copyBookList = aDefault.getCopyBookList(token, page, type, pid);
      // Log.d("DeBug","2 已经执行到这里");
       Observable<List<CopyBook>> newsListModel = copyBookList
               .map(new Func1<HttpResult, List<CopyBook>>() {
           @Override
           public List<CopyBook> call(HttpResult httpResult) {
              // Log.d("DeBug","3 已经执行到这里");
              // Log.d("DeBug","返回数据===》"+httpResult.getData().toString());

               return httpResult.getData();
           }
       })
               .compose(RxSchedulers.<List<CopyBook>>io_main());
      // Log.d("DeBug","4 已经执行到这里");
       return newsListModel;
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
