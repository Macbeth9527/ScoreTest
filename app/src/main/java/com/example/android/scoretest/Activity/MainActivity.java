package com.example.android.scoretest.Activity;

import android.content.Context;

import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scoretest.Adapter.CourseAdapter;
import com.example.android.scoretest.R;
import com.example.android.scoretest.model.Course;
import com.example.android.scoretest.utils.OkHttpUtil;
import com.example.android.scoretest.utils.mUtil;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends BaseActivity {

    private String loginScoreUrl;

    private String cookie;

    private String urlString;

    private String VIEWSTATE;

    private DrawerLayout mDrawerLayout;

    private NavigationView navView;

    private TextView stuIDText;

    private TextView stuNameText;


    private List<Course> courseList = new ArrayList<>();

    private String stuId;
    private String stuName;


    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void initParms(Bundle parms) {

        cookie = parms.getString("cookie");
        Log.d("cookie", cookie);
        urlString = parms.getString("urlString");
        Log.d("urlString", urlString);
        stuId = parms.getString("stuId");
        Log.d("stuId", stuId);
        stuName = parms.getString("stuName");
        Log.d("stuName", stuName);


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


    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

        stuIDText.setText(stuId);

        stuNameText.setText(stuName);


        try {
            loginScoreUrl = "http://218.94.104.201:84/xscj.aspx?xh=" + stuId
                    + "&xm=" + URLEncoder.encode(stuName, "GB2312") + "&gnmkdm=N121605";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new ScoreTask().execute();


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_score_all:

                        mDrawerLayout.closeDrawers();
                        break;


                    case R.id.nav_GPA_get:

                        String gpa = mUtil.countGPA(courseList).toString();
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("平均学分绩点为")
                                .setContentText(gpa)
                                .show();
                        mDrawerLayout.closeDrawers();
                        break;

                    default:
                        break;
                }

                return true;
            }
        });

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


    private class ScoreTask extends AsyncTask<Void, Void, Boolean> {

        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {

            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycle_view);


                GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);

                recyclerView.setLayoutManager(layoutManager);

                CourseAdapter adapter = new CourseAdapter(courseList);

                recyclerView.setAdapter(adapter);

                pDialog.dismiss();


            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            VIEWSTATE = OkHttpUtil.doGetVIEWSTATE(loginScoreUrl, cookie, urlString);

            courseList = OkHttpUtil.getScore(loginScoreUrl, loginScoreUrl, cookie, VIEWSTATE);


            return true;

        }
    }


}
