package com.example.android.scoretest.utils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ALIENWARE on 2017/6/28.
 */

public class OkHttpUtil {

    public static String getCookieByHand(Response response){
        String cookie;
        cookie = response.headers("Set-Cookie").toString();
        String a[] = cookie.split(";");
        cookie = a[0];
        cookie = cookie.substring(1);

        return cookie;
    }

    public static void sendRequestGet(String address , okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendRequestGet(String address , String Cookie , String Referer , okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Referer", Referer)
                .addHeader("Host", "218.94.104.201:84")
                .addHeader("Cookie", Cookie)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }




    public static void sendRequestPost(String address , String VIEWSTATE, String stuID, String password,String Cookie, okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        //构建表单数据
        FormBody.Builder builder = new  FormBody.Builder();
        builder.add("__VIEWSTATE", VIEWSTATE);
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
                .addHeader("Cookie", Cookie)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendRequestPost(String address ,String Referer, String Cookie, String VIEWSTATE , okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        //构建表单数据
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
        okHttpClient.newCall(request).enqueue(callback);
    }


}
