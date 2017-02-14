package com.sensei.Adapters;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.DataModelClasses.QuizDataModel;
import com.sensei.R;

import org.joda.time.LocalDate;

import java.util.List;

import cn.refactor.library.SmoothCheckBox;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/19/16.
 */

public class DashboardQuizAdapter extends BaseQuickAdapter<QuizDataModel, BaseViewHolder> {

    public DashboardQuizAdapter(int layoutResId, List<QuizDataModel> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final QuizDataModel quizDataModel) {

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

        baseViewHolder.addOnClickListener(markAsDone.getId());


        CourseDataModel parentCourse = getCourseDataInstance().getCourse(quizDataModel);


        courseName.setText(parentCourse.getCourseName());

        colorView.setBackgroundColor(parentCourse.getCourseColorCode());

        quizTitle.setText(quizDataModel.getQuizTitle());

        if (quizDataModel.getDueDate() != null) {
            dueDate.setVisibility(View.VISIBLE);
            dueDate.setText(quizDataModel.getDueDateOriginal().toString("E, d MMM"));
        } else
            dueDate.setVisibility(View.INVISIBLE);


        if (quizDataModel.getDueTime() != null) {
            dueTime.setVisibility(View.VISIBLE);
            dueTime.setText(quizDataModel.getDueTimeOriginal().toString("h:mm a"));
        } else
            dueTime.setVisibility(View.INVISIBLE);

        LocalDate today = new LocalDate();
        LocalDate thisWeekEnd = today.dayOfWeek().withMaximumValue();
        LocalDate nextWeekEnd = thisWeekEnd.plusDays(7);

        if (quizDataModel.getDueDate() != null) {
            int quizDay = quizDataModel.getDueDateOriginal().getDayOfYear();
            dueWhen.setVisibility(View.VISIBLE);

            if (today.getDayOfYear() > quizDay)
                dueWhen.setText("Overdue!");
            else if (quizDay == today.getDayOfYear())
                dueWhen.setText("Due Today!");
            else if (quizDay == today.plusDays(1).getDayOfYear())
                dueWhen.setText("Due Tomorrow!");
            else if (quizDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.getWeekOfWeekyear())
                dueWhen.setText("Due This Week!");
            else if (quizDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.plusWeeks(1).getWeekOfWeekyear())
                dueWhen.setText("Due Next Week!");
            else dueWhen.setVisibility(View.GONE);

        }

        markAsDone.setChecked(quizDataModel.getCompleted(), false);

        if (quizDataModel.getCompleted()) {
            dueWhen.setVisibility(View.GONE);
        } else {
            dueWhen.setVisibility(View.VISIBLE);
        }

        markAsDone.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean b) {
                getCourseDataInstance().updateTaskCompleted(quizDataModel, b);
            }
        });
    }


}
