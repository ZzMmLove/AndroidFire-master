package com.gdgst.shuoke360.ui.news.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.api.HostType;
import com.gdgst.shuoke360.bean.HttpResult;
import com.gdgst.shuoke360.bean.MessageDetail;
import com.gdgst.shuoke360.bean.NewsDetail;
import com.gdgst.shuoke360.ui.news.contract.NewsDetailContract;
import com.gdgst.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * des:新闻详情
 * Created by xsf
 * on 2016.09.17:09
 */
public class NewsDetailModel implements NewsDetailContract.Model {
    @Override
    public Observable<List<NewsDetail>> getOneMessageData(String token, String type, String pid, String id) {
        return Api.getDefault(1).getMessageDetail(token, type, pid, id)
                .map(new Func1<HttpResult<NewsDetail>, List<NewsDetail>>() {
                    @Override
                    public List<NewsDetail> call(HttpResult<NewsDetail> newsDetailHttpResult) {
                        return newsDetailHttpResult.getData();
                    }
                }).compose(RxSchedulers.<List<NewsDetail>>io_main());
    }

    @Override
    public Observable<MessageDetail.DataBean.TopicBean> getOneMessageData02(String a, String tid, String perpage) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getMessageDetail(a, tid, perpage)
                .map(new Func1<MessageDetail, MessageDetail.DataBean.TopicBean>() {
                    @Override
                    public MessageDetail.DataBean.TopicBean call(MessageDetail messageData) {

                        return messageData.getData().getTopic();
                    }
                })
                .compose(RxSchedulers.< MessageDetail.DataBean.TopicBean>io_main());
    }


//    @Override
//    public Observable<NewsDetail> getOneNewsData(final String postId) {
//        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getNewDetail(Api.getCacheControl(),postId)
//                .map(new Func1<Map<String, NewsDetail>, NewsDetail>() {
//                    @Override
//                    public NewsDetail call(Map<String, NewsDetail> map) {
//                        NewsDetail newsDetail = map.get(postId);
//                        changeNewsDetail(newsDetail);
//                        return newsDetail;
//                    }
//                })
//                .compose(RxSchedulers.<NewsDetail>io_main());
//    }
//
//   @Override
//    public Observable<MessageDetail.DataBean.TopicBean> getOneMessageData(String a, final String tid, String perpage) {
//        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getMessageDetail(a, tid, perpage)
//                .map(new Func1<MessageDetail, MessageDetail.DataBean.TopicBean>() {
//                    @Override
//                    public MessageDetail.DataBean.TopicBean call(MessageDetail messageData) {
//
//                        return messageData.getData().getTopic();
//                    }
//                })
//                .compose(RxSchedulers.< MessageDetail.DataBean.TopicBean>io_main());
//    }
//
//   private void changeNewsDetail(NewsDetail newsDetail) {
//        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
//        if (isChange(imgSrcs)) {
//            String newsBody = newsDetail.getBody();
//            newsBody = changeNewsBody(imgSrcs, newsBody);
//            newsDetail.setBody(newsBody);
//        }
//    }
//
//    private boolean isChange(List<NewsDetail.ImgBean> imgSrcs) {
//        return imgSrcs != null && imgSrcs.size() >= 2;
//    }
//
//    private String changeNewsBody(List<NewsDetail.ImgBean> imgSrcs, String newsBody) {
//        for (int i = 0; i < imgSrcs.size(); i++) {
//            String oldChars = "<!--IMG#" + i + "-->";
//            String newChars;
//            if (i == 0) {
//                newChars = "";
//            } else {
//                newChars = "<img src=\"" + imgSrcs.get(i).getSrc() + "\" />";
//            }
//            newsBody = newsBody.replace(oldChars, newChars);
//
//        }
//        return newsBody;
//    }
}
