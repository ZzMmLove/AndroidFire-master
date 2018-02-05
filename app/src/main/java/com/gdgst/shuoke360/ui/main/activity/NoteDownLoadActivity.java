package com.gdgst.shuoke360.ui.main.activity;

import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonwidget.LoadingTip;
import com.gdgst.common.commonwidget.StatusBarCompat;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.NoteDownload;
import com.gdgst.shuoke360.ui.main.adapter.HistoryAdapter;
import com.gdgst.shuoke360.ui.main.adapter.NoteDownloadAdapter;
import com.gdgst.shuoke360.ui.main.contract.NoteDownloadContrct;
import com.gdgst.shuoke360.ui.main.model.NoteDownloadModel;
import com.gdgst.shuoke360.ui.main.presenter.NoteDownloadPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 *
 * Created by Administrator on 10/27 0027.
 */

public class NoteDownLoadActivity extends BaseActivity<NoteDownloadPresenter, NoteDownloadModel> implements NoteDownloadContrct.View, OnRefreshListener {

    /**显示列表*/
    @Bind(R.id.irc_list)
    IRecyclerView irc;
    /**回到顶部*/
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private List<NoteDownload> datas;
    private NoteDownloadAdapter noteDownloadAdapter;
    String outputFile;


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
        toolbar.setTitle("讲义下载");
        //结束当前页面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        irc.setLayoutManager(new LinearLayoutManager(this));
        datas = new ArrayList<>();
        noteDownloadAdapter = new NoteDownloadAdapter(this, R.layout.nav_header_news, datas);
        noteDownloadAdapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(noteDownloadAdapter);
        irc.setOnRefreshListener(this);

        mPresenter.readDownloadNote(outputFile, ".ppt");
    }

    @Override
    public void returnReadResult(List<NoteDownload> noteDownloads) {
        Log.e("RESULT", noteDownloads.get(0).getName());
        if (noteDownloads != null){
            noteDownloadAdapter.replaceAll(noteDownloads);
        }
        irc.setRefreshing(false);
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
    }

   /* @Override
    public void scrolltoTop() {
        irc.smoothScrollToPosition(0);
    }*/

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
    public void onRefresh() {
        noteDownloadAdapter.getPageBean().setRefresh(true);
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.readDownloadNote(outputFile, ".ppt");
    }
}
