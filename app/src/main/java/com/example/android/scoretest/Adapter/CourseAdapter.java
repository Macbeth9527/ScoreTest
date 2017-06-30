package com.example.android.scoretest.Adapter;

import android.content.Context;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.android.scoretest.R;
import com.example.android.scoretest.model.Course;

import java.util.List;

/**
 * Created by ALIENWARE on 2017/6/7.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private Context mcontext;

    private List<Course> mCourseLists;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView courseID;
        TextView courseName;
        TextView scoreTest;
        TextView scoreFinal;
        TextView scoreSec;
        TextView criditNo;



        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            courseID = (TextView)view.findViewById(R.id.course_id);
            courseName = (TextView)view.findViewById(R.id.course_name);
            scoreTest = (TextView)view.findViewById(R.id.score_test);
            scoreFinal = (TextView)view.findViewById(R.id.score_final);
            scoreSec = (TextView)view.findViewById(R.id.score_sec);
            criditNo = (TextView)view.findViewById(R.id.cridit_no);
        }
    }

    public CourseAdapter(List<Course> courseLists) {
        mCourseLists = courseLists;
    }

    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mcontext == null){
            mcontext = parent.getContext();
        }

        View view = LayoutInflater.from(mcontext).inflate(R.layout.course_item,parent,false);

        final ViewHolder holder = new ViewHolder(view);

       /* holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitLists.get(position);
                Intent intent = new Intent(mcontext,FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME,fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID,fruit.getImageid());
                mcontext.startActivity(intent);

            }
        });
        */


        return holder;
    }

    @Override
    public void onBindViewHolder(CourseAdapter.ViewHolder holder, int position) {
        Course course = mCourseLists.get(position);
        holder.courseID.setText(course.getcID());
        holder.courseName.setText(course.getcName());
        holder.scoreTest.setText(course.getTestScore());
        holder.scoreFinal.setText(course.getFinScore());
        holder.scoreSec.setText(course.getSecScore());
        holder.criditNo.setText(course.getCredit());

    }

    @Override
    public int getItemCount() {
        return mCourseLists.size();
    }
}
