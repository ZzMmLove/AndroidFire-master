package com.gdgst.shuoke360.ui.news.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gdgst.common.commonutils.ToastUitl;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.baserx.RxSchedulers;
import com.gdgst.shuoke360.downloadprogress.bean.Download;
import com.gdgst.shuoke360.downloadprogress.service.DownloadService;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;


import rx.Observable;
import rx.Subscriber;

/**
 *
 * Created by Administrator on 8/18 0018.
 */

public class NoteDetailActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.download_progress)
    ProgressBar mDownloadProgress;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    private String url;
    private String fileName;
    private LocalBroadcastManager bManager;
    public static final String MESSAGE_PROGRESS = "message_progress";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(MESSAGE_PROGRESS)) {

                Download download = intent.getParcelableExtra("download");
                mDownloadProgress.setProgress(download.getProgress());
               if (download.getProgress() == 100) {
                    ToastUitl.show("下载完成", 0);
                   mDownloadProgress.setVisibility(View.GONE);
//                    progress_text.setText("File Download Complete");

                } else {
//                    progress_text.setText(
//                            StringUtils.getDataSize(download.getCurrentFileSize())
//                                    + "/" +
//                                    StringUtils.getDataSize(download.getTotalFileSize()));
                }
            }
        }
    };


    public static void startAction(Context context, View view, String url, String name){
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("name", name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, view, AppConstant.TRANSITION_ANIMATION_NEWS_PHOTOS);
            context.startActivity(intent, options.toBundle());
        }else {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth()/2, view.getHeight()/2, 0, 0);
            ActivityCompat.startActivity(context, intent, options.toBundle());
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.sg;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        verifyStoragePermissions(this);
        SetTranslanteBar();
        SetStatusBarColor();
        url = getIntent().getStringExtra("url");
        fileName = getIntent().getStringExtra("name");

        setWebView(url);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                ToastUitl.show(item.getTitle(), 0);
                mDownloadProgress.setVisibility(View.VISIBLE);
                Intent intent = new Intent(NoteDetailActivity.this, DownloadService.class);
                intent.putExtra("url", url);
                intent.putExtra("name", fileName);
                startService(intent);
                return true;
            }
        });
        registerReceiver();
    }

    private void registerReceiver() {

        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void setWebView(final String url) {
        mRxManager.add(Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.<Long>io_main())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        WebSettings settings = webView.getSettings();
                        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
                        settings.setJavaScriptEnabled(true);
                        settings.setUseWideViewPort(true);
                       // Log.e("TAG", "https://view.officeapps.live.com/op/view.aspx?src="+url);
                        webView.setWebViewClient(new WebViewClient(){
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                            }
                        });
                        webView.loadUrl("https://view.officeapps.live.com/op/view.aspx?src="+url);
                    }
                }));
    }

    //

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册时，使用注册时的manager解绑
        bManager.unregisterReceiver(broadcastReceiver);
    }

}
