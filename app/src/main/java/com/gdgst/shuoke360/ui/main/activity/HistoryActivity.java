package com.gdgst.shuoke360.ui.main.activity;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.History;
import com.gdgst.shuoke360.ui.main.adapter.HistoryAdapter;
import com.gdgst.shuoke360.ui.main.contract.HistoryContract;
import com.gdgst.shuoke360.ui.main.model.HistoryModel;
import com.gdgst.shuoke360.ui.main.presenter.HistoryPresenter;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonutils.ToastUitl;
import com.gdgst.common.commonwidget.LoadingTip;
import com.gdgst.common.commonwidget.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 *浏览记录页面
 * Created by Administrator on 9/21 0021.
 */

public class HistoryActivity extends BaseActivity<HistoryPresenter, HistoryModel> implements HistoryContract.View, OnRefreshListener, OnLoadMoreListener{
    /**显示列表*/
    @Bind(R.id.irc_list)
    IRecyclerView irc;
    /**回到顶部*/
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private HistoryAdapter historyAdapter;
    private List<History> datas;
    private SharedPreferences sharedPreferences;
    private String accessToken;
    private int page;
    private final String token = "gstshuoke360";

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        //设置沉浸式状态
        StatusBarCompat.translucentStatusBar(this);
        SetTranslanteBar();
        SetStatusBarColor();
        toolbar.setBackgroundResource(R.color.skyblue);
        toolbar.setTitle("浏览记录");
        //结束当前页面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sharedPreferences = getSharedPreferences(AppConstant.SHAREPREFRENECE_USER, MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", "");
        if (!accessToken.equals("")){
             irc.setLayoutManager(new LinearLayoutManager(this));
             datas = new ArrayList<>();
             historyAdapter = new HistoryAdapter(this, R.layout.nav_header_news, datas);
             historyAdapter.openLoadAnimation(new ScaleInAnimation());
             irc.setAdapter(historyAdapter);
             irc.setOnRefreshListener(this);
             irc.setOnLoadMoreListener(this);
             if (historyAdapter.getSize() <= 0){
                 page = 1;
                 mPresenter.loadHistoryResult(token, accessToken, page);
             }
        }else ToastUitl.show("您还未登录，请先登录…", 0);
    }

    @Override
    public void returnHistoryResult(List<History> historyList) {
        if (historyList != null){
            page += 1;
            if (historyAdapter.getPageBean().isRefresh()){
                irc.setRefreshing(false);
                historyAdapter.replaceAll(historyList);
            }else {
                if (historyList.size() > 0){
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    historyAdapter.addAll(historyList);
                }else {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }

    @Override
    public void scrolltoTop() {
        irc.smoothScrollToPosition(0);
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void onLoadMore(View loadMoreView) {
        historyAdapter.getPageBean().setRefresh(false);
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.loadHistoryResult(token, accessToken, page);
    }

    @Override
    public void onRefresh() {
        historyAdapter.getPageBean().setRefresh(true);
        page = 1;
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.loadHistoryResult(token, accessToken, page);
    }

}
