package com.example.android.scoretest.utils;

import com.example.android.scoretest.model.Course;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;



public class JoupUtil {

    public static List<Course> parseHtml(String html) {

        List<Course> courseList = new ArrayList<>();
        Document document= Jsoup.parse(html);
        //得到存放成绩的表格
        Element element=document.getElementsByClass("datelist").get(0);
        //得到所有的行
        Elements trs=element.getElementsByTag("tr");
        for (int i=1;i<trs.size();i++){
            //第一行是列名这里不需要
            Element e=trs.get(i);
            //得到一行中的所有列
            Elements tds=e.getElementsByTag("td");
            String cID=tds.get(0).text();
            String cName=tds.get(1).text();
            String testScore=tds.get(3).text();
            String finScore=tds.get(4).text();
            String secScore=tds.get(6).text();
            String credit=tds.get(8).text();
            Course course=new Course(cID,cName,testScore,finScore,secScore,credit);
            courseList.add(course);
        }

        return courseList;

    }

    public static String getVIEWSTATE(String html){

        Document document = Jsoup.parse(html);
        String VIEWSTATE = document.select("input[name=__VIEWSTATE]").val();
        return VIEWSTATE;
    }

    public static String getStuName(String html){

        String stuName;
        stuName = Jsoup.parse(html).getElementById("xhxm").text();
        String a[] = stuName.split("同");
        stuName = a[0];
        return stuName;

    }

}
