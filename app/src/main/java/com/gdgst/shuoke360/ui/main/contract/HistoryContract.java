package com.gdgst.shuoke360.ui.main.contract;

import com.gdgst.shuoke360.bean.History;
import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 *
 * Created by Administrator on 9/21 0021.
 */

public interface HistoryContract {
    interface Model extends BaseModel{
        Observable<List<History>> loadHistoryResult(String token, String accessToken, int page);
    }

    interface View extends BaseView{
        void returnHistoryResult(List<History> historyList);
        void scrolltoTop();
    }

     abstract  class Presenter extends BasePresenter<View, Model>{
        public abstract void loadHistoryResult(String token, String accessToken, int page);
    }
}
