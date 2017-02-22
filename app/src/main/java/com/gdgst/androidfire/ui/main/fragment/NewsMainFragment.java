package com.gdgst.androidfire.ui.main.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gdgst.androidfire.R;
import com.gdgst.androidfire.app.AppConstant;
import com.gdgst.androidfire.bean.NewsChannelTable;
import com.gdgst.androidfire.ui.main.contract.NewsMainContract;
import com.gdgst.androidfire.ui.main.model.NewsMainModel;
import com.gdgst.androidfire.ui.main.presenter.NewsMainPresenter;
import com.gdgst.androidfire.ui.news.fragment.NewsFrament;
import com.gdgst.androidfire.utils.MyUtils;
import com.gdgst.common.base.BaseFragment;
import com.gdgst.common.base.BaseFragmentAdapter;
import com.gdgst.common.commonutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * des:新闻首页
 * Created by xsf
 * on 2016.09.16:45
 *   *******************************
 *   **在MVP模式中这个是View层的实现(看implements NewMainContract.View)** (wrote by JenfeeMa)
 *   *******************************
 */
public class NewsMainFragment extends BaseFragment<NewsMainPresenter,NewsMainModel>implements NewsMainContract.View {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabs;
//    @Bind(R.id.add_channel_iv)
//    ImageView addChannelIv;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private BaseFragmentAdapter fragmentAdapter;


    @Override
    protected int getLayoutResource() {
        return R.layout.app_bar_news;
    }

    @Override
    public void initPresenter() {
      mPresenter.setVM(this,mModel);
    }

    @Override
    public void initView() {
        mPresenter.lodeMineChannelsRequest();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRxManager.post(AppConstant.NEWS_LIST_TO_TOP, "");
            }
        });
    }
//    @OnClick(R.id.add_channel_iv)
//    public void clickAdd(){
//        NewsChannelActivity.startAction(getContext());
//    }

    @Override
    public void returnMineNewsChannels(List<NewsChannelTable> newsChannelsMine) {
        if(newsChannelsMine!=null) {
            List<String> channelNames = new ArrayList<>();
            List<Fragment> mNewsFragmentList = new ArrayList<>();
            for (int i = 0; i < newsChannelsMine.size(); i++) {
                channelNames.add(newsChannelsMine.get(i).getNewsChannelName());
                mNewsFragmentList.add(createListFragments(newsChannelsMine.get(i)));
            }
            fragmentAdapter = new BaseFragmentAdapter(getChildFragmentManager(), mNewsFragmentList, channelNames);
            viewPager.setAdapter(fragmentAdapter);
            tabs.setupWithViewPager(viewPager);
            MyUtils.dynamicSetTabLayoutMode(tabs);
            setPageChangeListener();
        }
    }

    private void setPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private NewsFrament createListFragments(NewsChannelTable newsChannel) {
        NewsFrament fragment = new NewsFrament();

        Bundle bundle = new Bundle();
        bundle.putString("newschanelId", newsChannel.getNewsChannelId());
        bundle.putString(AppConstant.NEWS_TYPE, newsChannel.getNewsChannelType());
        bundle.putInt(AppConstant.CHANNEL_POSITION, newsChannel.getNewsChannelIndex());
        LogUtils.logd("DON putstirng:",newsChannel.getNewsChannelId());
        fragment.setArguments(bundle);
        return fragment;
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
}
