package com.gdgst.shuoke360.ui.main.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.UpdatePwdContract;
import com.gdgst.shuoke360.ui.main.model.UpdatePwdModel;
import com.gdgst.shuoke360.ui.main.presenter.UpdatePwdPresenter;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonutils.ToastUitl;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 9/16 0016.
 */

public class UpdatePasswordActivity extends BaseActivity<UpdatePwdPresenter, UpdatePwdModel> implements UpdatePwdContract.View{

    @Bind(R.id.et_oldpwd)
    EditText oldPwd;
    @Bind(R.id.et_newpwd)
    EditText newPwd;
    private SharedPreferences sharedPreferences;
    private String accessToken;
    private UpdateUserinfo updateUserinfo;
    @Override
    public int getLayoutId() {
        return R.layout.act_update_password;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        sharedPreferences = getSharedPreferences(AppConstant.SHAREPREFRENECE_USER, MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", "");
        //updateUserinfo = new UpdateUserinfo();
    }

    @OnClick(R.id.bt_go)
    public void onClickGo(){
        String oldP = oldPwd.getText().toString().trim();
        String newP = newPwd.getText().toString().trim();
        if (oldP.equals("") || newP.equals("")){
            ToastUitl.show("新旧密码不能为空!", 0);
        }else {
            mPresenter.loadUpdatePwdResult("gstshuoke360", accessToken, oldP, newP);
        }
    }

    @OnClick(R.id.fab)
    public void fabOnClick(){
        finish();
    }

    @Override
    public void returnUpdatePwdResult(Result<UpdateUserinfo> result) {
        updateUserinfo = result.getData();
        if (updateUserinfo != null && updateUserinfo.getError_code() == 0){
            startActivity(new Intent(this, LoginActivity.class));
            ToastUitl.show("修改成功，请重新登录!", 0);
            finish();
        }else {
            ToastUitl.show("修改失败!", 0);
        }
        if (result.getError_code() == 1){
            ToastUitl.show("原密码错误!", 0);
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
