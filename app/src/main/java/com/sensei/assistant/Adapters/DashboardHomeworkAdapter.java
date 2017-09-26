package com.sensei.assistant.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.zagum.switchicon.SwitchIconView;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.HomeworkDataModel;
import com.sensei.assistant.R;

import org.joda.time.LocalDate;

import java.util.List;

import static android.view.View.GONE;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/19/16.
 */

public class DashboardHomeworkAdapter extends BaseItemDraggableAdapter<HomeworkDataModel, BaseViewHolder> {

    public DashboardHomeworkAdapter(int layoutResId, List<HomeworkDataModel> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, final HomeworkDataModel homeworkDataModel) {

        View colorView;
        TextView courseName;
        TextView homeworkTitle;
        TextView dueDate;
        TextView dueTime;
        final TextView dueWhen;
        final SwitchIconView markAsDone;


        colorView = baseViewHolder.getView(R.id.color);
        courseName = baseViewHolder.getView(R.id.course_name);
        homeworkTitle = baseViewHolder.getView(R.id.quiz_title);
        dueDate = baseViewHolder.getView(R.id.due_date);
        dueTime = baseViewHolder.getView(R.id.due_time);
        dueWhen = baseViewHolder.getView(R.id.due_when);
        markAsDone = baseViewHolder.getView(R.id.done_checkbox);

        baseViewHolder.addOnClickListener(markAsDone.getId());
        CourseDataModel parentCourse = getCourseDataInstance().getCourse(homeworkDataModel);


        courseName.setText(parentCourse.getCourseName());

        colorView.setBackgroundColor(parentCourse.getCourseColorCode());

        homeworkTitle.setText(homeworkDataModel.getHomeworkTitle());

        if (homeworkDataModel.getDueTime() != null) {
            dueTime.setVisibility(View.VISIBLE);
            dueTime.setText(homeworkDataModel.getDueTimeOriginal().toString("h:mm a"));
        } else {
            dueTime.setVisibility(View.GONE);
        }

        LocalDate today = new LocalDate();
        LocalDate thisWeekEnd = today.dayOfWeek().withMaximumValue();
        LocalDate nextWeekEnd = thisWeekEnd.plusDays(7);

        if (homeworkDataModel.getDueDate() != null) {

            int quizDay = homeworkDataModel.getDueDateOriginal().getDayOfYear();
            dueWhen.setVisibility(View.VISIBLE);
            dueDate.setVisibility(View.VISIBLE);
            dueDate.setText(homeworkDataModel.getDueDateOriginal().toString("E, d MMM"));


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
            else dueWhen.setText("Upcoming!");

        } else {
            dueDate.setVisibility(GONE);
            dueWhen.setVisibility(GONE);
        }


        markAsDone.setIconEnabled(homeworkDataModel.getCompleted(), false);

        if (homeworkDataModel.getCompleted()) {

            dueWhen.setVisibility(GONE);
        } else {
            if (homeworkDataModel.getDueDate() != null) {
                dueWhen.setVisibility(View.VISIBLE);
            } else
                dueWhen.setVisibility(GONE);
        }

        markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markAsDone.switchState(true);
//                notifyDataSetChanged();
                getCourseDataInstance().updateTaskCompleted(homeworkDataModel, markAsDone.isIconEnabled());
                if (markAsDone.isIconEnabled()) { //MARK AS INCOMPLETE
                    dueWhen.animate()
//                            .translationY(view.getHeight())
                            .alpha(0.0f)
                            .setDuration(250)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    dueWhen.setVisibility(GONE);

                                }
                            });


//                    dueWhen.setVisibility(View.GONE);
                } else {   //MARK AS DONE

//                    dueWhen.setVisibility(View.VISIBLE);
                    dueWhen.animate()
//                            .translationY(-view.getHeight())
                            .alpha(1f)
                            .setDuration(250)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if (homeworkDataModel.getDueDate() != null) {
                                        dueWhen.setVisibility(View.VISIBLE);
                                    }
//                                    dueWhen.setVisibility(View.VISIBLE);
                                }
                            });


                }
//                setNewData(getCourseDataInstance().getListOfQuizzes());


            }


        });


    }


}
