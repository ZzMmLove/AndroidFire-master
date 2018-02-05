package com.gdgst.shuoke360.ui.main.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gdgst.shuoke360.R;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonutils.ToastUitl;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 手机号验证
 * Created by Administrator on 9/18 0018.
 */

public class CheckSMS extends BaseActivity {

    @Bind(R.id.et_mob)
    EditText mob;
    @Bind(R.id.et_smscode)
    EditText smsCode;
    @Bind(R.id.btn_getcode)
    Button getCode;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.tv_title)
    TextView tvTitle;


    private TimeCount timeCount;
    private EventHandler eventHandler;
    private String phone;
    private String key;

    @Override
    public int getLayoutId() {
        return R.layout.check_smsmob;
    }

    @Override
    public void initPresenter() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {
        //MobSDK.init(this, "2100f46cdc470", "f161e1be7799d1171be48a69cbde826e");
        key = getIntent().getStringExtra("key");
        switch (key){
            case "forgetpwd":
                tvTitle.setText("忘记密码验证");
                break;
            case "regist":
                tvTitle.setText("注册验证");
                break;
        }
        timeCount = new TimeCount(60000, 1000);
        phone = mob.getText().toString().trim();
        eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE){
                    //完成回调
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        //提交验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUitl.show("验证成功!", 0);
                                if(key.equals("forgetpwd")){
                                    Intent intent = new Intent(CheckSMS.this, ForgetPwdActivity.class);
                                    intent.putExtra("mob", phone);
                                    startActivity(intent);
                                    finish();
                                }else if (key.equals("regist")){
                                    getWindow().setExitTransition(null);
                                    getWindow().setEnterTransition(null);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ActivityOptions options =
                                                ActivityOptions.makeSceneTransitionAnimation(CheckSMS.this, fab, fab.getTransitionName());
                                        Intent intent = new Intent(CheckSMS.this, RegisterActivity.class);
                                        intent.putExtra("mob", phone);
                                        startActivity(intent, options.toBundle());
                                        finish();
                                    } else {
                                        Intent intent = new Intent(CheckSMS.this, RegisterActivity.class);
                                        intent.putExtra("mob", phone);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            }
                        });
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUitl.show("验证码已发送…", 0);
                            }
                        });
                    }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else {
                    ((Throwable)data).printStackTrace();
                    Throwable throwable = (Throwable)data;
                    String message = throwable.getMessage();
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(message);
                        final String detail = jsonObject.optString("detail");
                        Log.e("TAG", "错误信息："+detail);

                        if (!TextUtils.isEmpty(detail)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUitl.show(detail, 0);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }

    @OnClick({R.id.btn_getcode, R.id.bt_next, R.id.fab})
    public void getCode(View view){
        phone = mob.getText().toString().trim();
        switch (view.getId()){
            case R.id.btn_getcode:
                if (!phone.equals("")){
                    timeCount.start();
                    //获取验证码
                    SMSSDK.getVerificationCode("86", phone);
                }else {
                    ToastUitl.show("手机号码不能为空!", 0);
                }
                break;

            case R.id.bt_next:
                String number = smsCode.getText().toString();
                SMSSDK.submitVerificationCode("86",phone,number);
                break;

            case R.id.fab:
                finish();
                break;
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {//计时完毕时触发
            getCode.setText("获取验证码");
            getCode.setClickable(true);
            getCode.setBackgroundResource(R.drawable.bt_shape);
        }

        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            getCode.setClickable(false);
            getCode.setBackgroundResource(R.drawable.bt_shape_2);
            getCode.setText(millisUntilFinished /1000+"秒后可重新获取");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
