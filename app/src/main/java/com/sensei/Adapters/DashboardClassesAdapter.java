package com.sensei.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.List;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static com.sensei.R.id.course;

/**
 * Created by asad on 7/19/16.
 */

public class DashboardClassesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private class ClassViewHolder extends RecyclerView.ViewHolder {
        Context context;
        View colorView;
        TextView courseName;
        TextView mDuration;
        TextView mLocation;
        TextView mTime;
        TextView RemainingTime;
        TextView classType;

        ClassViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;

            colorView = itemView.findViewById(R.id.color);
            courseName = (TextView) itemView.findViewById(R.id.course_name);
            mDuration = (TextView) itemView.findViewById(R.id.duration);
            mLocation = (TextView) itemView.findViewById(R.id.location);
            mTime = (TextView) itemView.findViewById(R.id.time);
            RemainingTime = (TextView) itemView.findViewById(R.id.remaining_time);
            classType = (TextView) itemView.findViewById(R.id.class_type);


        }

    }


    public DashboardClassesAdapter(Context context) {
        this.context = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.class_layout, parent, false);
        return new ClassViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ClassViewHolder viewHolder = (ClassViewHolder) holder;
        ClassDataModel classDataModel = getCourseDataInstance().getListOfClassesForCurrentDay().get(position);
        CourseDataModel parentCourse = null;
        for (CourseDataModel courseDataModel : getCourseDataInstance().CoursesList) {
            if (courseDataModel.getClassesList().contains(classDataModel)) {
                parentCourse = courseDataModel;
                break;
            }
        }

        viewHolder.courseName.setText(parentCourse.getCourseName());
        viewHolder.colorView.setBackgroundColor(parentCourse.getCourseColorCode());
        viewHolder.mDuration.setText(classDataModel.getDuration());
        viewHolder.mTime.setText(classDataModel.getStartTimeOriginal().toString("h:mm a")
                + " to "
                + classDataModel.getEndTimeOriginal().toString("h:mm a"));
        viewHolder.mLocation.setText(classDataModel.getLocation());
        LocalTime localTime = new LocalTime();
        viewHolder.RemainingTime.setVisibility(View.VISIBLE);

        int minutes = Minutes.minutesBetween(localTime, classDataModel.getStartTimeOriginal()).getMinutes();
        if (minutes < 0) {
            viewHolder.RemainingTime.setText("Class in progress!");

        } else if (minutes < 60)
            viewHolder.RemainingTime.setText("Class starting in " + String.valueOf(minutes) + " minutes");
        else {
            int hours = minutes / 60;
            minutes = minutes % 60;
            if (hours <= 12)
                viewHolder.RemainingTime.setText("Class starting in " + String.valueOf(hours) + " hours " + String.valueOf(minutes) + " mins ");
            else
                viewHolder.RemainingTime.setText("");
        }

        viewHolder.classType.setText(classDataModel.getClassType().toLowerCase());

    }


    @Override
    public int getItemCount() {
        return getCourseDataInstance().getListOfClassesForCurrentDay().size();
    }
}
