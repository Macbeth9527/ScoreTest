package com.example.android.scoretest.Activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scoretest.R;
import com.example.android.scoretest.model.Course;
import com.example.android.scoretest.utils.JoupUtil;
import com.example.android.scoretest.utils.OkHttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String loginScoreUrl;

    private String Cookie;

    private String UrlString;

    private String VIEWSTATE;

    private DrawerLayout mDrawerLayout;

    private NavigationView navView;

    private TextView stuIDText;

    private TextView stuNameText;

    private WebView webView;

    private long exitTime = 0;


    @Override
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

        loginScore(UrlString);


    }

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
                                    mDrawerLayout.closeDrawers();
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

                    List<Course> courseList = JoupUtil.parseHtml(sb.toString());
                    Intent intent = new Intent(MainActivity.this, CourseActivity.class);
                    intent.putExtra("list", (Serializable) (courseList));
                    startActivity(intent);
                }
            }
        });

    }


    private void initViews() {

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_done);
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
}
