package com.gdgst.shuoke360.ui.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.CopyBook;
import com.gdgst.shuoke360.ui.main.view.GlideImageLoader;
import com.gdgst.shuoke360.ui.news.adapter.NewListAdapter;
import com.gdgst.shuoke360.ui.news.contract.NewsListContract;
import com.gdgst.shuoke360.ui.news.drawerlayout.adapter.RightSideslipLayAdapter;
import com.gdgst.shuoke360.ui.news.drawerlayout.ui.RightSideslipLay;
import com.gdgst.shuoke360.ui.news.model.NewsListModel;
import com.gdgst.shuoke360.ui.news.presenter.NewsListPresenter;
import com.gdgst.common.base.BaseFragment;
import com.gdgst.common.baseapp.BaseApplication;
import com.gdgst.common.commonwidget.LoadingTip;
import com.youth.banner.Banner;
import com.youth.banner.transformer.CubeOutTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * des:新闻fragment
 * Created by xsf
 * on 2016.09.17:30
 */
public class NewsFrament extends BaseFragment<NewsListPresenter, NewsListModel> implements NewsListContract.View, OnRefreshListener, OnLoadMoreListener {
    /**RecyclerView相当于ListView*/
    @Bind(R.id.irc)
    IRecyclerView irc;
    /**进度条*/
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    LinearLayout navigationView;
    @Bind(R.id.tvchoise)
    TextView tvChoise;

    private NewListAdapter newListAdapter;
    private List<CopyBook> datas = new ArrayList<>();
    private RightSideslipLay menuHeaderView;

    /**新闻分类的Id*/
    private String mNewsPid;
    private String mNewsType;
    private int mStartPage=1;
    private int channelIndex;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private boolean isVisible;

    //实现头部的广告轮播Banner机制,
    private Banner mBanner;
    private Bundle saveState;

    @Override
    protected int getLayoutResource() {
        return R.layout.framents_news;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // Log.e("TAG","onActivityCreated()");
    }

    @Override
    protected void initView() {
        //getArguments（）方法是当fragment被实例化的时候调，返回一个Bundle对象
        if (getArguments() != null) {
            mNewsType = getArguments().getString(AppConstant.NEWS_ID);
            //LogUtils.logi("新闻Type"+mNewsType,"NewsFrament");
            mNewsPid = getArguments().getString(AppConstant.NEWS_TYPE);
           // LogUtils.logi("新闻id"+mNewsPid,"NewsFrament");
            channelIndex = getArguments().getInt(AppConstant.CHANNEL_POSITION);
           // LogUtils.logi("新闻Index:"+channelIndex,"NewsFrament");
        }
        irc.setLayoutManager(new LinearLayoutManager(getContext()));

        newListAdapter = new NewListAdapter(getContext(), datas);
        //为每一个item设置一个动画
        newListAdapter.openLoadAnimation(new ScaleInAnimation());

        //头部广告轮播判断，为了防止fragment滑回来重复创建，这里只在第一个fragment中创建添加这个广告轮播
        if (mBanner == null && channelIndex == 0){
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.irecyclerview_header, null, false);
            mBanner = (Banner) headerView.findViewById(R.id.banner);
            mBanner.setLayoutParams(new IRecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BaseApplication.screenH/4));
            irc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            irc.addHeaderView(mBanner);
            //启动Banner
            mBanner.setImages(BaseApplication.images)
                    .setBannerAnimation(CubeOutTransformer.class)
                    .setImageLoader(new GlideImageLoader())
                    .start();
        }
        //筛选按钮
      /*  if (channelIndex == 1){
            tvChoise.setVisibility(View.VISIBLE);
            inflateDrawerLayout();
        }else tvChoise.setVisibility(View.GONE);*/

        irc.setAdapter(newListAdapter);
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);

        //数据为空才重新发起请求
        if(newListAdapter.getSize()<=0) {
            mStartPage = 1;
           // mPresenter.getMessageDataRequest("forum.topic.list", mStartPage, "8","topic","new",mNewsId);
           // Log.d("DeBug","0 从这里出发=");
            mPresenter.getCopyBookList("gstshuoke360", mStartPage, mNewsType, mNewsPid);
        }
    }

    private void inflateDrawerLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        menuHeaderView = new RightSideslipLay(getActivity());
        navigationView.addView(menuHeaderView);
        menuHeaderView.setCloseMenuCallBack(new RightSideslipLay.CloseMenuCallBack() {

            @Override
            public void getShowStr(RightSideslipLayAdapter rightSideslipLayAdapter) {
                for (int i = 0; i <= 2; i++) {
                    String str = rightSideslipLayAdapter.getData().get(i).getShowStr();
                    //Log.e("TAG", "-----fragment:>" + str);
                }
            }

            @Override
            public void setupCloseMean() {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }
        });
    }


    @OnClick(R.id.tvchoise)
    public void choise(){
        mDrawerLayout.openDrawer(GravityCompat.END);
    }


    @Override
    public void returnCopyBookList(List<CopyBook> copyBooks) {

        if (copyBooks != null){
            mStartPage += 1;
            newListAdapter.setValue(mNewsPid, mNewsType);
            if (newListAdapter.getPageBean().isRefresh()){
                irc.setRefreshing(false);
                newListAdapter.replaceAll(copyBooks);
            }else {
                if (copyBooks.size() > 0){
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    newListAdapter.addAll(copyBooks);
                }else {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }


    /**
     * 返回顶部,重写了NewsListContract的View的接口，相当于IView层
     */
    @Override
    public void scrolltoTop() {
        irc.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        newListAdapter.getPageBean().setRefresh(true);
        mStartPage = 1;
        //发起请求
        irc.setRefreshing(true);
        mPresenter.getCopyBookList("gstshuoke360", mStartPage, mNewsType, mNewsPid);
    }

    /**
     * 加载更多
     * @param loadMoreView
     */
    @Override
    public void onLoadMore(View loadMoreView) {
        newListAdapter.getPageBean().setRefresh(false);
        //发起请求
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        //mPresenter.getMessageDataRequest("forum.topic.list", mStartPage, "8","topic","new",mNewsId);
        mPresenter.getCopyBookList("gstshuoke360", mStartPage, mNewsType, mNewsPid);
    }

    /**
     * 显示加载进度圈
     * @param title
     */
    @Override
    public void showLoading(String title) {
        if( newListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        }
    }

    /**
     * 停止显示
     */
    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

    /**
     * 显示错误信息80 1000 2000000000 200yi * 100 20000yi rmb  500
     * @param msg
     */
    @Override
    public void showErrorTip(String msg) {
        if( newListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
            loadedTip.setTips(msg);
            irc.setRefreshing(false);
        }else{
            irc.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // Log.e("TAG","onDestroyView");
        saveStateToArgument();
    }

    private Bundle saveState(){
        Bundle state = new Bundle();
       // Log.e("TAG", "保存前："+mNewsType);
        state.putString(AppConstant.NEWS_ID, mNewsType);
        return  state;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        restoreStateFormArgument();
        newListAdapter.setValue(mNewsPid, mNewsType);
    }

    private void saveStateToArgument(){
        saveState = saveState();
        if (saveState != null){
            Bundle b = getArguments();
            b.putBundle(AppConstant.NEWS_ID, saveState);
        }
    }

    private void restoreState(){
        if (saveState != null){
            mNewsType = saveState.getString(AppConstant.NEWS_ID);
           // Log.e("TAG", "保存后："+mNewsType);
        }
    }

    private boolean restoreStateFormArgument(){
        Bundle b = getArguments();
        saveState = b.getBundle(AppConstant.NEWS_ID);
        if (saveState != null){
            restoreState();
            return true;
        }
        return false;
    }

}
