package com.gdgst.androidfire.ui.news.presenter;

import com.gdgst.androidfire.R;
import com.gdgst.androidfire.bean.MessageDetail;
import com.gdgst.androidfire.ui.news.contract.NewsDetailContract;
import com.gdgst.common.baserx.RxSubscriber;
import com.gdgst.common.commonutils.ToastUitl;

/**
 * des:新闻详情
 * Created by xsf
 * on 2016.09.17:08
 */
public class NewsDetailPresenter extends NewsDetailContract.Presenter{
//    @Override
//    public void getOneNewsDataRequest(String postId) {
//        mRxManage.add(mModel.getOneNewsData(postId).subscribe(new RxSubscriber<NewsDetail>(mContext) {
//            @Override
//            protected void _onNext(NewsDetail newsDetail) {
//            mView.returnOneNewsData(newsDetail);
//            }
//
//            @Override
//            protected void _onError(String message) {
//                ToastUitl.showToastWithImg(message, R.drawable.ic_wrong);
//            }
//        }));
//    }

    @Override
    public void getOneMessageDataRequest(String a,String tid,String perpage) {
        mRxManage.add(mModel.getOneMessageData(a, tid, perpage).subscribe(new RxSubscriber<MessageDetail.DataBean.TopicBean>(mContext) {
            @Override
            protected void _onNext(MessageDetail.DataBean.TopicBean topicBean) {
                mView.returnOneMessageData(topicBean);
            }

            @Override
            protected void _onError(String message) {
                ToastUitl.showToastWithImg(message, R.drawable.ic_wrong);

            }
        }));
    }
}
