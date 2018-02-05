package com.gdgst.shuoke360.ui.main.presenter;

import android.util.Log;

import com.gdgst.shuoke360.bean.Category;
import com.gdgst.shuoke360.ui.main.contract.CategoryContract;
import com.gdgst.common.baserx.RxSubscriber;

/**
 * Created by Administrator on 8/14 0014.
 */

public class CategoryPresenter extends CategoryContract.Presenter {

    @Override
    public void loadCategoryResult(String fileName) {
        mRxManage.add(mModel.loadCategory(fileName).subscribe(new RxSubscriber<Category>(mContext, false) {
            @Override
            protected void _onNext(Category category) {
                Log.e("Category", "-----------Pre"+category.toString());
                mView.returnCategory(category);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
