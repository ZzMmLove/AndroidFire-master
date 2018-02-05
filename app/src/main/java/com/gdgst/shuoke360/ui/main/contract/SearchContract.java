package com.gdgst.shuoke360.ui.main.contract;

import com.gdgst.shuoke360.bean.SearchBean;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * 搜索
 * Created by Administrator on 8/30 0030.
 */

public interface SearchContract {

    interface Model extends BaseModel{
        Observable<List<SearchBean>> loadSearch(String taken, String searchKey, int page);
    }

    interface View extends BaseView{
        void returnSearchResult(List<SearchBean> searchBean);
    }

    abstract class Presenter extends BasePresenter<View, Model>{
        public abstract void loadSearchResult(String token, String searchKey, int page);
    }
}
