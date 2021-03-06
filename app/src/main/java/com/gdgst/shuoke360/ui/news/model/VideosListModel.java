package com.gdgst.shuoke360.ui.news.model;

import com.gdgst.shuoke360.api.Api;
import com.gdgst.shuoke360.bean.HttpResult;
import com.gdgst.shuoke360.bean.Video;
import com.gdgst.shuoke360.ui.news.contract.VideosListContract;
import com.gdgst.common.baserx.RxSchedulers;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * des:视频列表model,发起网络请求视频， 通过Presenter层作为中介来与View层的VideosFragment进行数据交互
 * Created by xsf
 * on 2016.09.14:54
 */
public class VideosListModel implements VideosListContract.Model {
    @Override
    public Observable<List<Video>> getvideolist(Map<String, String> params) {
        return Api.getDefault(1)
                .getvideo(params)
                .map(new Func1<HttpResult, List<Video>>() {
                    @Override
                    public List<Video> call(HttpResult girlData) {
                        return girlData.getData();
                    }
                })
                //指定线程是在UI主线程
                .compose(RxSchedulers.<List<Video>> io_main());
    }


//    @Override
//    public Observable<List<VideoData>> getVideosListData(final String type, int startPage) {
//        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getVideoList(Api.getCacheControl(), type, startPage)
//                .flatMap(new Func1<Map<String, List<VideoData>>, Observable<VideoData>>() {
//                    @Override
//                    public Observable<VideoData> call(Map<String, List<VideoData>> map) {
//                        return Observable.from(map.get(type));
//                    }
//                })
//                //转化时间
//                .map(new Func1<VideoData, VideoData>() {
//                    @Override
//                    public VideoData call(VideoData videoData) {
//                        String ptime = TimeUtil.formatDate(videoData.getPtime());
//                        videoData.setPtime(ptime);
//                        return videoData;
//                    }
//                })
//                .distinct()//去重
//                .toSortedList(new Func2<VideoData, VideoData, Integer>() {
//                    @Override
//                    public Integer call(VideoData videoData, VideoData videoData2) {
//                        return videoData2.getPtime().compareTo(videoData.getPtime());
//                    }
//                })
//                //声明线程调度
//                .compose(RxSchedulers.<List<VideoData>>io_main());
//    }


//    public Observable<HttpResult<List<Video>>> getvideolist(final String desc_type, String category_id, int paged) {
//        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getvideo(desc_type, category_id, paged)
//                .flatMap(new Func1<Map<String, HttpResult<Video>>, Observable<Video>>() {
//                    @Override
//                    public Observable<Video> call(Map<String, HttpResult<Video>> stringHttpResultMap) {
//                        return null;
//                    }
//                })
//        //转化时间
//                .map(new Func1<Video, Video>() {
//                    @Override
//                    public Video call(Video videoData) {
//                        String ptime = TimeUtil.formatDate(videoData.getTime());
//                        videoData.setTime(ptime);
//                        return videoData;
//                    }
//                })
//                .distinct()//去重
//                .toSortedList(new Func2<Video, Video, Integer>() {
//                    @Override
//                    public Integer call(Video videoData, Video videoData2) {
//                        return videoData2.getTime().compareTo(videoData.getTime());
//                    }
//                })
//                //声明线程调度
//                .compose(RxSchedulers.<List<Video>>io_main());
//    }


}
