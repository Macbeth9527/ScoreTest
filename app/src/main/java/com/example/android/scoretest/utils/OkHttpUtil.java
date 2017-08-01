package com.example.android.scoretest.utils;

import com.example.android.scoretest.model.Course;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ALIENWARE on 2017/6/28.
 */

public class OkHttpUtil {

    static  OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .followRedirects(false)
            .followSslRedirects(false)
            //禁止OkHttp的重定向操作，我们自己处理重定向
            // .cookieJar(mCookieJar)为OkHttp设置自动携带Cookie的功能
            .build();

    public static Response loginRequest(String address , String stuID , String password){

        Response response = null ;

        FormBody.Builder builder = new  FormBody.Builder();
        builder.add("__VIEWSTATE", "dDwyODE2NTM0OTg7Oz5c58HV5k1nl8+paP2b//32KXuW2Q==");
        builder.add("txtUserName",stuID);
        builder.add("TextBox2", password);
        builder.add("txtSecretCode", "");
        builder.add("RadioButtonList1", "学生");
        builder.add("Button1", "");
        builder.add("lbLanguage", "");
        builder.add("hidPdrs", "");
        builder.add("hidsc", "");
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();

        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String getCookie(Response response){

        String cookie;

        cookie = response.headers("Set-Cookie").toString();

        String a[] = cookie.split(";");

        cookie = a[0];

        cookie = cookie.substring(1);

        return cookie;

    }

    public static String loginGet( String address , String cookie){

        String result = null;

        Request Request = new Request.Builder()
                .url(address)
                .addHeader("Cookie",cookie)
                .build();

        try {
            Response response  = okHttpClient.newCall(Request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }



    public static String doGetVIEWSTATE(String address , String Cookie , String Referer ){

        String VIEWSTATE = "";

        Request request = new Request.Builder()
                .url(address)
                .addHeader("Referer", Referer)
                .addHeader("Host", "218.94.104.201:84")
                .addHeader("Cookie", Cookie)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String htmlBody = response.body().string();
            VIEWSTATE = JoupUtil.getVIEWSTATE(htmlBody);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  VIEWSTATE;


    }


    public static List<Course> getScore(String address , String Referer, String Cookie, String VIEWSTATE){

        List<Course> courseList = new ArrayList();

        FormBody.Builder builder = new  FormBody.Builder();
        RequestBody requestBody = builder
                .add("__VIEWSTATE",VIEWSTATE)
                .add("ddlXN", "")
                .add("ddlXQ", "")
                .add("txtQSCJ","0")
                .add("txtZZCJ","100")
                .add("Button2", "历年成绩")
                .build();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Referer", Referer)
                .addHeader("Host", "218.94.104.201:84")
                .addHeader("Cookie", Cookie)
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream(), "gb2312"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                courseList = JoupUtil.parseHtml(sb.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return courseList;


    }








}
