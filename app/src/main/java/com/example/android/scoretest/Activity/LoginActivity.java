package com.example.android.scoretest.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scoretest.R;
import com.example.android.scoretest.utils.JoupUtil;
import com.example.android.scoretest.utils.OkHttpUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private static final int Login_Failed = 0;
    private static final int Login_Success = 1;
    private static final int Login_Failed_SysBusy = 2;

    private String VIEWSTATE;

    private String Cookie;

    private String stuID;

    private String password;


    private String loginUrl = "http://218.94.104.201:84/default2.aspx";

    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private TextInputLayout mAccount, mPassword;

    private String url ;




    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Login_Failed:
                    showToast("登陆失败");
                    break;
                case Login_Success:
                    showToast("登陆成功");
                    break;
                case Login_Failed_SysBusy:
                    showToast("登陆失败，系统正忙");
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public void widgetClick(View v) {

        switch (v.getId()){
            case R.id.main_btn_login :

                stuID = mAccount.getEditText().getText().toString();

                if (stuID.equals("")){
                    mAccount.setError("账号不能为空");
                    break;
                }else if (stuID.length()!= 11){
                    mAccount.setError("账号必须为11位");
                    break;
                }
                password = mPassword.getEditText().getText().toString();
                if (password.equals("")){
                    mPassword.setError("账号不能为空");
                    break;
                }

                getInfo();

                break;
            default:
                break;

        }

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(View view) {

        mBtnLogin = $(R.id.main_btn_login);

        progress = $(R.id.layout_progress);

        mInputLayout = $(R.id.input_layout);

        mAccount = $(R.id.input_layout_account);

        mPassword = $(R.id.input_layout_password);

    }

    @Override
    public void setListener() {

        mBtnLogin.setOnClickListener(this);

    }

    @Override
    public void doBusiness(Context mContext) {



    }

    private void getInfo() {

        OkHttpUtil.sendRequestGet(loginUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Cookie = OkHttpUtil.getCookieByHand(response);

                VIEWSTATE = JoupUtil.getVIEWSTATE(response.body().string());

                login();

            }
        });


    }


    private void login() throws UnsupportedEncodingException {

        final Message message = new Message();

        //发送post请求
        OkHttpUtil.sendRequestPost(loginUrl, VIEWSTATE, stuID, password, Cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String urlString = response.request().url().toString();
                final String body = response.body().string();

                if (urlString.equals(loginUrl)) {

                    message.what = Login_Failed;

                    handler.sendMessage(message);

                } else if (urlString.equals("http://218.94.104.201:84/zdy.htm?aspxerrorpath=/default2.aspx")) {
                    message.what = Login_Failed_SysBusy;
                    handler.sendMessage(message);
                } else {

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("urlString", urlString);
                    intent.putExtra("cookie", Cookie);
                    intent.putExtra("stuId", stuID);
                    intent.putExtra("stuName", JoupUtil.getStuName(body));

                    message.what = Login_Success;
                    handler.sendMessage(message);

                    startActivity(intent);
                    finish();

                }

            }
        });


    }






}
