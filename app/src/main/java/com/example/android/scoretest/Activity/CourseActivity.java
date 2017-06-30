package com.example.android.scoretest.Activity;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.android.scoretest.Adapter.CourseAdapter;
import com.example.android.scoretest.R;
import com.example.android.scoretest.model.Course;
import java.util.List;


public class CourseActivity extends AppCompatActivity {

    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        List<Course> mcourseList = (List<Course>)getIntent().getSerializableExtra("list");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);

        courseAdapter = new CourseAdapter(mcourseList);

        recyclerView.setAdapter(courseAdapter);
    }


}
