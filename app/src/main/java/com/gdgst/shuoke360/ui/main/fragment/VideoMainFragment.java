package com.gdgst.shuoke360.ui.main.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.Category;
import com.gdgst.shuoke360.bean.VideoChannelTable;
import com.gdgst.shuoke360.db.VideosChannelTableManager;
import com.gdgst.shuoke360.ui.main.contract.CategoryContract;
import com.gdgst.shuoke360.ui.main.model.CategoryModel;
import com.gdgst.shuoke360.ui.main.presenter.CategoryPresenter;
import com.gdgst.shuoke360.ui.main.view.MyPopupWindow;
import com.gdgst.shuoke360.ui.news.fragment.VideosFragment;
import com.gdgst.shuoke360.utils.MyUtils;
import com.gdgst.common.base.BaseFragment;
import com.gdgst.common.base.BaseFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * des:视频首页
 * Created by xsf
 * on 2016.09.16:45
 */
public class VideoMainFragment extends BaseFragment<CategoryPresenter, CategoryModel> implements CategoryContract.View {

    private final static String TAG = "VideoMainFragment";
    @Bind(R.id.tabs)
    TabLayout tabs;

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    /**浮动的回到顶部按钮*/
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.tv_nianji)
    TextView tvNianJi;
    @Bind(R.id.tv_bihua)
    TextView tvBiHua;
    @Bind(R.id.tv_pianpang)
    TextView tvPianP;
    @Bind(R.id.tv_jiegou)
    TextView tvJieGou;
    
    private BaseFragmentAdapter fragmentAdapter;

    private Category category;

    @Override
    protected int getLayoutResource() {
        return R.layout.app_bar_video;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mPresenter.loadCategoryResult("category.json");
//        String s = MyUtils.getJson(getContext(), "category.json");
//        Log.e("TAG", "====dddd===="+s);

        List<String> channelNames = new ArrayList<>();
        List<VideoChannelTable> videoChannelTableList = VideosChannelTableManager.loadVideosChannelsMine();
        List<Fragment> mNewsFragmentList = new ArrayList<>();
        for (int i = 0; i < videoChannelTableList.size(); i++) {
            channelNames.add(videoChannelTableList.get(i).getChannelName());
            mNewsFragmentList.add(createListFragments(videoChannelTableList.get(i)));
        }
        fragmentAdapter = new BaseFragmentAdapter(getChildFragmentManager(), mNewsFragmentList, channelNames);
        viewPager.setAdapter(fragmentAdapter);
        tabs.setupWithViewPager(viewPager);
        MyUtils.dynamicSetTabLayoutMode(tabs);
        setPageChangeListener();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRxManager.post(AppConstant.NEWS_LIST_TO_TOP, "");
            }
        });

    }


    @OnClick({R.id.tv_nianji, R.id.tv_bihua, R.id.tv_pianpang, R.id.tv_jiegou})
    public void clickTv(View view){
        List<Category.AttrBean.ValsBean> vals = new ArrayList<>();
        switch (view.getId()){
            case R.id.tv_nianji:
                vals = category.getAttr().get(0).getVals();
                break;
            case R.id.tv_bihua:
                //Log.e("Category", "-----------"+category.toString());
                vals = category.getAttr().get(1).getVals();
                break;
            case R.id.tv_pianpang:
                vals = category.getAttr().get(2).getVals();
                break;
            case R.id.tv_jiegou:
                vals = category.getAttr().get(3).getVals();
                break;
        }
        MyPopupWindow myPopupWindow = new MyPopupWindow(getContext(),vals);
        myPopupWindow.showPricePopup(getView().findViewById(R.id.ll_fenlei), vals);
        myPopupWindow.setItemClickCompleted(new MyPopupWindow.OnItemClickCompleted() {
            @Override
            public void onItemClick(String itemValue) {
                Log.e("Category", "-----------"+itemValue);
            }
        });
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

    private VideosFragment createListFragments(VideoChannelTable videoChannelTable) {
        VideosFragment fragment = new VideosFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.VIDEO_TYPE, videoChannelTable.getChannelId());
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void returnCategory(Category category) {
        Log.e("Category", "-----------"+category.toString());
        this.category = category;
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
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: 2222222222222222222222222222222222222");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: 22222222222222222222222222222222222222");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: 22222222222222222222222222222222222222");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: 22222222222222222222222222222222");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: 22222222222222222222222222222222");
    }
}
