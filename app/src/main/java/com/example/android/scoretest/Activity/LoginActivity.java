package com.example.android.scoretest.Activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scoretest.R;
import com.example.android.scoretest.utils.JellyInterpolator;
import com.example.android.scoretest.utils.JoupUtil;
import com.example.android.scoretest.utils.OkHttpUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;


import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private static final Integer Login_Failed = 0;
    private static final Integer Login_Success = 1;
    private static final Integer Login_Failed_SysBusy = 2;

    private String stuID;

    private String password;

    private String loginUrl = "http://218.94.104.201:84/default2.aspx";

    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private TextInputLayout mAccount, mPassword;

    private String url ;

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

                new LoginTask().execute();

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

    private class LoginTask extends AsyncTask<Void,Void,Integer>{

        private String cookie = null;

        private String responseBody = null;

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            progressAnimator(progress);
            mInputLayout.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Integer result) {

            if ( result == Login_Failed){
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("登陆失败")
                        .setContentText("可能是账号密码输错了？")
                        .setConfirmText("重新登陆")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                progress.setVisibility(View.INVISIBLE);
                mInputLayout.setVisibility(View.VISIBLE);
            }
            if ( result == Login_Failed_SysBusy){
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("登陆失败")
                        .setContentText("对不起，服务器又大姨妈了")
                        .setConfirmText("稍后再试")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

                progress.setVisibility(View.INVISIBLE);
                mInputLayout.setVisibility(View.VISIBLE);
            }
            if ( result == Login_Success){

                final Bundle bundle = new Bundle();

                bundle.putString("urlString",url);
                bundle.putString("cookie",cookie);
                bundle.putString("stuId",stuID);
                bundle.putString("stuName",JoupUtil.getStuName(responseBody));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                            startActivity(MainActivity.class, bundle);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }

        }


        @Override
        protected Integer doInBackground(Void... params) {

            Response response = OkHttpUtil.loginRequest(loginUrl,stuID,password);

            cookie = OkHttpUtil.getCookie(response);

            if (response.isRedirect()){

                url = "http://218.94.104.201:84" + response.header("Location");

                if (url.equals("http://218.94.104.201:84/zdy.htm?aspxerrorpath=/default2.aspx")){

                    return Login_Failed_SysBusy;

                }else {

                    responseBody = OkHttpUtil.loginGet( url , cookie );

                    return Login_Success;
                }

            }

            return Login_Failed;
        }
    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);

        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);

        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);

        animator3.setDuration(2000);

        animator3.setInterpolator(new JellyInterpolator());

        animator3.start();

    }






}
