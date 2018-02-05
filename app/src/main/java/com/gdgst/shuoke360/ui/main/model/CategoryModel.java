package com.gdgst.shuoke360.ui.main.model;

import com.gdgst.shuoke360.app.AppApplication;
import com.gdgst.shuoke360.bean.Category;
import com.gdgst.shuoke360.ui.main.contract.CategoryContract;
import com.gdgst.shuoke360.utils.MyUtils;
import com.gdgst.common.baserx.RxSchedulers;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;

/**
 *
 * Created by Administrator on 8/14 0014.
 */

public class CategoryModel implements CategoryContract.Model {
    @Override
    public Observable<Category> loadCategory(final String fileName) {

        return Observable.create(new Observable.OnSubscribe<Category>() {
            @Override
            public void call(Subscriber<? super Category> subscriber) {
                String categoryJsonString = MyUtils.getJson(AppApplication.getAppContext(), fileName);
                Gson gson = new Gson();
                Category category = gson.fromJson(categoryJsonString, Category.class);
                subscriber.onNext(category);
                subscriber.onCompleted();
            }
        }).compose(RxSchedulers.<Category>io_main());
    }
}
