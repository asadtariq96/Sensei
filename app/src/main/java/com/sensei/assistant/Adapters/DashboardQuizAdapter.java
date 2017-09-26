package com.sensei.assistant.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.zagum.switchicon.SwitchIconView;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.R;

import org.joda.time.LocalDate;

import java.util.List;

import timber.log.Timber;

import static android.view.View.GONE;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/19/16.
 */

public class DashboardQuizAdapter extends BaseItemDraggableAdapter<QuizDataModel, BaseViewHolder> {


    public DashboardQuizAdapter(int layoutResId, List<QuizDataModel> data, boolean isDashboard) {
        super(layoutResId, data);
        boolean isDashboard1 = isDashboard;
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final QuizDataModel quizDataModel) {

        View colorView;
        TextView courseName;
        TextView quizTitle;
        final TextView dueDate;
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

        if (quizDataModel.getDueTime() != null) {
            dueTime.setVisibility(View.VISIBLE);
            dueTime.setText(quizDataModel.getDueTimeOriginal().toString("h:mm a"));
        } else {
            dueTime.setVisibility(View.GONE);
        }

        final LocalDate today = new LocalDate();

        if (quizDataModel.getDueDate() != null) {
            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate!=null");
            int quizDay = quizDataModel.getDueDateOriginal().getDayOfYear();
            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(View.VISIBLE)");
            dueDate.setVisibility(View.VISIBLE);
            dueDate.setText(quizDataModel.getDueDateOriginal().toString("E, d MMM"));

            if (!quizDataModel.getCompleted()) {
                Timber.d(getViewHolderPosition(baseViewHolder) + " quiz not completed");
                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(View.VISIBLE)");

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
                dueWhen.setVisibility(View.VISIBLE);
                Timber.d(getViewHolderPosition(baseViewHolder) + ":" + dueWhen.getText().toString());

            } else {
                dueWhen.setVisibility(GONE);
                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");

            }


        } else {
            Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");
            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(GONE)");

            dueWhen.setVisibility(GONE);
            dueDate.setVisibility(GONE);
        }


        markAsDone.setIconEnabled(quizDataModel.getCompleted(), false);


        markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                markAsDone.switchState(true);
//                notifyDataSetChanged();

                Timber.d("markAsDoneState " + markAsDone.isIconEnabled());
                getCourseDataInstance().updateTaskCompleted(quizDataModel, markAsDone.isIconEnabled());
                quizDataModel.setCompleted(markAsDone.isIconEnabled());

                if (markAsDone.isIconEnabled()) { //MARK AS COMPLETE
                    dueWhen.animate()
                            .alpha(0.0f)
                            .setDuration(250)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    dueWhen.setVisibility(GONE);
//                                    if(isDashboard){
//                                        remove(baseViewHolder.getAdapterPosition());
//                                    }

                                }
                            });


                } else {   //MARK AS INCOMPLETE

                    dueWhen.animate()
                            .alpha(1f)
                            .setDuration(250)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if (quizDataModel.getDueDate() != null) {
                                        LocalDate today = new LocalDate();

                                        if (quizDataModel.getDueDate() != null) {
                                            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate!=null");
                                            int quizDay = quizDataModel.getDueDateOriginal().getDayOfYear();
                                            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(View.VISIBLE)");
                                            dueDate.setVisibility(View.VISIBLE);
                                            dueDate.setText(quizDataModel.getDueDateOriginal().toString("E, d MMM"));

                                            if (!quizDataModel.getCompleted()) {
                                                Timber.d(getViewHolderPosition(baseViewHolder) + " quiz not completed");
                                                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(View.VISIBLE)");

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

                                                dueWhen.setVisibility(View.VISIBLE);
                                                Timber.d(getViewHolderPosition(baseViewHolder) + ":" + dueWhen.getText().toString());


                                            } else {
                                                dueWhen.setVisibility(GONE);
                                                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");

                                            }


                                        } else {
                                            Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");
                                            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(GONE)");

                                            dueWhen.setVisibility(GONE);
                                            dueDate.setVisibility(GONE);
                                        }
                                    }
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

//    public void add(QuizDataModel quizDataModel, int position) {
//        getData().add(position, quizDataModel);
//        notifyItemInserted(position);
//    }
//
//    public void remove(int position) {
//        getData().remove(position);
//        notifyItemRemoved(position);
//    }


}
