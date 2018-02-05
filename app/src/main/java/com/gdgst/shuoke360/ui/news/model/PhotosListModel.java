package com.gdgst.shuoke360.ui.news.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.bean.CopyBookGlide;
import com.gdgst.shuoke360.bean.HttpResult;
import com.gdgst.shuoke360.ui.news.contract.PhotoListContract;
import com.gdgst.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * des:图片
 * Created by xsf
 * on 2016.09.12:02
 */
public class PhotosListModel implements PhotoListContract.Model{

    @Override
    public Observable<List<CopyBookGlide>> getCopyBookGlide(String token, int page, String id, String type) {
        return Api.getDefault(1)
                .getCopyBookListGlide(Api.getCacheControl(),token, page, id, type)
                .map(new Func1<HttpResult<CopyBookGlide>, List<CopyBookGlide>>() {
                    @Override
                    public List<CopyBookGlide> call(HttpResult<CopyBookGlide> copyBookGlideHttpResult) {
                        return copyBookGlideHttpResult.getData();
                    }
                }).compose(RxSchedulers.<List<CopyBookGlide>> io_main());
    }
}
