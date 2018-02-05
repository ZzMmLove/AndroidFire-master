package com.gdgst.shuoke360.ui.main.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.bean.HttpResult;
import com.gdgst.shuoke360.bean.SearchBean;
import com.gdgst.shuoke360.ui.main.contract.SearchContract;
import com.gdgst.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 8/31 0031.
 */

public class SearchModel implements SearchContract.Model {
    @Override
    public Observable<List<SearchBean>> loadSearch(String taken, String searchKey,int page) {
        return Api.getDefault(1)
                .getSearchResult(taken, searchKey, page)
                .map(new Func1<HttpResult<SearchBean>, List<SearchBean>>() {
                    @Override
                    public List<SearchBean> call(HttpResult<SearchBean> searchBeanHttpResult) {
                        return searchBeanHttpResult.getData();
                    }
                })
                .compose(RxSchedulers.<List<SearchBean>>io_main());
    }
}
