package com.gdgst.shuoke360.ui.main.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gdgst.common.commonutils.ImageLoaderUtils;
import com.gdgst.common.compressorutils.ImageUtil;
import com.gdgst.shuoke360.BuildConfig;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.api.ApiConstants;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.CheckUpdate;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.shuoke360.service.UpdateAppService;
import com.gdgst.shuoke360.ui.main.activity.HistoryActivity;
import com.gdgst.shuoke360.ui.main.activity.LoginActivity;
import com.gdgst.shuoke360.ui.main.activity.MainActivity;
import com.gdgst.shuoke360.ui.main.activity.NoteDownLoadActivity;
import com.gdgst.shuoke360.ui.main.activity.UpdatePasswordActivity;
import com.gdgst.shuoke360.ui.main.activity.UserInfoActivity;
import com.gdgst.shuoke360.ui.main.contract.AutoLoginContract;
import com.gdgst.shuoke360.ui.main.model.AutoLoginModel;
import com.gdgst.shuoke360.ui.main.presenter.AutoLoginPresenter;
import com.gdgst.shuoke360.ui.news.activity.AboutActivity;
import com.gdgst.shuoke360.ui.news.activity.NoteDetailActivity;
import com.gdgst.shuoke360.utils.MyUtils;
import com.gdgst.common.base.BaseFragment;
import com.gdgst.common.commonutils.DataCleanManager;
import com.gdgst.common.commonutils.ToastUitl;
import com.gdgst.common.commonwidget.NormalTitleBar;
import com.gdgst.common.daynightmodeutils.ChangeModeController;
import com.jiangyy.easydialog.CommonDialog;
import com.jiangyy.easydialog.UpdateDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.wyp.avatarstudio.AvatarStudio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;

/**
 * des:关注主页(第四个fragment),也就是我的页面
 * Created by xsf
 * on 2016.09.17:07
 */
public class FourMainFragment extends BaseFragment<AutoLoginPresenter, AutoLoginModel> implements AutoLoginContract.View, SwipeRefreshLayout.OnRefreshListener {
    //    @Bind(R.id.ll_friend_zone)
//    LinearLayout llFriendZone;
//    @Bind(R.id.iv_add)
//    ImageView ivAdd;
    //账号管理
//    @Bind(R.id.rl_account_manager)
//    RelativeLayout rl_account_manager;
//    //我的收藏
//    @Bind(R.id.rl_switch_collect)
//    RelativeLayout rl_switch_collect;
    //浏览记录
    @Bind(R.id.rl_history)
    RelativeLayout rl_history;
    //清空缓存
    @Bind(R.id.rl_switch_cache)
    RelativeLayout rl_switch_cache;
    //意见反馈
    @Bind(R.id.rl_control_them)
    //分享好友
    RelativeLayout rl_feedback;
    @Bind(R.id.rl_shared)
    RelativeLayout rl_shared;
    //检测更新
    @Bind(R.id.rl_update)
    RelativeLayout rl_update;
    //关于
    @Bind(R.id.rl_switch_about)
    RelativeLayout rl_switch_about;
    //
    @Bind(R.id.btnLogin)
    Button btnLogin;
    //
    @Bind(R.id.ivAvatar)
    CircleImageView ivAvatar;
    //
    @Bind(R.id.tv_name)
    TextView name;
    //
    @Bind(R.id.tv_school)
    TextView school;

    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    //
    @Bind(R.id.btn_exit)
    Button exitLogin;
    //
    @Bind(R.id.rl_switch_updatepwd)
    RelativeLayout updatePwd;
    //
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;


    private SharedPreferences sharedPreferences;
    private String accessToken;

    private User user;
    private Intent updateServiceIntent;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_mine;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        sharedPreferences = getContext().getSharedPreferences(AppConstant.SHAREPREFRENECE_USER, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token","");
        if (!accessToken.equals("")){
            mPresenter.getUserData("gstshuoke360", accessToken);

            String nameStr = sharedPreferences.getString("name", "--");
            String schoolStr = sharedPreferences.getString("school", "--");
            String mobStr = sharedPreferences.getString("mob","点我登录");
            String avatar = sharedPreferences.getString("avatar","");
            Glide.with( getContext() ).load(ApiConstants.SHUO_KE_HOST+avatar).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.toux2).into(ivAvatar);//.diskCacheStrategy(DiskCacheStrategy.RESULT).
            btnLogin.setText(mobStr);
            name.setText(nameStr);
            school.setText(schoolStr);
            exitLogin.setVisibility(View.VISIBLE);
            updatePwd.setVisibility(View.VISIBLE);

        }else{
            exitLogin.setVisibility(View.GONE);
            updatePwd.setVisibility(View.GONE);
        }
        ntb.setTvLeftVisiable(false);
        ntb.setTitleText(getContext().getString(R.string.care_main_title));
        srl.setOnRefreshListener(this);

    }

    @OnClick(R.id.btnLogin)
    public void btnLogin(){
        if (accessToken.equals("")){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else {
            startActivity(new Intent(getActivity(), UserInfoActivity.class));
        }
    }

    @OnClick(R.id.rl_history)
    public void rl_history() {
        //浏览记录
        startActivity(new Intent(getActivity(), HistoryActivity.class));
    }

    @OnClick(R.id.rl_download)
    public void rl_download(){
        startActivity(new Intent(getActivity(), NoteDownLoadActivity.class));
    }

    @OnClick(R.id.rl_switch_cache)
    public void rl_switch_cache() {
        //清空缓存
        //File file = getContext().getCacheDir();  // 所有缓存文件
//        Log.e("Files", "=="+getContext().getExternalCacheDir().getPath());
        File fileDir = new File(getContext().getExternalCacheDir().getPath());
     /*  String[] files = fileDir.list();
        for (int i = 0; i < files.length; i++){
            Log.e("File", "=="+files[i]);
        }*/
        try {
            String size = DataCleanManager.getCacheSize(fileDir);
            new CommonDialog.Builder(getActivity())
                    .setTitle("是否清空缓存？")
                    .setMessage(size)
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataCleanManager.cleanExternalCache(getContext());
                            DataCleanManager.cleanFiles(getContext());
                        }
                    }).setNegativeButton("取消", null)
                    .create()
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.rl_control_them)
    public void rl_feedback() {
        //切换主题
        ChangeModeController.toggleThemeSetting(getActivity());
    }

    @OnClick(R.id.rl_shared)
    public void rl_shared() {
        //分享给好友
        showQrcode();
    }

    @OnClick(R.id.rl_update)
    public void rl_update() {
        //检测更新
        String version_code = BuildConfig.VERSION_NAME;
        mPresenter.getCheckUpdateResult("gstshuoke360", version_code);
    }

    @OnClick(R.id.rl_switch_about)
    public void rl_switch_about() {
        //关于我们
        AboutActivity.startAction(getContext());
    }

    @OnClick(R.id.ivLeft)
    public void ivEdit(){
        if (accessToken.equals("")){
            startActivity(new Intent(getContext(), LoginActivity.class));
        }else
        startActivity(new Intent(getContext(), UserInfoActivity.class));
    }

    @OnClick(R.id.btn_exit)
    public void exitLogin(){
        SharedPreferences.Editor editor = sharedPreferences.edit().remove("mob");
        editor.remove("school");
        editor.remove("nickname");
        editor.remove("name");
        editor.remove("email");
        editor.remove("remark");
        editor.remove("type");
        editor.remove("sex");
        editor.remove("access_token");
        editor.remove("avatar");
        editor.remove("addr");
        editor.remove("className");
        editor.commit();
        exitLogin.setVisibility(View.GONE);
        onRefresh();
        onResume();
        LoginActivity.startAction(getActivity());
        getActivity().finish();
    }

    @OnClick(R.id.ivAvatar)
    public void ivAvatar(){
        new AvatarStudio.Builder((FragmentActivity) getContext())
                .needCrop(true)//是否裁剪，默认裁剪
                .setTextColor(Color.BLUE)
                .dimEnabled(true)//背景是否dim 默认true
                .setAspect(1, 1)//裁剪比例 默认1：1
                .setOutput(200, 200)//裁剪大小 默认200*200
                .setText("打开相机", "从相册中选取", "取消")
                .show(new AvatarStudio.CallBack() {
                    @Override
                    public void callback(String uri) {
                        //uri为图片路径
                        Glide.with( getContext() ).load(uri).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.toux2).into(ivAvatar);
//                      Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(uri));
                        Bitmap bitmap = ImageUtil.getScaledBitmap(getContext(), Uri.parse(uri), 200, 200);
                        //Log.e("TAG", "图片压缩前大小:"+(bitmap.getByteCount() / 1024 )+"宽度："+bitmap.getHeight());
                        String base64Str = MyUtils.Bitmap2StrByBase64(bitmap);
                        if (!accessToken.equals("")){
                            mPresenter.getAvatarResult("gstshuoke360", accessToken, "data:image/jpeg;base64,"+base64Str);//"image/jpg;base64,"+
                        }
                    }
                });
    }

    @OnClick(R.id.rl_switch_updatepwd)
    public void updatePwd(){
        startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "onResume()");
        initView();
        String nameStr = sharedPreferences.getString("name", "--");
        String schoolStr = sharedPreferences.getString("school", "--");
        String mobStr = sharedPreferences.getString("mob","点我登录");
        String avatarUrl = sharedPreferences.getString("avatar", "");
        Log.e("AVATAR", avatarUrl);
        Glide.with( getContext() ).load(ApiConstants.SHUO_KE_HOST+avatarUrl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.toux2).into(ivAvatar);//diskCacheStrategy(DiskCacheStrategy.RESULT)
        btnLogin.setText(mobStr);
        name.setText(nameStr);
        school.setText(schoolStr);
    }

    @Override
    public void onRefresh() {
        initView();
        srl.setRefreshing(false);
    }

    @Override
    public void retunUserData(User user) {
        Log.e("TAG", "AutoLogin-User-return");
        this.user = user;
    }

    @Override
    public void returnAvatarResult(String imgUrl) {
        Log.e("TAG", "Avatar:"+imgUrl);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("avatar", imgUrl);
        editor.commit();
        //Glide.get(getContext()).clearMemory();
        Glide.with( getContext()).load(ApiConstants.SHUO_KE_HOST+imgUrl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.toux2).into(ivAvatar);
    }

    /**
     * 检查更新返回结果
     * @param updateResult
     */
    @Override
    public void retrunCheckUpdateResult(Result<CheckUpdate> updateResult) {
        int errorCode = updateResult.getError_code();
        String message = updateResult.getMessage();
        if (errorCode == 1){
            ToastUitl.show(message, 0);
        }else if (errorCode == 0){
            String ver = updateResult.getData().getApp_ver();
            String readme = updateResult.getData().getApp_readme();
            final String link = updateResult.getData().getApp_link();
            new UpdateDialog.Builder(getActivity())
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

    private void upDate(final String link) {

        RxPermissions.getInstance(getActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                            updateServiceIntent = new Intent(getActivity(), UpdateAppService.class);
                            updateServiceIntent.putExtra("url", link);
                            Toast.makeText(getContext(), "正在下载中", Toast.LENGTH_SHORT).show();
                            getActivity().startService(updateServiceIntent);
                        } else {
                            Toast.makeText(getContext(), "SD卡下载权限被拒绝", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 分享
     */
    private void showQrcode() {
        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  //image的布局方式
//		lp.setMargins(10, 10, 10, 10);
        layout.setPadding(60, 60, 60, 20);
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(R.drawable.qrcode); // 二维码
        layout.setGravity(Gravity.CENTER);
        iv.setLayoutParams(lp);
        layout.addView(iv);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("扫描二维码，下载实验掌上通");
        builder.setPositiveButton("其他方式分享", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
                intent.setType("text/plain"); // 分享发送的数据类型
//				String msg = "我正在使用《掌上通》，挺不错的应用。你也来试试吧！下载地址："+info.getUpgrade_url();
                String msg = "我正在使用《数字书法掌上通》，挺不错的应用。你也来试试吧！下载地址：http://url.cn/5qDD3aC";
                intent.putExtra(Intent.EXTRA_TEXT, msg); // 分享的内容
                startActivity(Intent.createChooser(intent, "选择分享"));// 目标应用选择对话框的标题
            }
        });
        builder.setView(layout);
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 110){
            if(Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(updateServiceIntent!=null)
                        getActivity().startService(updateServiceIntent);
                }else{
                    //提示没有权限，安装不了咯
                    Toast.makeText(getContext(), "没有权限，无法安装", Toast.LENGTH_LONG).show();
                }
            }
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


}
