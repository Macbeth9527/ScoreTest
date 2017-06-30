package com.example.android.scoretest.model;

import java.io.Serializable;

/**
 * Created by ALIENWARE on 2017/6/20.
 */

public class Course implements Serializable {

    //课程id
    private String cID;

    //课程名
    private String cName;

    //考试成绩
    private String testScore;

    //最终成绩
    private String finScore;

    //补考成绩
    private String secScore;

    //学分
    private String credit;

    public Course(){

    }

    public Course(String cID, String cName, String testScore, String finScore, String secScore, String credit) {
        this.cID = cID;
        this.cName = cName;
        this.testScore = testScore;
        this.finScore = finScore;
        this.secScore = secScore;
        this.credit = credit;
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getTestScore() {
        return testScore;
    }

    public void setTestScore(String testScore) {
        this.testScore = testScore;
    }

    public String getFinScore() {
        return finScore;
    }

    public void setFinScore(String finScore) {
        this.finScore = finScore;
    }

    public String getSecScore() {
        return secScore;
    }

    public void setSecScore(String secScore) {
        this.secScore = secScore;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
