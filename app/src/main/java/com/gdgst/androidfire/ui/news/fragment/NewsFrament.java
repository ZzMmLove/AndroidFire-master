package com.gdgst.androidfire.ui.news.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.gdgst.androidfire.R;
import com.gdgst.androidfire.app.AppConstant;
import com.gdgst.androidfire.bean.MessageData;
import com.gdgst.androidfire.ui.news.adapter.NewListAdapter;
import com.gdgst.androidfire.ui.news.contract.NewsListContract;
import com.gdgst.androidfire.ui.news.model.NewsListModel;
import com.gdgst.androidfire.ui.news.presenter.NewsListPresenter;
import com.gdgst.common.base.BaseFragment;
import com.gdgst.common.commonwidget.LoadingTip;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * des:新闻fragment
 * Created by xsf
 * on 2016.09.17:30
 */
public class NewsFrament extends BaseFragment<NewsListPresenter, NewsListModel> implements NewsListContract.View, OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    private NewListAdapter newListAdapter;
    private List<MessageData.DataBean.ListBean> datas = new ArrayList<>();

    private String mNewsId;
    private String mNewsType;
    private int mStartPage=0;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private boolean isVisible;

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
        if (getArguments() != null) {
            mNewsId = getArguments().getString("newschanelId");

            mNewsType = getArguments().getString(AppConstant.NEWS_TYPE);
        }
        irc.setLayoutManager(new LinearLayoutManager(getContext()));
        newListAdapter = new NewListAdapter(getContext(), datas);
        newListAdapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(newListAdapter);
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);
        //数据为空才重新发起请求
        if(newListAdapter.getSize()<=0) {
            mStartPage = 0;
            mPresenter.getMessageDataRequest("forum.topic.list", mStartPage, "8","topic","new",mNewsId);
        }
    }


    @Override
    public void returnMessageData(List<MessageData.DataBean.ListBean> newsSummaries)  {
        if (newsSummaries != null) {
            mStartPage += 1;
            if (newListAdapter.getPageBean().isRefresh()) {
                irc.setRefreshing(false);
                newListAdapter.replaceAll(newsSummaries);
            } else {
                if (newsSummaries.size() > 0) {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    newListAdapter.addAll(newsSummaries);
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
        newListAdapter.getPageBean().setRefresh(true);
        mStartPage = 0;
        //发起请求
        irc.setRefreshing(true);
        mPresenter.getMessageDataRequest("forum.topic.list", mStartPage, "8","topic","new",mNewsId);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        newListAdapter.getPageBean().setRefresh(false);
        //发起请求
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getMessageDataRequest("forum.topic.list", mStartPage, "8","topic","new",mNewsId);
    }

    @Override
    public void showLoading(String title) {
        if( newListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        }
    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

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

}
