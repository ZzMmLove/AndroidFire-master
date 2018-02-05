package com.gdgst.shuoke360.ui.news.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.api.ApiConstants;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.CopyBookGlide;
import com.gdgst.shuoke360.ui.news.contract.PhotoListContract;
import com.gdgst.shuoke360.ui.news.model.PhotosListModel;
import com.gdgst.shuoke360.ui.news.presenter.PhotosListPresenter;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonwidget.LoadingTip;
import com.maning.imagebrowserlibrary.MNImageBrowser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * 碑帖图片浏览
 */
public class CopyBookPhotosListActivity extends BaseActivity<PhotosListPresenter, PhotosListModel> implements PhotoListContract.View, OnRefreshListener, OnLoadMoreListener{

    @Bind(R.id.ntb)
    Toolbar ntb;
    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private int page;
    private String id;
    private CommonRecycleViewAdapter<CopyBookGlide> adapter;
    private ArrayList<String> imgList = new ArrayList<>();

    public static void startAction(Context mContext, View view, String id) {
        Intent intent = new Intent(mContext, CopyBookPhotosListActivity.class);
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
        return R.layout.act_photos_list;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        SetTranslanteBar();
        SetStatusBarColor();
        id = getIntent().getStringExtra("id");
        ntb.setTitle("碑帖详情");
        ntb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

            adapter = new CommonRecycleViewAdapter<CopyBookGlide>(this, R.layout.item_photo) {
                @Override
                public void convert(ViewHolderHelper helper, final CopyBookGlide copyBookGlide) {
                    final int position = helper.getAdapterPosition();
                    //使用软应用防止内存泄漏
                    WeakReference<ImageView> imageViewWeakReference = new WeakReference<>((ImageView) helper.getView(R.id.iv_photo));
                    final ImageView imageView = imageViewWeakReference.get();
                    Glide.with(CopyBookPhotosListActivity.this).load(ApiConstants.SHUO_KE_HOST+copyBookGlide.getImg_url())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(com.gdgst.common.R.drawable.ic_image_loading)
                            .error(com.gdgst.common.R.drawable.ic_empty_picture)
                            .centerCrop()
                            .override(1090, 1090 * 3/4)
                            .crossFade().into(imageView);

                    helper.setOnClickListener(R.id.iv_photo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          /*  Intent intent = new Intent(CopyBookPhotosListActivity.this, PhotosDetailActivity.class);
                            intent.putExtra(AppConstant.PHOTO_DETAIL, ApiConstants.SHUO_KE_HOST+copyBookGlide.getImg_url());
                            startActivity(intent);*/
                           MNImageBrowser.showImageBrowser(getBaseContext(), imageView, position - 2,  imgList);
                        }
                    });
                }
            };
        irc.setAdapter(adapter);
        irc.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irc.smoothScrollToPosition(0);
            }
        });
        mPresenter.getCopyBookGlideRequest("gstshuoke360", page, id, "beitie");
    }

    /**
     * 从这里返回网络请求回调得到的数据返并把数据传到适配器
     * @param copyBookGlides
     */
    @Override
    public void returnCopyBookGlide(List<CopyBookGlide> copyBookGlides) {
        //把一套碑帖的所有图片放到一个集合中
        for (int i = 0; i < copyBookGlides.size(); i++){
            imgList.add(ApiConstants.SHUO_KE_HOST+copyBookGlides.get(i).getImg_url());
        }
        //Log.e("TAG","===Img==="+imgList.toString());
        if (copyBookGlides != null){
            page += 1;
            if (adapter.getPageBean().isRefresh()){
                irc.setRefreshing(false);
                adapter.replaceAll(copyBookGlides);
            }else {
                if (copyBookGlides.size() > 0){
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    adapter.addAll(copyBookGlides);
                }else {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }



    @Override
    public void showLoading(String title) {
        if (adapter.getPageBean().isRefresh()){
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        }
    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

    @Override
    public void showErrorTip(String msg) {
        if (adapter.getPageBean().isRefresh()){
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
            loadedTip.setTips(msg);
            irc.setRefreshing(false);
        }else {
            irc.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        adapter.getPageBean().setRefresh(false);
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getCopyBookGlideRequest("gstshuoke360", page, id, "beitie");
    }

    @Override
    public void onRefresh() {
        adapter.getPageBean().setRefresh(true);
        page = 0;
        irc.setRefreshing(true);
        mPresenter.getCopyBookGlideRequest("gstshuoke360", page, id, "beitie");
    }
}
