package com.sensei.Adapters;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sensei.DataModelClasses.AssignmentDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;

import org.joda.time.LocalDate;

import java.util.List;

import cn.refactor.library.SmoothCheckBox;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/19/16.
 */

public class DashboardAssignmentAdapter extends BaseQuickAdapter<AssignmentDataModel, BaseViewHolder> {

    public DashboardAssignmentAdapter(int layoutResId, List<AssignmentDataModel> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, AssignmentDataModel assignmentDataModel) {

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


        CourseDataModel parentCourse = getCourseDataInstance().getCourse(assignmentDataModel);


        courseName.setText(parentCourse.getCourseName());

        colorView.setBackgroundColor(parentCourse.getCourseColorCode());

        quizTitle.setText(assignmentDataModel.getAssignmentTitle());

        if (assignmentDataModel.getDueDate() != null) {
            dueDate.setVisibility(View.VISIBLE);
            dueDate.setText(assignmentDataModel.getDueDateOriginal().toString("E, d MMM"));
        } else
            dueDate.setVisibility(View.INVISIBLE);


        if (assignmentDataModel.getDueTime() != null) {
            dueTime.setVisibility(View.VISIBLE);
            dueTime.setText(assignmentDataModel.getDueTimeOriginal().toString("h:mm a"));
        } else
            dueTime.setVisibility(View.INVISIBLE);

        LocalDate today = new LocalDate();
        LocalDate thisWeekEnd = today.dayOfWeek().withMaximumValue();
        LocalDate nextWeekEnd = thisWeekEnd.plusDays(7);

        if (assignmentDataModel.getDueDate() != null) {
            int quizDay = assignmentDataModel.getDueDateOriginal().getDayOfYear();

            if (today.getDayOfYear() > quizDay)
                dueWhen.setText("Overdue!");
            else if (quizDay == today.getDayOfYear())
                dueWhen.setText("Due Today!");
            else if (quizDay == today.plusDays(1).getDayOfYear())
                dueWhen.setText("Due Tomorrow!");
            else if (assignmentDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.getWeekOfWeekyear())
                dueWhen.setText("Due This Week!");
            else if (assignmentDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.plusWeeks(1).getWeekOfWeekyear())
                dueWhen.setText("Due Next Week!");
            else dueWhen.setVisibility(View.GONE);

        }

        markAsDone.setChecked(assignmentDataModel.getCompleted());


    }


}
