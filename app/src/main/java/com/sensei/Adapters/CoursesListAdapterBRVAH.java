package com.sensei.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;

import java.util.List;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/19/16.
 */

public class CoursesListAdapterBRVAH extends BaseQuickAdapter<CourseDataModel, BaseViewHolder> {

    public CoursesListAdapterBRVAH(int layoutResId, List<CourseDataModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CourseDataModel courseDataModel) {

        View colorView;
        TextView courseName;
        TextView courseInstructor;

        colorView = baseViewHolder.getView(R.id.color);
        courseName = baseViewHolder.getView(R.id.course_name);
        courseInstructor = baseViewHolder.getView(R.id.course_instructor);


        courseName.setText(courseDataModel.getCourseName());
        if (!courseDataModel.getInstructor().isEmpty()) {
            courseInstructor.setVisibility(View.VISIBLE);
            courseInstructor.setText(courseDataModel.getInstructor());
        } else
            courseInstructor.setVisibility(View.GONE);
        colorView.setBackgroundColor(courseDataModel.getCourseColorCode());

    }


}
