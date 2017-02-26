package com.sensei.Adapters;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sensei.DataModelClasses.AssignmentDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.DataModelClasses.HomeworkDataModel;
import com.sensei.R;

import org.joda.time.LocalDate;

import java.util.List;

import cn.refactor.library.SmoothCheckBox;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/19/16.
 */

public class DashboardHomeworkAdapter extends BaseQuickAdapter<HomeworkDataModel, BaseViewHolder> {

    public DashboardHomeworkAdapter(int layoutResId, List<HomeworkDataModel> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, HomeworkDataModel homeworkDataModel) {

        View colorView;
        TextView courseName;
        TextView quizTitle;
        TextView dueDate;
        TextView dueTime;
        TextView dueWhen;
        SmoothCheckBox markAsDone;


        colorView = baseViewHolder.getView(R.id.color);
        courseName = baseViewHolder.getView(R.id.course_name);
        quizTitle = baseViewHolder.getView(R.id.quiz_title);
        dueDate = baseViewHolder.getView(R.id.due_date);
        dueTime = baseViewHolder.getView(R.id.due_time);
        dueWhen = baseViewHolder.getView(R.id.due_when);
        markAsDone = baseViewHolder.getView(R.id.done_checkbox);


        CourseDataModel parentCourse = getCourseDataInstance().getCourse(homeworkDataModel);


        courseName.setText(parentCourse.getCourseName());

        colorView.setBackgroundColor(parentCourse.getCourseColorCode());

        quizTitle.setText(homeworkDataModel.getHomeworkTitle());

        if (homeworkDataModel.getDueDate() != null) {
            dueDate.setVisibility(View.VISIBLE);
            dueDate.setText(homeworkDataModel.getDueDateOriginal().toString("E, d MMM"));
        } else
            dueDate.setVisibility(View.INVISIBLE);


        if (homeworkDataModel.getDueTime() != null) {
            dueTime.setVisibility(View.VISIBLE);
            dueTime.setText(homeworkDataModel.getDueTimeOriginal().toString("h:mm a"));
        } else
            dueTime.setVisibility(View.INVISIBLE);

        LocalDate today = new LocalDate();
        LocalDate thisWeekEnd = today.dayOfWeek().withMaximumValue();
        LocalDate nextWeekEnd = thisWeekEnd.plusDays(7);

        if (homeworkDataModel.getDueDate() != null) {
            int quizDay = homeworkDataModel.getDueDateOriginal().getDayOfYear();

            if (today.getDayOfYear() > quizDay)
                dueWhen.setText("Overdue!");
            else if (quizDay == today.getDayOfYear())
                dueWhen.setText("Due Today!");
            else if (quizDay == today.plusDays(1).getDayOfYear())
                dueWhen.setText("Due Tomorrow!");
            else if (homeworkDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.getWeekOfWeekyear())
                dueWhen.setText("Due This Week!");
            else if (homeworkDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.plusWeeks(1).getWeekOfWeekyear())
                dueWhen.setText("Due Next Week!");
            else dueWhen.setVisibility(View.GONE);

        }

        markAsDone.setChecked(homeworkDataModel.getCompleted(), false);




    }


}
