package com.example.android.scoretest.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scoretest.R;
import com.example.android.scoretest.model.Course;
import com.example.android.scoretest.utils.JoupUtil;
import com.example.android.scoretest.utils.OkHttpUtil;
import com.example.android.scoretest.utils.mUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private String loginScoreUrl;

    private String Cookie;

    private String UrlString;

    private String VIEWSTATE;

    private DrawerLayout mDrawerLayout;

    private NavigationView navView;

    private TextView stuIDText;

    private TextView stuNameText;

    private WebView webView;

    private List<Course> courseList = new ArrayList<>();

    private long exitTime = 0;



    @Override
    public void widgetClick(View v) {

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
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        navView = (NavigationView) findViewById(R.id.nav_view);

        View headerLayout = navView.inflateHeaderView(R.layout.main_header);

        stuIDText = (TextView) headerLayout.findViewById(R.id.main_stuID);

        stuNameText = (TextView) headerLayout.findViewById(R.id.main_stuName);

        webView = (WebView) findViewById(R.id.web_view_index_sanjiang);

        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("http://www.sju.js.cn/");

    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

        String stuId = getIntent().getStringExtra("stuId");
        String stuName = getIntent().getStringExtra("stuName");

        stuIDText.setText(stuId);
        stuNameText.setText(stuName);


        Cookie = getIntent().getStringExtra("cookie");
        UrlString = getIntent().getStringExtra("urlString");
        try {
            loginScoreUrl = "http://218.94.104.201:84/xscj.aspx?xh=" + stuId
                    + "&xm=" + URLEncoder.encode(stuName, "GB2312") + "&gnmkdm=N121605";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        //loginScore(UrlString);
        getDate(UrlString);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_score_all:

                        if (courseList.isEmpty()) {
                            delayProDia();
                            break;
                        } else {

                            Intent intent = new Intent(MainActivity.this, CourseActivity.class);
                            intent.putExtra("list", (Serializable) courseList);
                            startActivity(intent);

                            break;
                        }
                    case R.id.nav_GPT_get:

                        if (courseList.isEmpty()) {
                            delayProDia();
                            break;
                        } else {
                            float GPA = mUtil.countGPA(courseList);
                            showToast("平局绩点为："+GPA);

                            break;
                        }
                    default:
                        break;
                }

                return true;
            }
        });

    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        initViews();

        String stuId = getIntent().getStringExtra("stuId");
        String stuName = getIntent().getStringExtra("stuName");

        stuIDText.setText(stuId);
        stuNameText.setText(stuName);


        Cookie = getIntent().getStringExtra("cookie");
        UrlString = getIntent().getStringExtra("urlString");
        try {
            loginScoreUrl = "http://218.94.104.201:84/xscj.aspx?xh=" + stuId
                    + "&xm=" + URLEncoder.encode(stuName, "GB2312") + "&gnmkdm=N121605";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        //loginScore(UrlString);
        getDate(UrlString);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_score_all:

                        if (courseList.isEmpty()) {
                            delayProDia();
                            break;
                        } else {

                            Intent intent = new Intent(MainActivity.this, CourseActivity.class);
                            intent.putExtra("list", (Serializable) courseList);
                            startActivity(intent);

                            break;
                        }
                    case R.id.nav_GPT_get:

                        if (courseList.isEmpty()) {
                            delayProDia();
                            break;
                        } else {
                            float credit = 0.0f ;
                            float fin_score_all = 0.0f ;
                            float GPT_all = 0.0f;
                            float score_temple = 0.0f;
                            float GPT_final = 0.0f;
                            //String all = String.valueOf(courseList.size());
                            for (int num = 0 ; num < courseList.size() ; num++){
                                try {
                                    score_temple = Float.parseFloat(courseList.get(num).getFinScore());

                                    if (score_temple < 60.0f){
                                        score_temple = Float.parseFloat(courseList.get(num).getSecScore());
                                        if (score_temple < 60.0f){
                                            score_temple = 0.0f;
                                        }

                                    }

                                }catch (Exception e){
                                    continue;

                                }
                                fin_score_all = score_temple + fin_score_all;
                                credit = Float.parseFloat(courseList.get(num).getCredit()) + credit ;
                                GPT_all = score_temple * Float.parseFloat(courseList.get(num).getCredit()) + GPT_all;
                            }
                            if (GPT_all/credit < 60){
                                GPT_final = 0;

                            }else {
                                GPT_final = ( GPT_all/credit - 50 ) * 0.1f ;
                                GPT_final = (float)(Math.round(GPT_final*100))/100;
                            }

                            Toast.makeText(MainActivity.this,"您的绩点为：" + GPT_final ,Toast.LENGTH_SHORT ).show();

                            break;
                        }
                    default:
                        break;
                }

                return true;
            }
        });


    }*/



/*

    private void loginScore(String urlString) {

        OkHttpUtil.sendRequestGet(loginScoreUrl, Cookie, urlString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {

                    final String htmlBody = response.body().string();
                    VIEWSTATE = JoupUtil.getVIEWSTATE(htmlBody);


                    navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.nav_score_all:
                                    try {
                                        searchAllScore();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                    break;
                                case R.id.nav_GPT_get:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "u clicked the gpt button, but nothing happened", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }

                            return true;
                        }
                    });


                }
            }
        });

    }


    private void searchAllScore() throws UnsupportedEncodingException {


        OkHttpUtil.sendRequestPost(loginScoreUrl, loginScoreUrl, Cookie, VIEWSTATE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "gb2312"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    courseList = JoupUtil.parseHtml(sb.toString());
                    Intent intent = new Intent(MainActivity.this,CourseActivity.class);
                    intent.putExtra("list",(Serializable) (courseList));
                    startActivity(intent);

                }
            }
        });

    }
    */

    private void getDate(String urlString) {

        OkHttpUtil.sendRequestGet(loginScoreUrl, Cookie, urlString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {

                    final String htmlBody = response.body().string();
                    VIEWSTATE = JoupUtil.getVIEWSTATE(htmlBody);

                    OkHttpUtil.sendRequestPost(loginScoreUrl, loginScoreUrl, Cookie, VIEWSTATE, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            if (response.isSuccessful()) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "gb2312"));
                                StringBuilder sb = new StringBuilder();
                                String line;
                                while ((line = br.readLine()) != null) {
                                    sb.append(line);
                                }

                                courseList = JoupUtil.parseHtml(sb.toString());

                            }
                        }
                    });
                }
            }
        });


    }


    /*private void initViews() {

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        navView = (NavigationView) findViewById(R.id.nav_view);

        View headerLayout = navView.inflateHeaderView(R.layout.main_header);

        stuIDText = (TextView) headerLayout.findViewById(R.id.main_stuID);

        stuNameText = (TextView) headerLayout.findViewById(R.id.main_stuName);

        webView = (WebView) findViewById(R.id.web_view_index_sanjiang);

        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("http://www.sju.js.cn/");


    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(MainActivity.this, "u clicked the settings button, but nothing happened", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            default:
                break;
        }

        return true;
    }


    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }

        }
    }

    public void delayProDia() {
        final ProgressDialog proDia = ProgressDialog.show(MainActivity.this, "Loading", "正在获取数据，请稍后...", true, false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    proDia.dismiss();//关闭ProgressDialog
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


}
