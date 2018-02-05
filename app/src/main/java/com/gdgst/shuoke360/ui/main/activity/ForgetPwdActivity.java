package com.gdgst.shuoke360.ui.main.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.ForgetPwdContract;
import com.gdgst.shuoke360.ui.main.model.ForgetPwdModel;
import com.gdgst.shuoke360.ui.main.presenter.ForgetPwdPresenter;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonutils.ToastUitl;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * 找回密码
 * Created by Administrator on 9/19 0019.
 */

public class ForgetPwdActivity extends BaseActivity<ForgetPwdPresenter, ForgetPwdModel> implements ForgetPwdContract.View{

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.cv_add)
    CardView cvAdd;
//    @Bind(R.id.et_username)
//    EditText etUserName;
    @Bind(R.id.et_password)
    EditText etPassWord;
    @Bind(R.id.et_repeatpassword)
    EditText etRepeatPassWord;
//    @Bind(R.id.bt_go)
//    Button register;
    @Bind(R.id.tv_title)
    TextView title;

    private String mob;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mob = getIntent().getStringExtra("mob");
        title.setText("重置密码");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animateRevealClose();
                }
            }
        });
    }

    @OnClick(R.id.bt_go)
    public void onClick(){
        String passWord = etPassWord.getText().toString();
        String repeatPassword = etRepeatPassWord.getText().toString();
        if (passWord.equals("") || repeatPassword.equals("")){
            ToastUitl.show("密码不能为空!", 0);
        } else if (!passWord.equals(repeatPassword)){
            Animation shake = AnimationUtils.loadAnimation(ForgetPwdActivity.this, R.anim.shake);
            this.etRepeatPassWord.startAnimation(shake);
            this.etPassWord.startAnimation(shake);
            ToastUitl.show("两次密码输入不一致…", 0);
        }else {
            mPresenter.getForgetPwdResult("gstshuoke360", mob, repeatPassword);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                ForgetPwdActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @Override
    public void returnForgetPwdResult(Result<UpdateUserinfo> result) {
        if (result.getError_code() == 1){
            ToastUitl.show(result.getMessage(), 0);
        }else {
            ToastUitl.show(result.getMessage(), 0);
            startActivity(new Intent(this, LoginActivity.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                animateRevealClose();
            }
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
