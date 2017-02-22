package com.gdgst.androidfire.ui.news.activity;

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
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdgst.androidfire.R;
import com.gdgst.androidfire.app.AppConstant;
import com.gdgst.androidfire.bean.MessageDetail;
import com.gdgst.androidfire.ui.news.contract.NewsDetailContract;
import com.gdgst.androidfire.ui.news.model.NewsDetailModel;
import com.gdgst.androidfire.ui.news.presenter.NewsDetailPresenter;
import com.gdgst.androidfire.widget.URLImageGetter;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.baserx.RxSchedulers;
import com.gdgst.common.commonutils.LogUtils;
import com.gdgst.common.commonutils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;

/**
 * des:普通新闻详情
 * Created by xsf
 * on 2016.09.16:57
 */
public class NewsDetailActivity extends BaseActivity<NewsDetailPresenter, NewsDetailModel> implements NewsDetailContract.View {


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
    @Bind(R.id.news_detail_body_tv)
    TextView newsDetailBodyTv;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.fab)
    FloatingActionButton fab;


    private String tid;
    private URLImageGetter mUrlImageGetter;
    private String mNewsTitle;
    private String mShareLink;

    /**
     * 入口
     *
     * @param mContext
     * @param tid
     */
    public static void startAction(Context mContext, View view, String tid, String imgUrl) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra("tid", tid);
        intent.putExtra("imgurl", imgUrl);

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
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        SetTranslanteBar();
        tid = getIntent().getStringExtra("tid");

//        mPresenter.getOneNewsDataRequest(postId);
        mPresenter.getOneMessageDataRequest("forum.post.list", tid, "10");
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
                        NewsBrowserActivity.startAction(NewsDetailActivity.this, mShareLink, mNewsTitle);
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
                if (mShareLink == null) {
                    mShareLink = "";
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_contents, mNewsTitle, mShareLink));
                startActivity(Intent.createChooser(intent, getTitle()));
            }
        });


    }

//    @Override
//    public void returnOneNewsData(NewsDetail newsDetail) {
//        mShareLink = newsDetail.getShareLink();
//        mNewsTitle = newsDetail.getTitle();
//        String newsSource = newsDetail.getSource();
//        String newsTime = TimeUtil.formatDate(newsDetail.getPtime());
//        String newsBody = newsDetail.getBody();
////        String NewsImgSrc = getImgSrcs(newsDetail);
//
//        setToolBarLayout(mNewsTitle);
//        //mNewsDetailTitleTv.setText(newsTitle);
//        newsDetailFromTv.setText(getString(R.string.news_from, newsSource, newsTime));
////        setNewsDetailPhotoIv(NewsImgSrc);
//        setNewsDetailBodyTv(newsDetail, newsBody);
//    }

    @Override
    public void returnOneMessageData(MessageDetail.DataBean.TopicBean newsDetail) {
        mShareLink = newsDetail.getShare_url();
        mNewsTitle = newsDetail.getTitle();
        String newsSource = newsDetail.getTitle();
        String newsTime = TimeUtil.formatDate(newsDetail.getPostdate());
        String newsBody = newsDetail.getContent();

        //设置顶部图片
        String NewsImgSrc = getIntent().getStringExtra("imgurl");
        setNewsDetailPhotoIv(NewsImgSrc);

        setToolBarLayout(mNewsTitle);
        //mNewsDetailTitleTv.setText(newsTitle);
        newsDetailFromTv.setText(getString(R.string.news_from, newsSource, newsTime));

        setNewsDetailBodyTv(newsDetail, newsBody);
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

    private void setNewsDetailBodyTv(final MessageDetail.DataBean.TopicBean newsDetail, final String newsBody) {
        mRxManager.add(Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.<Long>io_main())
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
                        setBody(newsDetail, newsBody);
                    }
                }));
    }

    private void setBody(MessageDetail.DataBean.TopicBean newsDetail, String newsBody) {
        List list = getImgSrc(newsBody);
        LogUtils.logd(list.get(1).toString());

        int imgTotal = list.size();
        if (isShowBody(newsBody, imgTotal)) {
//              mNewsDetailBodyTv.setMovementMethod(LinkMovementMethod.getInstance());//加这句才能让里面的超链接生效,实测经常卡机崩溃
            mUrlImageGetter = new URLImageGetter(newsDetailBodyTv, newsBody, imgTotal);

            newsDetailBodyTv.setText(Html.fromHtml(newsBody, mUrlImageGetter, null));

        } else {
            newsDetailBodyTv.setText(Html.fromHtml(newsBody));
        }
    }


    private boolean isShowBody(String newsBody, int imgTotal) {
        return imgTotal >= 2 && newsBody != null;
    }

//    private String getImgSrcs(NewsDetail newsDetail) {
//        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
//        String imgSrc;
//        if (imgSrcs != null && imgSrcs.size() > 0) {
//            imgSrc = imgSrcs.get(0).getSrc();
//        } else {
//            imgSrc = getIntent().getStringExtra("imgurl");
//        }
//        return imgSrc;
//    }

    private boolean canBrowse(Intent intent) {
        return intent.resolveActivity(getPackageManager()) != null && mShareLink != null;
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


    public static final Pattern PATTERN = Pattern.compile("<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static List getImgSrc(String html) {
        Matcher matcher = PATTERN.matcher(html);
        List list = new ArrayList();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (group == null) {
                continue;
            }
            //   这里可能还需要更复杂的判断,用以处理src="...."内的一些转义符
            if (group.startsWith("'")) {
                list.add(group.substring(1, group.indexOf("'", 1)));
            } else if (group.startsWith("\"")) {
                list.add(group.substring(1, group.indexOf("\"", 1)));
            } else {
                list.add(group.split("\\s")[0]);
            }
        }
        return list;
    }

}
