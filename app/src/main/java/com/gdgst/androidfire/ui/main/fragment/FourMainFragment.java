package com.gdgst.androidfire.ui.main.fragment;

import android.content.Intent;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.gdgst.androidfire.R;
import com.gdgst.androidfire.ui.main.view.PractiseActivity;
import com.gdgst.common.base.BaseFragment;
import com.gdgst.common.commonwidget.NormalTitleBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * des:关注主页(第四个fragment)
 * Created by xsf
 * on 2016.09.17:07
 */
public class FourMainFragment extends BaseFragment {
    //    @Bind(R.id.ll_friend_zone)
//    LinearLayout llFriendZone;
//    @Bind(R.id.iv_add)
//    ImageView ivAdd;
    //账号管理
    @Bind(R.id.rl_account_manager)
    RelativeLayout rl_account_manager;
    //我的收藏
    @Bind(R.id.rl_switch_collect)
    RelativeLayout rl_switch_collect;
    //浏览记录
    @Bind(R.id.rl_history)
    RelativeLayout rl_history;
    //清空缓存
    @Bind(R.id.rl_switch_cache)
    RelativeLayout rl_switch_cache;
    //意见反馈
    @Bind(R.id.rl_feedback)
    RelativeLayout rl_feedback;
    //分享好友
    @Bind(R.id.rl_shared)
    RelativeLayout rl_shared;
    //检测更新
    @Bind(R.id.rl_update)
    RelativeLayout rl_update;
    //关于
    @Bind(R.id.rl_switch_about)
    RelativeLayout rl_switch_about;


    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_four_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        ntb.setTvLeftVisiable(false);
        ntb.setTitleText(getContext().getString(R.string.care_main_title));
    }

//    @OnClick(R.id.ll_daynight_toggle)
//    public void dayNightToggle(){
//        ChangeModeController.toggleThemeSetting(getActivity());
//    }
//    @OnClick(R.id.ll_daynight_about)
//    public void about(){
//        AboutActivity.startAction(getContext());
//    }


    @OnClick(R.id.rl_account_manager)
    public void rl_account_manager() {
        //账号管理
    }

    @OnClick(R.id.rl_switch_collect)
    public void rl_switch_collect() {
        //我的收藏
    }
    @OnClick(R.id.rl_history)
    public void rl_history() {
        //浏览记录
    }
    @OnClick(R.id.rl_switch_cache)
    public void rl_switch_cache() {
        //清空缓存
    }
    @OnClick(R.id.rl_feedback)
    public void rl_feedback() {
        //意见反馈
    }
    @OnClick(R.id.rl_shared)
    public void rl_shared() {
        //分享给好友
    }
    @OnClick(R.id.rl_update)
    public void rl_update() {
        //检测更新
    }
    @OnClick(R.id.rl_switch_about)
    public void rl_switch_about() {
        //关于我们
        Intent intent1 = new Intent();
        intent1.setClass(getActivity(), PractiseActivity.class);
        startActivity(intent1);
    }


}
