package com.sensei.assistant.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/19/16.
 */

public class DashboardClassesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;

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

    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(View itemView, Context context) {
            super(itemView);
        }
    }


    public DashboardClassesAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {
        if (getCourseDataInstance().getListOfClassesForCurrentDay().isEmpty()) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        } else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                viewHolder = new EmptyViewHolder(inflater.inflate(R.layout.no_class_placeholder, parent, false), context);
                break;
            case VIEW_TYPE_OBJECT_VIEW:
                viewHolder = new ClassViewHolder(inflater.inflate(R.layout.class_layout, parent, false), context);
                break;
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_OBJECT_VIEW) {
            ClassViewHolder viewHolder = (ClassViewHolder) holder;
            ClassDataModel classDataModel = getCourseDataInstance().getListOfClassesForCurrentDay().get(position);
            CourseDataModel parentCourse = null;
            for (CourseDataModel courseDataModel : getCourseDataInstance().CoursesList) {
                if (courseDataModel.getClasses().contains(classDataModel)) {
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
            if (!classDataModel.getLocation().equals("")) {
                viewHolder.mLocation.setVisibility(View.VISIBLE);
                viewHolder.mLocation.setText(classDataModel.getLocation());
            } else
                viewHolder.mLocation.setVisibility(View.INVISIBLE);


//            viewHolder.mLocation.setText(classDataModel.getLocation());
            LocalTime localTime = new LocalTime();
            viewHolder.RemainingTime.setVisibility(View.VISIBLE);

            LocalTime now = LocalTime.now();

            //if current time ahead of starting time
            if (now.isAfter(classDataModel.getStartTimeOriginal())) {
                //if current time before ending time
                if (now.isBefore(classDataModel.getEndTimeOriginal())) {
                    viewHolder.RemainingTime.setText("Class in progress!");
                    //current time after ending time
                } else
                    viewHolder.RemainingTime.setVisibility(View.INVISIBLE);
            } else if (Minutes.minutesBetween(now, classDataModel.getStartTimeOriginal()).getMinutes() < 60) {
                viewHolder.RemainingTime.setText("Class starting in "
                        + String.valueOf(Minutes.minutesBetween(now, classDataModel.getStartTimeOriginal()).getMinutes() + 1)
                        + " minutes");
            } else {
                int hours = Minutes.minutesBetween(now, classDataModel.getStartTimeOriginal()).getMinutes() / 60;
                if (hours <= 16)
                    viewHolder.RemainingTime.setText("Class starting in "
                            + String.valueOf(hours)
                            + " hours "
                            + String.valueOf(Minutes.minutesBetween(now, classDataModel.getStartTimeOriginal()).getMinutes() % 60)
                            + " mins ");
                else
                    viewHolder.RemainingTime.setVisibility(View.GONE);
            }

            viewHolder.classType.setText(classDataModel.getClassType());
        }


    }


    @Override
    public int getItemCount() {
        return getCourseDataInstance().getListOfClassesForCurrentDay().size() > 0 ? getCourseDataInstance().getListOfClassesForCurrentDay().size() : 1;
    }


}
