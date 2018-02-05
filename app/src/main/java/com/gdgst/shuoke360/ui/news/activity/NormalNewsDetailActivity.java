package com.gdgst.shuoke360.ui.news.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.api.ApiConstants;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.baserx.RxSchedulers;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;

/**
 * des:普通新闻详情
 * Created by xsf
 * on 2016.09.16:57
 */
public class NormalNewsDetailActivity extends BaseActivity{


    @Bind(R.id.news_detail_photo_iv)
    ImageView newsDetailPhotoIv;
    @Bind(R.id.mask_view)
    View maskView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.news_detail_from_tv)
    TextView newsDetailFromTv;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.webView1)
    WebView webView;


    private String pid;
    private String type;
    private String id;
    private String mNewsTitle;
    private String mShareLink;

    /**
     * 入口，跳转到新闻详情页面
     */
    public static void startAction(Context mContext, View view,  String imgUrl, String pid, String id) {
        Intent intent = new Intent(mContext, NormalNewsDetailActivity.class);
        intent.putExtra("pid", pid);
        intent.putExtra("imgurl", imgUrl);
        intent.putExtra("id", id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((Activity) mContext, view, AppConstant.TRANSITION_ANIMATION_NEWS_PHOTOS);
            mContext.startActivity(intent, options.toBundle());
        } else {

            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_news_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        SetTranslanteBar();
        SetStatusBarColor();
        pid = getIntent().getStringExtra("pid");
        id = getIntent().getStringExtra("id");
        mNewsTitle = getIntent().getStringExtra("title");

        String newsImgSrc = getIntent().getStringExtra("imgurl");
        setNewsDetailPhotoIv(newsImgSrc);
        setToolBarLayout(mNewsTitle);
        //String url = "http://shuoke360.cn/api/copybook?token=gstshuoke360&type=yuedu&pid=411&id=1115";
        final String url = ApiConstants.SHUO_KE_HOST+"/api/copybook?token=gstshuoke360&type=yuedu&pid="+pid+"&id="+id;
        setNewsDetailBodyWebView(url);

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
        toolbar.inflateMenu(R.menu.news_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_web_view:
                        //NormalNewsDetailActivity.startAction(NormalNewsDetailActivity.this, mShareLink, mNewsTitle);
                        break;
                    case R.id.action_browser:
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        if (canBrowse(intent)) {
                            Uri uri = Uri.parse(mShareLink);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }
        });
        //分享
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (url == null) {
//                    mShareLink = "";
//                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_contents, mNewsTitle, url));
                startActivity(Intent.createChooser(intent, getTitle()));
            }
        });
    }

    private void setToolBarLayout(String newsTitle) {
        toolbarLayout.setTitle(newsTitle);
        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.primary_text_white));
    }

    private void setNewsDetailPhotoIv(String imgSrc) {
        Glide.with(this).load(imgSrc)
                .fitCenter()
                .error(com.gdgst.common.R.drawable.ic_empty_picture)
                .crossFade().into(newsDetailPhotoIv);
    }

    private void setNewsDetailBodyWebView(final String url) {
        mRxManager.add(Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.<Long>io_main())  //线程的切换
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        setBody(url);
                    }
                }));
    }

    /***
     * 将新闻的内容放到界面显示
     * @param url
     */
    private void setBody(String url) {
        WebSettings settings = webView.getSettings();
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        settings.setJavaScriptEnabled(true);
         webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new JavaScriptInterface(NormalNewsDetailActivity.this), "imagelistner");
    }

    private boolean canBrowse(Intent intent) {
        return intent.resolveActivity(getPackageManager()) != null && mShareLink != null;
    }

    @Override
    protected void onDestroy() {
        if (webView != null){
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    /**
     * 自定义WebViewClient，重新设置网页中图片的大小并设置图片点击事件
     */
    private class MyWebViewClient extends WebViewClient{

        @Override
        public void onPageFinished(WebView view, String url) {
            imgReset();
            addImageClickListner();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    /**
     * html加载完成之后，添加监听图片的点击js函数
     */
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
       webView.loadUrl("javascript:(function(){" +
               "var objs = document.getElementsByTagName(\"img\");" +
               "for(var i = 0; i < objs.length; i++){" +
               "objs[i].onclick = function(){" +
               "window.imagelistner.openImage(this.src);" +
               "}" +
               "}" +
               "})()");
    }

    /**
     * 重置webview中img标签的图片大小
     */
    private void imgReset() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img');" +
                "for(var i = 0; i < objs.length; i++){" +
                "var img = objs[i];" +
                "img.style.maxWidth = '100%';" +
                "img.style.height = 'auto';" +
                "}" +
                "})()");
    }
    public static class JavaScriptInterface {

        private Context context;

        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openImage(String img) {
            Log.i("TAG", "响应点击事件!");
            Intent intent = new Intent();
            intent.putExtra(AppConstant.PHOTO_DETAIL, img);
            //查看大图的类
            intent.setClass(context, PhotosDetailActivity.class);
            context.startActivity(intent);
        }
    }
}
