package com.gdgst.shuoke360.ui.news.presenter;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.CopyBook;
import com.gdgst.shuoke360.ui.news.contract.NewsListContract;
import com.gdgst.common.baserx.RxSubscriber;

import java.util.List;

import rx.functions.Action1;

/**
 * des:
 * Created by xsf
 * on 2016.09.14:53
 */
public class NewsListPresenter extends NewsListContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
        //监听返回顶部动作
       mRxManage.on(AppConstant.NEWS_LIST_TO_TOP, new Action1<Object>() {
           @Override
           public void call(Object o) {
            mView.scrolltoTop();
           }
       });
    }

//    /**
//     * 请求获取列表数据
//     * @param type
//     * @param id
//     * @param startPage
//     */
////    @Override
////    public void getNewsListDataRequest(String type, String id, int startPage) {
////         mRxManage.add(mModel.getNewsListData(type,id,startPage).subscribe(new RxSubscriber<List<NewsSummary>>(mContext,false) {
////             @Override
////             public void onStart() {
////                 super.onStart();
////                 mView.showLoading(mContext.getString(R.string.loading));
////             }
////
////             @Override
////             protected void _onNext(List<NewsSummary> newsSummaries) {
////                 mView.returnNewsListData(newsSummaries);
////                 mView.stopLoading();
////             }
////
////             @Override
////             protected void _onError(String message) {
////                 mView.showErrorTip(message);
////             }
////         }));
////    }


    /**
     * 发起请求获取常用碑帖的列表
     * @param token
     * @param page
     */
    @Override
    public void getCopyBookList(String token, int page, String type, String pid){
        mRxManage.add(mModel.getCopyBookList(token, page, type, pid)
        .subscribe(new RxSubscriber<List<CopyBook>>(mContext, false) {

            @Override
            public void onStart() {
                super.onStart();
                //Log.d("DeBug", "===onStart===");
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(List<CopyBook> copyBooks) {
                //Log.d("DeBug", "===_onNext===");

                mView.returnCopyBookList(copyBooks);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }


}
