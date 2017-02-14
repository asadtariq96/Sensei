package com.sensei.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/19/16.
 */

public class CoursesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private class CourseViewHolder extends RecyclerView.ViewHolder {
        Context context;
        View colorView;
        TextView courseName;

        CourseViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;

            colorView = itemView.findViewById(R.id.color);
            courseName = (TextView) itemView.findViewById(R.id.course_name);


        }

    }


    public CoursesListAdapter(Context context) {
        this.context = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.course_layout, parent, false);
        return new CourseViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        CourseViewHolder viewHolder = (CourseViewHolder) holder;
        CourseDataModel course = getCourseDataInstance().CoursesList.get(position);
        viewHolder.courseName.setText(course.getCourseName());
        viewHolder.colorView.setBackgroundColor(course.getCourseColorCode());


    }


    @Override
    public int getItemCount() {
        return getCourseDataInstance().CoursesList.size();
    }
}
