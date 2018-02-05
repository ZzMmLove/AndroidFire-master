package com.gdgst.shuoke360.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.NewsChannelTable;
import com.gdgst.shuoke360.ui.main.activity.SearchActivity;
import com.gdgst.shuoke360.ui.main.contract.NewsMainContract;
import com.gdgst.shuoke360.ui.main.model.NewsMainModel;
import com.gdgst.shuoke360.ui.main.presenter.NewsMainPresenter;
import com.gdgst.shuoke360.ui.news.fragment.NewsFrament;
import com.gdgst.shuoke360.utils.MyUtils;
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

    /**左上角的数字书法掌上通*/
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    /***/
    @Bind(R.id.tabs)
    TabLayout tabs;

    /**频道分类管理*/
    //@Bind(R.id.add_channel_iv)
    //ImageView addChannelIv;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void initView() {
        mPresenter.lodeMineChannelsRequest();
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_edit:
                        startActivity(new Intent(getActivity(), SearchActivity.class));
                }
                return true;
            }
        });
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

    /**
     * 从网络请求数据后从这里返回数据到View层
     * @param newsChannelsMine 返回来的数据
     */
    @Override
    public void returnMineNewsChannels(List<NewsChannelTable> newsChannelsMine) {
        if(newsChannelsMine!=null) {
            List<String> channelNames = new ArrayList<>();
            List<Fragment> mNewsFragmentList = new ArrayList<>();

            for (int i = 0; i < newsChannelsMine.size(); i++) {
                //吧新闻频道的名称放到集合中
                channelNames.add(newsChannelsMine.get(i).getNewsChannelName());
                //newsChannelsMine的长度决定要加载多少Fragment
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
                //TODO
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
        bundle.putString(AppConstant.NEWS_ID, newsChannel.getNewsChannelId());
        bundle.putString(AppConstant.NEWS_TYPE, newsChannel.getNewsChannelType());
        bundle.putInt(AppConstant.CHANNEL_POSITION, newsChannel.getNewsChannelIndex());
        LogUtils.logd("DON putstirng:",newsChannel.getNewsChannelId());
        //用于fragment之间的传值
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
