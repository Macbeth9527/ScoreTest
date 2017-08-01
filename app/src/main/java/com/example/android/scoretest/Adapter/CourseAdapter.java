package com.example.android.scoretest.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.scoretest.R;
import com.example.android.scoretest.model.Course;

import java.util.List;

/**
 * Created by ALIENWARE on 2017/7/28.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<Course> mcourseVOlist;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView courseID;

        TextView courseName;

        public ViewHolder(View itemView) {
            super(itemView);
            courseID = (TextView)itemView.findViewById(R.id.main_coureID_vo);

            courseName = (TextView)itemView.findViewById(R.id.main_coureName_vo);

        }
    }

    public CourseAdapter(List<Course> list){
        mcourseVOlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item,parent,false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Course courseVO = mcourseVOlist.get(position);

        holder.courseID.setText(courseVO.getcID());
        holder.courseName.setText(courseVO.getcName());

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



    }

    @Override
    public int getItemCount() {
        return mcourseVOlist.size();
    }
}
