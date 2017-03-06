package com.sensei.assistant.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.zagum.switchicon.SwitchIconView;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.R;

import org.joda.time.LocalDate;

import java.util.List;

import cn.refactor.library.SmoothCheckBox;

import static android.view.View.GONE;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

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
        final TextView dueWhen;
        final SwitchIconView markAsDone;


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

//        if (quizDataModel.getDueDate() != null) {
//            dueWhen.setVisibility(View.VISIBLE);
//            dueDate.setVisibility(View.VISIBLE);
//            dueDate.setText(quizDataModel.getDueDateOriginal().toString("E, d MMM"));
//        } else {
//            dueDate.setVisibility(View.GONE);
//            dueWhen.setVisibility(GONE);
//        }


        if (quizDataModel.getDueTime() != null) {
            dueTime.setVisibility(View.VISIBLE);
            dueTime.setText(quizDataModel.getDueTimeOriginal().toString("h:mm a"));
        } else {
            dueTime.setVisibility(View.GONE);
        }

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
            else dueWhen.setText("Upcoming!");

        } else {
            dueDate.setVisibility(GONE);
            dueWhen.setVisibility(GONE);
        }

        markAsDone.setIconEnabled(quizDataModel.getCompleted(), false);

        if (quizDataModel.getCompleted()) {

            dueWhen.setVisibility(GONE);
        } else {
            if (quizDataModel.getDueDate() != null) {
                dueWhen.setVisibility(View.VISIBLE);
            } else
                dueWhen.setVisibility(GONE);
        }

        markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markAsDone.switchState(true);
                getCourseDataInstance().updateTaskCompleted(quizDataModel, markAsDone.isIconEnabled());
                if (markAsDone.isIconEnabled()) {
                    dueWhen.animate()
//                            .translationY(view.getHeight())
                            .alpha(0.0f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    dueWhen.setVisibility(GONE);
                                }
                            });

//                    dueWhen.setVisibility(View.GONE);
                } else {

//                    dueWhen.setVisibility(View.VISIBLE);
                    dueWhen.animate()
//                            .translationY(-view.getHeight())
                            .alpha(1f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    dueWhen.setVisibility(View.VISIBLE);
                                }
                            });


                }
            }
        });

//        markAsDone.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean b) {
//                getCourseDataInstance().updateTaskCompleted(quizDataModel, b);
//            }
//        });
    }


}
