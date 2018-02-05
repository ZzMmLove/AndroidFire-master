package com.gdgst.shuoke360.ui.news.presenter;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.bean.MessageDetail;
import com.gdgst.shuoke360.bean.NewsDetail;
import com.gdgst.shuoke360.ui.news.contract.NewsDetailContract;
import com.gdgst.common.baserx.RxSubscriber;
import com.gdgst.common.commonutils.ToastUitl;

import java.util.List;

/**
 * des:新闻详情
 * Created by xsf
 * on 2016.09.17:08
 */
public class NewsDetailPresenter extends NewsDetailContract.Presenter{
    @Override
    public void getOneMessageDataRequest(String token, String type, String pid, String id) {
        mRxManage.add(mModel.getOneMessageData(token, type, pid, id)
        .subscribe(new RxSubscriber<List<NewsDetail>>(mContext) {
            @Override
            protected void _onNext(List<NewsDetail> newsDetails) {
                mView.returnOneMessageData(newsDetails);
            }

            @Override
            protected void _onError(String message) {
                ToastUitl.showToastWithImg(message, R.drawable.ic_warm);
            }
        }));
    }

    @Override
    public void getOneMessageDataRequest02(String a,String tid,String perpage) {
      mRxManage.add(mModel.getOneMessageData02(a, tid, perpage)
      .subscribe(new RxSubscriber<MessageDetail.DataBean.TopicBean>(mContext) {
          @Override
          protected void _onNext(MessageDetail.DataBean.TopicBean topicBean) {
              mView.returnOneMessageData(topicBean);
          }

          @Override
          protected void _onError(String message) {
              ToastUitl.showToastWithImg(message, R.drawable.ic_warm);
          }
      }));
    }
}
