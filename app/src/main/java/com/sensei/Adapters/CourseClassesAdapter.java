package com.sensei.Adapters;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by Asad on 16-Jan-17.
 */

public class CourseClassesAdapter extends BaseQuickAdapter<ClassDataModel, BaseViewHolder> {

    public CourseClassesAdapter(int layoutResId, List<ClassDataModel> data) {

        super(layoutResId, data);
        Timber.d("CourseClassesAdapter constructor " + data.size());

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


        DateTime.Property day = new DateTime().withDayOfWeek(classDataModel.getDayOfWeek()).dayOfWeek();
        String dayOfWeek = day.getAsText(Locale.getDefault());


        courseName.setText(dayOfWeek);

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
        RemainingTime.setVisibility(View.GONE);
        classType.setText(classDataModel.getClassType().toLowerCase());

    }
}
