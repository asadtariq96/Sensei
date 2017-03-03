package com.sensei.assistant.Adapters;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.List;

import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/19/16.
 */

public class DashboardClassesAdapterBRVAH extends BaseQuickAdapter<ClassDataModel, BaseViewHolder> {

    public DashboardClassesAdapterBRVAH(int layoutResId, List<ClassDataModel> data) {
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


        courseName.setText(parentCourse.getCourseName());

        colorView.setBackgroundColor(parentCourse.getCourseColorCode());

        mDuration.setText(classDataModel.getDuration());

        mTime.setText(classDataModel.getStartTimeOriginal().toString("h:mm a")
                + " to "
                + classDataModel.getEndTimeOriginal().toString("h:mm a"));

        if (!classDataModel.getLocation().equals("")) {
            mLocation.setVisibility(View.VISIBLE);
            mLocation.setText(classDataModel.getLocation());
        } else
            mLocation.setVisibility(View.INVISIBLE);

        RemainingTime.setVisibility(View.VISIBLE);
        LocalTime now = LocalTime.now();

        //if current time ahead of starting time
        if (now.isAfter(classDataModel.getStartTimeOriginal())) {
            //if current time before ending time
            if (now.isBefore(classDataModel.getEndTimeOriginal())) {
                RemainingTime.setText("Class in progress!");
                //current time after ending time
            } else
                RemainingTime.setVisibility(View.INVISIBLE);
        } else if (Minutes.minutesBetween(now, classDataModel.getStartTimeOriginal()).getMinutes() < 60) {
            RemainingTime.setText("Class starting in "
                    + String.valueOf(Minutes.minutesBetween(now, classDataModel.getStartTimeOriginal()).getMinutes() + 1)
                    + " minutes");
        } else {
            int hours = Minutes.minutesBetween(now, classDataModel.getStartTimeOriginal()).getMinutes() / 60;
            if (hours <= 16)
                RemainingTime.setText("Class starting in "
                        + String.valueOf(hours)
                        + " hours "
                        + String.valueOf(Minutes.minutesBetween(now, classDataModel.getStartTimeOriginal()).getMinutes() % 60)
                        + " mins ");
            else
                RemainingTime.setVisibility(View.GONE);
        }

        classType.setText(classDataModel.getClassType().toLowerCase());
    }


}
