package com.gdgst.shuoke360.ui.news.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gdgst.common.baserx.RxBus;
import com.gdgst.common.baserx.RxManager;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.api.ApiConstants;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.Video;
import com.gdgst.shuoke360.ui.news.contract.VideosListContract;
import com.gdgst.shuoke360.ui.news.model.VideosListModel;
import com.gdgst.shuoke360.ui.news.presenter.VideoListPresenter;
import com.gdgst.common.base.BaseFragment;
import com.gdgst.common.commonutils.LogUtils;
import com.gdgst.common.commonwidget.LoadingTip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * des:视频fragment
 * Created by xsf
 * on 2016.09.17:30
 */
public class VideosFragment extends BaseFragment<VideoListPresenter, VideosListModel> implements VideosListContract.View, OnRefreshListener, OnLoadMoreListener {
    private final static String TAG = "VideoFragment";
    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.tv_nologin)
    TextView tvIslogin;
    private CommonRecycleViewAdapter<Video> videoListAdapter;

    private String mPid;
    private int mStartPage=1;
    private MyBroadcastRecevive recevive;
    private Map<String, String> params;
    private String accessToken;

    @Override
    protected int getLayoutResource() {
        return R.layout.framents_news;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }



    @Override
    protected void initView() {

       /* if (accessToken == null || accessToken.equals("")){
            tvIslogin.setVisibility(View.VISIBLE);
            irc.setVisibility(View.GONE);
        }else {
            tvIslogin.setVisibility(View.GONE);
            irc.setVisibility(View.VISIBLE);
        }*/
        params = new HashMap<>();
        params.put("token", "gstshuoke360");
        final String host = ApiConstants.SHUO_KE_HOST;
        if (getArguments() != null) {
            mPid = getArguments().getString(AppConstant.VIDEO_TYPE);
        }
        irc.setLayoutManager(new LinearLayoutManager(getContext()));
        videoListAdapter = new CommonRecycleViewAdapter<Video>(getContext(),R.layout.item_video_list) {
            @Override
            public void convert(ViewHolderHelper helper, Video videoData) {
                //helper.setImageRoundUrl(R.id.iv_logo,videoData.getTopicImg());
                //helper.setText(R.id.tv_from,videoData.getTime());
                // helper.setText(R.id.tv_play_time,String.format(getResources().getString(R.string.video_play_times), String.valueOf(videoData.getView_count())));

                JCVideoPlayerStandard jcVideoPlayerStandard = helper.getView(R.id.videoplayer);
                boolean setUp = jcVideoPlayerStandard.setUp(
                        host+videoData.getVideo_url(),        //视频的路径
                        JCVideoPlayer.SCREEN_LAYOUT_LIST,  //视频在屏幕的摆放类型
                        TextUtils.isEmpty(videoData.getVideo_url())?videoData.getName()+"":videoData.getName());  //视频的标题名字
                // LogUtils.logd(videoData.getName());
                if (setUp) {
                    //为视频设置一个默认的缩略图
                    Glide.with(mContext).load(host+videoData.getImg_url_s())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(com.gdgst.common.R.drawable.ic_empty_picture)
                            .crossFade().into(jcVideoPlayerStandard.thumbImageView);
                }
            }
        };
        irc.setAdapter(videoListAdapter);
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);
        //视频监听
        irc.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }
            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if (JCVideoPlayerManager.listener() != null) {
                    JCVideoPlayer videoPlayer = (JCVideoPlayer) JCVideoPlayerManager.listener();
                    if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                        JCVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });

        recevive = new MyBroadcastRecevive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstant.BROADCASTRECEIVE_ACTION);
        getContext().registerReceiver(recevive, intentFilter);

        //数据为空才重新发起请求
        if(videoListAdapter.getSize()<=0) {
            //发起请求
            mStartPage=1;
            params.put("page", String.valueOf(mStartPage));
            mPresenter.getvideo(params);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: 22222222222222222222222222222222222222");
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


    @Override
    public void returnVideosListData(List<Video> videoDatas) {
        if (videoDatas != null) {
            mStartPage += 1;
            params.put("page", String.valueOf(mStartPage));
            if (videoListAdapter.getPageBean().isRefresh()) {
                irc.setRefreshing(false);
                videoListAdapter.replaceAll(videoDatas);
            } else {
                if (videoDatas.size() > 0) {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    videoListAdapter.addAll(videoDatas);
                } else {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }

    /**
     * 返回顶部
     */
    @Override
    public void scrolltoTop() {
        irc.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        videoListAdapter.getPageBean().setRefresh(true);
        mStartPage = 1;
        //发起请求
        irc.setRefreshing(true);
        params.put("page", String.valueOf(mStartPage));
        mPresenter.getvideo(params);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        videoListAdapter.getPageBean().setRefresh(false);
        //发起请求
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getvideo(params);
    }

    @Override
    public void showLoading(String title) {
        if( videoListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        }
    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

    @Override
    public void showErrorTip(String msg) {
        if( videoListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
            loadedTip.setTips(msg);
            irc.setRefreshing(false);
        }else{
            irc.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    /**
     *广播接收器，用于接收从主页面发来的分类，接收到广播后立刻刷新页面
     */
    public  class MyBroadcastRecevive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppConstant.BROADCASTRECEIVE_ACTION.equals(intent.getAction())){
                String id = intent.getStringExtra("category");
                String type = intent.getStringExtra("type");
                if (type != null) params.put(type, id);
                mStartPage = 1;
                params.put("page", String.valueOf(mStartPage));
               // videoListAdapter.getPageBean().setRefresh(true);
                //发起请求
                //irc.setRefreshing(true);
//                Log.e("TAG", "==params=="+params.entrySet());
                mPresenter.getvideo(params);
                videoListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView: 2222222222222222222222222222222222222");
        if (JCVideoPlayer.backPress()){
             return;
         }
        super.onDestroyView();
        getContext().unregisterReceiver(recevive);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint: 22222222222222222222222222222222222222");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, "onHiddenChanged: 22222222222222222222222222222222222222");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: 22222222222222222222222222222222222222");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: 22222222222222222222222222222222");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: 22222222222222222222222222222222");
    }

}
