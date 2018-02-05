package com.gdgst.shuoke360.ui.main.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.bean.History;
import com.gdgst.shuoke360.bean.HttpResult;
import com.gdgst.shuoke360.ui.main.contract.HistoryContract;
import com.gdgst.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 9/21 0021.
 */

public class HistoryModel implements HistoryContract.Model{
    @Override
    public Observable<List<History>> loadHistoryResult(String token, String accessToken, int page) {
        return Api.getDefault(1)
                .getHistoryResult(token, accessToken, page)
                .map(new Func1<HttpResult<History>, List<History>>() {
                    @Override
                    public List<History> call(HttpResult<History> historyHttpResult) {
                        return historyHttpResult.getData();
                    }
                })
                .compose(RxSchedulers.<List<History>>io_main());
    }
}
