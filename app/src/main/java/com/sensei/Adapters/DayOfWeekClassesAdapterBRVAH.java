package com.sensei.Adapters;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;

import java.util.List;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by Asad on 10-Jan-17.
 */

public class DayOfWeekClassesAdapterBRVAH extends BaseQuickAdapter<ClassDataModel, BaseViewHolder> {

    public DayOfWeekClassesAdapterBRVAH(int layoutResId, List<ClassDataModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ClassDataModel classDataModel) {

        View colorView;
        TextView courseName;
        TextView mDuration;
        TextView mLocation;
        TextView mTime;
        TextView RemainingTime;
        TextView classType;

        colorView = baseViewHolder.getView(R.id.color);
        courseName = baseViewHolder.getView(R.id.course_name);
        mDuration = baseViewHolder.getView(R.id.duration);
        mLocation = baseViewHolder.getView(R.id.location);
        mTime = baseViewHolder.getView(R.id.time);
        RemainingTime = baseViewHolder.getView(R.id.remaining_time);
        classType = baseViewHolder.getView(R.id.class_type);

        CourseDataModel parentCourse = getCourseDataInstance().getCourseOfClass(classDataModel);
//        for (CourseDataModel courseDataModel : getCourseDataInstance().CoursesList) {
//            if (courseDataModel.getClasses().contains(classDataModel)) {
//                parentCourse = courseDataModel;
//                break;
//            }
//        }


        courseName.setText(parentCourse.getCourseName());
        colorView.setBackgroundColor(parentCourse.getCourseColorCode());
        mDuration.setText(classDataModel.getDuration());
        mTime.setText(classDataModel.getStartTimeOriginal().toString("h:mm a")
                + " to "
                + classDataModel.getEndTimeOriginal().toString("h:mm a"));
        mLocation.setText(classDataModel.getLocation());
        RemainingTime.setVisibility(View.GONE);
        classType.setText(classDataModel.getClassType().toLowerCase());

    }
}
