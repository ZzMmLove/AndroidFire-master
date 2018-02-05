package com.gdgst.shuoke360.ui.main.contract;

import com.gdgst.shuoke360.bean.Category;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import rx.Observable;

/**
 * Created by Administrator on 8/14 0014.
 */

public interface CategoryContract {

    interface Model extends BaseModel{
        Observable<Category> loadCategory(String fileName);
    }
    interface View extends BaseView{
        void returnCategory(Category category);
    }

    abstract static class Presenter extends BasePresenter<View, Model>{
        public abstract void loadCategoryResult(String fileName);
    }
}
