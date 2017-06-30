package com.example.android.scoretest.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.scoretest.R;
import com.example.android.scoretest.model.Course;
import com.example.android.scoretest.utils.JoupUtil;
import com.example.android.scoretest.utils.OkHttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String loginScoreUrl;

    private Button getAllScore;

    private String Cookie;

    private String UrlString;

    private String VIEWSTATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        Cookie = getIntent().getStringExtra("cookie");
        UrlString = getIntent().getStringExtra("urlString");
        loginScoreUrl = "http://218.94.104.201:84/xscj.aspx?xh=" + getIntent().getStringExtra("stuId")
                + "&xm=" + getIntent().getStringExtra("stuName") + "&gnmkdm=N121605";

        loginScore(UrlString);



    }

    private void loginScore(String urlString){

        OkHttpUtil.sendRequestGet(loginScoreUrl, Cookie, urlString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){

                    final String htmlBody = response.body().string();
                    VIEWSTATE = JoupUtil.getVIEWSTATE(htmlBody);


                    getAllScore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                searchAllScore();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

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

                if (response.isSuccessful()){
                    BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "gb2312"));
                    StringBuilder sb=new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    List<Course> courseList = JoupUtil.parseHtml(sb.toString());
                    Intent intent = new Intent(MainActivity.this,CourseActivity.class);
                    intent.putExtra("list",(Serializable) (courseList));
                    startActivity(intent);
                }
            }
        });

    }


    private void initViews(){
        getAllScore = (Button) findViewById(R.id.search_score_all);
    }




}
