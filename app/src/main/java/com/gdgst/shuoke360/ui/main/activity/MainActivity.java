package com.gdgst.shuoke360.ui.main.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gdgst.shuoke360.BuildConfig;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppApplication;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.CheckUpdate;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.TabEntity;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.shuoke360.service.UpdateAppService;
import com.gdgst.shuoke360.ui.main.contract.AutoLoginContract;
import com.gdgst.shuoke360.ui.main.fragment.FourMainFragment;
import com.gdgst.shuoke360.ui.main.fragment.NewsMainFragment;
import com.gdgst.shuoke360.ui.main.fragment.PractiseMainFragment;
import com.gdgst.shuoke360.ui.main.fragment.VideoMainFragment;
import com.gdgst.shuoke360.ui.main.model.AutoLoginModel;
import com.gdgst.shuoke360.ui.main.presenter.AutoLoginPresenter;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonutils.LogUtils;
import com.gdgst.common.daynightmodeutils.ChangeModeController;
import com.jiangyy.easydialog.UpdateDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * des:主界面 150 20000 3000000
 * Created by xsf
 * on 2016.09.15:32
 */
public class MainActivity extends BaseActivity<AutoLoginPresenter, AutoLoginModel> implements AutoLoginContract.View {
    private final static String TAG = "MainActivity";
    private static int SCREEN_HEIGHT = 0;
    private static int SCREEN_WIDTH = 0;

    @Bind(R.id.tab_layout)
    CommonTabLayout tabLayout;

    private String[] mTitles = {"首页", "练字","视频","我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.ic_home_normal,R.mipmap.ic_girl_normal,R.mipmap.ic_video_normal,R.mipmap.ic_care_normal};
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected,R.mipmap.ic_girl_selected, R.mipmap.ic_video_selected,R.mipmap.ic_care_selected};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private NewsMainFragment newsMainFragment;
    private VideoMainFragment videoMainFragment;
    private FourMainFragment careMainFragment;
    private PractiseMainFragment practiseMainFragment;

    private static int tabLayoutHeight;

    private Intent updateServiceIntent;

    /**
     * 退出时间
     */
    private long mExitTime;
    /**
     * 退出间隔
     */
    private static final int INTERVAL = 2000;

    protected AppApplication myApplication;

    /**
     * 入口
     * @param activity
     */
    public static void startAction(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.gdgst.common.R.anim.fade_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }
    @Override
    public void initView() {
        //此处填上在http://fir.im/注册账号后获得的API_TOKEN以及APP的应用ID
        /*UpdateKey.API_TOKEN = AppConfig.API_FIRE_TOKEN;
        UpdateKey.APP_ID = AppConfig.APP_FIRE_ID;
        如果你想通过Dialog来进行下载，可以如下设置
        UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;
        UpdateFunGO.init(this);*/
        //初始化菜单
        initTab();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //切换daynight模式要立即变色的页面
        ChangeModeController.getInstance().init(this,R.attr.class);
        super.onCreate(savedInstanceState);
        //初始化frament
        initFragment(savedInstanceState);
        tabLayout.measure(0,0);
        tabLayoutHeight=tabLayout.getMeasuredHeight();
        //使用RxJava监听菜单显示或隐藏
        mRxManager.on(AppConstant.MENU_SHOW_HIDE, new Action1<Boolean>() {
            @Override
            public void call(Boolean hideOrShow) {
               // startAnimation(hideOrShow);
            }
        });

        //检测更新
        String version_code = BuildConfig.VERSION_NAME;
        mPresenter.getCheckUpdateResult("gstshuoke360", version_code);

        Display display = getWindowManager().getDefaultDisplay();
        SCREEN_HEIGHT = display.getHeight();// 设置屏幕高度
        SCREEN_WIDTH = display.getWidth();// 设置屏幕宽度
    }

    public static int  getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public static int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }


    /**
     * 初始化tab
     */
    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }
            @Override
            public void onTabReselect(int position) {

            }
        });
    }
    /**
     * 初始化碎片
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            //findFragmentByTag通过指定的标签来初始化视图
            newsMainFragment = (NewsMainFragment) getSupportFragmentManager().findFragmentByTag("newsMainFragment");
//            photosMainFragment = (PhotosMainFragment) getSupportFragmentManager().findFragmentByTag("photosMainFragment");
            practiseMainFragment =  (PractiseMainFragment) getSupportFragmentManager().findFragmentByTag("practiseMainFragment");
            videoMainFragment = (VideoMainFragment) getSupportFragmentManager().findFragmentByTag("videoMainFragment");
            careMainFragment = (FourMainFragment) getSupportFragmentManager().findFragmentByTag("careMainFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            newsMainFragment = new NewsMainFragment();
//            photosMainFragment = new PhotosMainFragment();
            practiseMainFragment =new PractiseMainFragment();
            videoMainFragment = new VideoMainFragment();
            careMainFragment = new FourMainFragment();

            transaction.add(R.id.fl_body, newsMainFragment, "newsMainFragment");
//            transaction.add(R.id.fl_body, photosMainFragment, "photosMainFragment");
            transaction.add(R.id.fl_body, practiseMainFragment, "practiseMainFragment");
            transaction.add(R.id.fl_body, videoMainFragment, "videoMainFragment");
            transaction.add(R.id.fl_body, careMainFragment, "careMainFragment");
        }
        transaction.commit();
        SwitchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);
    }

    /**
     * 切换
     */
    private void SwitchTo(int position) {
        LogUtils.logd("主页菜单position" + position);
        //获得一个Fragment的管理器
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(practiseMainFragment);
                //transaction.hide(photosMainFragment);
                transaction.hide(videoMainFragment);
                transaction.hide(careMainFragment);
                //新闻页面
                transaction.show(newsMainFragment);
                transaction.commitAllowingStateLoss();
                break;

            //练字
            case 1:
                transaction.hide(newsMainFragment);
                transaction.hide(videoMainFragment);
                transaction.hide(careMainFragment);
                //transaction.show(photosMainFragment);
                //显示练字的fragment
                transaction.show(practiseMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 2:
                transaction.hide(newsMainFragment);
                //transaction.hide(photosMainFragment);
                transaction.hide(practiseMainFragment);
                transaction.hide(careMainFragment);
                //要显示视频的页面
                transaction.show(videoMainFragment);
                transaction.commitAllowingStateLoss();

                break;
            //个人中心
            case 3:
                transaction.hide(newsMainFragment);
                //transaction.hide(photosMainFragment);
                transaction.hide(practiseMainFragment);
                transaction.hide(videoMainFragment);
                //要显示的我的个人中心的页面
                transaction.show(careMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }



    /**
     * 菜单显示隐藏动画
     * @param showOrHide
     */
    private void startAnimation(boolean showOrHide){
        final ViewGroup.LayoutParams layoutParams = tabLayout.getLayoutParams();
        ValueAnimator valueAnimator;
        ObjectAnimator alpha;
        if(!showOrHide){
             valueAnimator = ValueAnimator.ofInt(tabLayoutHeight, 0);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 1, 0);
        }else{
             valueAnimator = ValueAnimator.ofInt(0, tabLayoutHeight);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 0, 1);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height= (int) valueAnimator.getAnimatedValue();
                tabLayout.setLayoutParams(layoutParams);
            }
        });
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(valueAnimator,alpha);
        animatorSet.start();
    }

    /**
     * 监听全屏视频时返回键
     */
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //奔溃前保存位置
        LogUtils.loge("onSaveInstanceState进来了1");
        if (tabLayout != null) {
            LogUtils.loge("onSaveInstanceState进来了2");
            outState.putInt(AppConstant.HOME_CURRENT_TAB_POSITION, tabLayout.getCurrentTab());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        UpdateFunGO.onResume(this);
        mRxManager.on("access_tolen", new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, "onResume-----"+s);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        UpdateFunGO.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChangeModeController.onDestory();
    }

    /**
     * 判断两次返回时间间隔,小于两秒则退出程序
     */
    private void exit() {
        if (System.currentTimeMillis() - mExitTime > INTERVAL) {
            Toast.makeText(getApplicationContext(), "再按一次返回键,可直接退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
//            if (myApplication.removerAll()) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
//            }
        }
    }

    @Override
    public void retunUserData(User user) {

    }

    @Override
    public void returnAvatarResult(String imgUrl) {

    }

    @Override
    public void retrunCheckUpdateResult(Result<CheckUpdate> updateResult) {
        int errorCode = updateResult.getError_code();
        if (errorCode == 0){
            String ver = updateResult.getData().getApp_ver();
            String readme = updateResult.getData().getApp_readme();
            final String link = updateResult.getData().getApp_link();
            new UpdateDialog.Builder(this)
                    .setIcon(R.mipmap.logo)
                    .setTitle("发现新版本,"+ver+"来了!")
                    .setMessage(readme, R.color.colorAccent)
                    .setPositiveButton("立即下载", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            upDate(link);
                        }
                    }).setNegativeButton("以后更新", null).show();
        }
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

    private void upDate(final String link) {

        RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            updateServiceIntent = new Intent(MainActivity.this, UpdateAppService.class);
                            updateServiceIntent.putExtra("url", link);
                            Toast.makeText(MainActivity.this, "正在下载中", Toast.LENGTH_SHORT).show();
                            startService(updateServiceIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "SD卡下载权限被拒绝", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 110){
            if(Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(updateServiceIntent!=null)
                        startService(updateServiceIntent);
                }else{
                    //提示没有权限，安装不了咯
                    Toast.makeText(MainActivity.this, "没有权限，无法安装", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            if (event.getAction() == KeyEvent.ACTION_DOWN
//                    && event.getRepeatCount() == 0) {
//                exit();
//            }
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }
}
