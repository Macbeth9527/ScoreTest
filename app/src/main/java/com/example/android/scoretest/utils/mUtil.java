package com.example.android.scoretest.utils;

import com.example.android.scoretest.model.Course;

import java.util.List;

/**
 * Created by ALIENWARE on 2017/7/24.
 */

public class mUtil {

    public static Float countGPA(List<Course> courseList){

        float credit = 0.0f ;
        float fin_score_all = 0.0f ;
        float GP_all = 0.0f;
        float score_temple = 0.0f;
        float GPA_final = 0.0f;
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
            GP_all = score_temple * Float.parseFloat(courseList.get(num).getCredit()) + GP_all;
        }
        if (GP_all/credit < 60){
            GPA_final = 0;

        }else {
            GPA_final = ( GP_all / credit - 50 ) * 0.1f ;
            GPA_final = (float)(Math.round(GPA_final*100))/100;
        }

        return GPA_final;
    }
}
