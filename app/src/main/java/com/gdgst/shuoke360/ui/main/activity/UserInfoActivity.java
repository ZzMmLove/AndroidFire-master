package com.gdgst.shuoke360.ui.main.activity;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.ui.main.contract.UpdateUserInfoContract;
import com.gdgst.shuoke360.ui.main.model.UpdateUserInfoModel;
import com.gdgst.shuoke360.ui.main.presenter.UpdateUserInfoPresenter;
import com.gdgst.shuoke360.ui.main.view.EditorDialog;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonutils.ToastUitl;
import com.gdgst.common.commonwidget.NormalTitleBar;


import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by Administrator on 9/5 0005.
 */

public class UserInfoActivity extends BaseActivity<UpdateUserInfoPresenter, UpdateUserInfoModel> implements UpdateUserInfoContract.View{

    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    @Bind(R.id.tv_username)
    TextView userName;
    @Bind(R.id.tv_nickname)
    TextView nickName;
    @Bind(R.id.tv_sex)
    TextView sex;
    @Bind(R.id.tv_type)
    TextView type;
    @Bind(R.id.tv_school)
    TextView school;
    @Bind(R.id.tv_class)
    TextView banJi;
    @Bind(R.id.tv_email)
    TextView email;
    @Bind(R.id.tv_address)
    TextView address;
    @Bind(R.id.tv_remark)
    TextView remark;
    @Bind(R.id.btn_pos)
    Button btnPos;
    private String accessToken;
    private SharedPreferences sharedPreferences;
    private Map<String, String> updateMap;


    @Override
    public int getLayoutId() {
        return R.layout.fra_four_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        updateMap = new HashMap<>();
        sharedPreferences = mContext.getSharedPreferences(AppConstant.SHAREPREFRENECE_USER, MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", "");
        if (!accessToken.equals("")){
            bySharePrefrence();
        }
        ntb.setTvLeftVisiable(false);
        ntb.setTitleText("详细信息");
        ntb.setBackVisibility(true);
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void bySharePrefrence() {
        userName.setText(sharedPreferences.getString("name", ""));
        nickName.setText(sharedPreferences.getString("nickname", ""));
        String mySex = sharedPreferences.getInt("sex",0) == 0 ? "女": "男";
        sex.setText(mySex);
        type.setText(sharedPreferences.getString("type", ""));
        school.setText(sharedPreferences.getString("school", ""));
        banJi.setText(sharedPreferences.getString("className", ""));
        email.setText(sharedPreferences.getString("email", ""));
        address.setText(sharedPreferences.getString("addr", ""));
        remark.setText(sharedPreferences.getString("remark", ""));
    }

    @OnClick({R.id.rl_account_username, R.id.rl_switch_nickname, R.id.rl_switch_type,R.id.rl_control_school,
               R.id.rl_class, R.id.rl_email, R.id.rl_switch_address, R.id.rl_remark})
    public void onClickUpdate(View view){
        EditorDialog editorDialog = new EditorDialog(this);
        switch (view.getId()){
            case R.id.rl_account_username:
                editorDialog.setOnPosNegClickListener(new EditorDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(String value) {
                        updateMap.put("name", value);
                        userName.setText(value);
                        isVisiable();
                    }
                });
                editorDialog.setCanceledOnTouchOutside(true);
                editorDialog.show();
                break;
            case R.id.rl_switch_nickname:
                editorDialog.setOnPosNegClickListener(new EditorDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(String value) {
                        updateMap.put("nickname", value);
                        nickName.setText(value);
                        isVisiable();
                    }
                });
                editorDialog.setCanceledOnTouchOutside(true);
                editorDialog.show();
                break;
            case R.id.rl_sex:

                break;
            case R.id.rl_switch_type:

                break;
            case R.id.rl_control_school:
                editorDialog.setOnPosNegClickListener(new EditorDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(String value) {
                        updateMap.put("school", value);
                        school.setText(value);
                        isVisiable();
                    }
                });
                editorDialog.setCanceledOnTouchOutside(true);
                editorDialog.show();
                break;
            case R.id.rl_class:
                editorDialog.setOnPosNegClickListener(new EditorDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(String value) {
                        updateMap.put("className", value);
                        banJi.setText(value);
                        isVisiable();
                    }
                });
                editorDialog.setCanceledOnTouchOutside(true);
                editorDialog.show();
                break;
            case R.id.rl_email:
                editorDialog.setOnPosNegClickListener(new EditorDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(String value) {
                        updateMap.put("email", value);
                        email.setText(value);
                        isVisiable();
                    }
                });
                editorDialog.setCanceledOnTouchOutside(true);
                editorDialog.show();
                break;
            case R.id.rl_switch_address:
                editorDialog.setOnPosNegClickListener(new EditorDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(String value) {
                        updateMap.put("addr", value);
                        address.setText(value);
                        isVisiable();
                    }
                });
                editorDialog.setCanceledOnTouchOutside(true);
                editorDialog.show();
                break;
            case R.id.rl_remark:
                editorDialog.setOnPosNegClickListener(new EditorDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(String value) {
                        updateMap.put("remark", value);
                        remark.setText(value);
                        isVisiable();
                    }
                });
                editorDialog.setCanceledOnTouchOutside(true);
                editorDialog.show();
                break;
        }
    }

    @OnClick(R.id.btn_pos)
    public void onClickPos(){
        JSONObject posJson = new JSONObject();
        for (Map.Entry<String, String> entry: updateMap.entrySet()){
            posJson.put(entry.getKey(), entry.getValue());
        }
        String posJsonStr = posJson.toJSONString().toString();
        Log.e("TAG", "posJsonStr:"+posJsonStr);

       // RequestBody body = RequestBody.create(MediaType.parse("application/json"), posJsonStr);
        mPresenter.loadUpdateResult("gstshuoke360", accessToken, posJsonStr);
        updateMap.clear();
        btnPos.setVisibility(View.GONE);
    }

    /**
     * 判断有修改
     */
    private void isVisiable() {
        if (!updateMap.isEmpty()){
            btnPos.setVisibility(View.VISIBLE);
        }else btnPos.setVisibility(View.GONE);
    }


    @Override
    public void returnUpdateResult(Result<UpdateUserinfo> result) {
//        Log.e("TAg", "返回结果："+result.getMessage());
        if (result.getError_code() == 0){
            ToastUitl.show("修改成功", 0);
        }else ToastUitl.show("修改失败", 0);
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        //ToastUitl.show(msg, 0);
    }
}
