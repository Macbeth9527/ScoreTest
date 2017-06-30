package com.example.android.scoretest.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.scoretest.R;
import com.example.android.scoretest.utils.JoupUtil;
import com.example.android.scoretest.utils.OkHttpUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    public static final int Login_Failed = 0;
    public static final int Login_Success = 1;

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button loginButton;

    private String VIEWSTATE;

    private String Cookie;

    private String stuID;

    private String password;

    private String loginUrl = "http://218.94.104.201:84/default2.aspx";



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Login_Failed :
                    Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                    break;
                case Login_Success :
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initView();



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuID = accountEdit.getText().toString();
                if (stuID.isEmpty()) {
                    accountEdit.setError("学号不能为空");
                }else if (stuID.length() != 11){
                    accountEdit.setError("学号不为11位");
                }


                password = passwordEdit.getText().toString();
                if (password.isEmpty()) {
                    passwordEdit.setError("密码不能为空");
                }

                getFuck();

            }
        });

    }


    private void getFuck(){

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
        OkHttpUtil.sendRequestPost(loginUrl,VIEWSTATE,stuID,password,Cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String urlString = response.request().url().toString();
                final String body = response.body().string();

                if (urlString.equals(loginUrl)){

                    message.what = Login_Failed;

                    handler.sendMessage(message);

                }else{

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("urlString", urlString);
                    intent.putExtra("cookie", Cookie);
                    intent.putExtra("stuId", stuID);
                    intent.putExtra("stuName", JoupUtil.getStuName(body));

                    message.what = Login_Success;
                    handler.sendMessage(message);

                    startActivity(intent);

                }

            }
        });


    }

    private void initView(){

        accountEdit = (EditText)findViewById(R.id.input_account);

        passwordEdit = (EditText)findViewById(R.id.input_password);

        loginButton = (Button)findViewById(R.id.btn_login);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.login_toolbar);

        setSupportActionBar(toolbar);


    }






}
