package com.gdgst.shuoke360.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.shuoke360.ui.main.contract.LoginContract;
import com.gdgst.shuoke360.ui.main.model.LoginModel;
import com.gdgst.shuoke360.ui.main.presenter.LoginPresenter;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonutils.ToastUitl;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 登陆
 * Created by Administrator on 9/5 0005.
 */

public class LoginActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View {

    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.bt_go)
    Button btGo;
    @Bind(R.id.cv)
    CardView cv;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    /**
     * 入口
     * @param activity
     */
    public static void startAction(Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.gdgst.common.R.anim.fade_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.bt_go, R.id.fab, R.id.forgetpwd})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.fab:
               /* getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }*/
                intent = new Intent(this, CheckSMS.class);
                intent.putExtra("key", "regist");
                startActivity(intent);

                break;
            case R.id.bt_go:
               // mPresenter.getUserData("gstshuoke360", "18877826721", "1231");
                String userName = etUsername.getText().toString().trim();
                String passWord = etPassword.getText().toString().trim();
                if (isUserEmpty(userName) || isPassWordEmpty(passWord)){
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    this.etPassword.startAnimation(shake);
                    this.etUsername.startAnimation(shake);
                }else {
                    mPresenter.getUserData("gstshuoke360", userName, passWord);
                    /*if (!"".equals(user.getMob()) || user.getMob() != null) {
                        Explode explode = new Explode();
                        explode.setDuration(500);
                        getWindow().setExitTransition(explode);
                        getWindow().setEnterTransition(explode);
                        ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this );
                        Intent i2 = new Intent(this, UserInfoActivity.class);
                        i2.putExtra("user", user);
                        startActivity(i2, oc2.toBundle());
                        finish();
                    }*/
                }
                break;
            case R.id.forgetpwd:
                intent = new Intent(this, CheckSMS.class);
                intent.putExtra("key", "forgetpwd");
                startActivity(intent);
                break;
        }
    }

    private boolean isUserEmpty(String userName){
        if (userName.equals("") || userName == null){
            ToastUitl.show("用户名不能为空……", 0);
            return true;
        }else
            return false;
    }

    private boolean isPassWordEmpty(String passWord){
        if (passWord.equals("") || passWord == null){
            ToastUitl.show("密码不能为空……", 0);
            return true;
        }else
            return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void returnUserData(User user) {
        Log.e("TAG", "User:"+ user.toString());
        if (!"".equals(user.getMob()) || user.getMob() != null) {
            Explode explode = new Explode();
            explode.setDuration(500);
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
            MainActivity.startAction(LoginActivity.this);
            finish();
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
