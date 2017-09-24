package com.sensei.assistant.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.ItemCoord;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.github.zagum.switchicon.SwitchIconView;
import com.sensei.assistant.Activities.Homework.HomeworkDetailActivity;
import com.sensei.assistant.Activities.Quizzes.QuizDetailActivity;
import com.sensei.assistant.DataModelClasses.AssignmentDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.HomeworkDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.DataModelClasses.TaskItem;
import com.sensei.assistant.R;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;

import bolts.Task;
import timber.log.Timber;

import static android.view.View.GONE;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static com.sensei.assistant.DataModelClasses.TaskItem.TYPE_ASSIGNMENT;
import static com.sensei.assistant.DataModelClasses.TaskItem.TYPE_HOMEWORK;
import static com.sensei.assistant.DataModelClasses.TaskItem.TYPE_QUIZ;

/**
 * Created by Asad on 9/24/2017.
 */

public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.MainVH> {


    public MainAdapter(Context context, List<QuizDataModel> quizList, List<AssignmentDataModel> assignmentList, List<HomeworkDataModel> homeworkList) {
        this.quizList = quizList;
        this.homeworkList = homeworkList;
        this.assignmentList = assignmentList;
        this.context = context;
    }

    public List<QuizDataModel> quizList;
    public List<AssignmentDataModel> assignmentList;
    public List<HomeworkDataModel> homeworkList;
    private Context context;

    @Override
    public int getSectionCount() {
        return 3;
    }

    @Override
    public int getItemCount(int section) {
        switch (section) {
            case TYPE_QUIZ:
                return quizList.size();
            case TYPE_ASSIGNMENT:
                return assignmentList.size();
            case TYPE_HOMEWORK:
                return homeworkList.size();
        }
        return 0;
    }

    @Override
    public void onBindHeaderViewHolder(MainVH holder, int section, boolean expanded) {
        switch (section) {
            case TYPE_QUIZ:
                holder.title.setText("Quizzes");
                break;
            case TYPE_ASSIGNMENT:
                holder.title.setText("Assignment");
                break;
            case TYPE_HOMEWORK:
                holder.title.setText("Homework");
                break;
        }
//        holder.title.setText("Quizzes");
        holder.caret.setImageResource(expanded ? R.drawable.ic_collapse : R.drawable.ic_expand);
    }

    @Override
    public void onBindFooterViewHolder(MainVH holder, int section) {
        holder.title.setText(String.format("Section footer %d", section));

    }

//    public void removeQuiz(int pos, QuizDataModel quizDataModel){
//
//
//    }

    @Override
    public void onBindViewHolder(
            final MainVH holder, final int section, final int relativePosition, int absolutePosition) {


        switch (section) {
            case TYPE_QUIZ:
                final QuizDataModel quizDataModel = quizList.get(relativePosition);

                ((View) holder.colorView.getParent()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, QuizDetailActivity.class);
                        String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourse(quizDataModel));
                        String quizID = getCourseDataInstance().getQuizID(quizDataModel);
                        intent.putExtra("courseID", courseID);
                        intent.putExtra("quizID", quizID);
                        context.startActivity(intent);

                    }
                });

                holder.markAsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.markAsDone.switchState(true);

                        Timber.d("markAsDoneState " + holder.markAsDone.isIconEnabled());
                        getCourseDataInstance().updateTaskCompleted(quizDataModel, holder.markAsDone.isIconEnabled());
                        quizDataModel.setCompleted(holder.markAsDone.isIconEnabled());

                        if (holder.markAsDone.isIconEnabled()) { //MARK AS COMPLETE

                            quizList.remove(quizDataModel);
//                            holder.adapter.notifyDataSetChanged();
                            if (quizList.isEmpty()) {
                                holder.adapter.notifyDataSetChanged();
                            } else {
                                holder.adapter.notifyItemRemoved(relativePosition);
                                holder.adapter.notifyItemRangeChanged(relativePosition, quizList.size());
                            }

                        } else {   //MARK AS INCOMPLETE

                            if (quizDataModel.getDueDate() != null) {
                                LocalDate today = new LocalDate();

                                if (quizDataModel.getDueDate() != null) {
                                    int quizDay = quizDataModel.getDueDateOriginal().getDayOfYear();
                                    holder.dueDate.setVisibility(View.VISIBLE);
                                    holder.dueDate.setText(quizDataModel.getDueDateOriginal().toString("E, d MMM"));

                                    if (!quizDataModel.getCompleted()) {

                                        if (today.getDayOfYear() > quizDay)
                                            holder.dueWhen.setText("Overdue!");
                                        else if (quizDay == today.getDayOfYear())
                                            holder.dueWhen.setText("Due Today!");
                                        else if (quizDay == today.plusDays(1).getDayOfYear())
                                            holder.dueWhen.setText("Due Tomorrow!");
                                        else if (quizDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.getWeekOfWeekyear())
                                            holder.dueWhen.setText("Due This Week!");
                                        else if (quizDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.plusWeeks(1).getWeekOfWeekyear())
                                            holder.dueWhen.setText("Due Next Week!");
                                        else holder.dueWhen.setText("Upcoming!");

                                        holder.dueWhen.setVisibility(View.VISIBLE);


                                    } else {
                                        holder.dueWhen.setVisibility(GONE);

                                    }


                                } else {
                                    holder.dueWhen.setVisibility(GONE);
                                    holder.dueDate.setVisibility(GONE);
                                }
                            }


                        }
                    }
                });


                CourseDataModel parentCourse = getCourseDataInstance().getCourse(quizDataModel);
                holder.courseName.setText(parentCourse.getCourseName());
                holder.colorView.setBackgroundColor(parentCourse.getCourseColorCode());
                holder.quizTitle.setText(quizDataModel.getQuizTitle());

                if (quizDataModel.getDueTime() != null) {
                    holder.dueTime.setVisibility(View.VISIBLE);
                    holder.dueTime.setText(quizDataModel.getDueTimeOriginal().toString("h:mm a"));
                } else {
                    holder.dueTime.setVisibility(View.GONE);
                }

                LocalDate today = new LocalDate();

                if (quizDataModel.getDueDate() != null) {
                    int quizDay = quizDataModel.getDueDateOriginal().getDayOfYear();
                    holder.dueDate.setVisibility(View.VISIBLE);
                    holder.dueDate.setText(quizDataModel.getDueDateOriginal().toString("E, d MMM"));

                    if (!quizDataModel.getCompleted()) {

                        if (today.getDayOfYear() > quizDay)
                            holder.dueWhen.setText("Overdue!");
                        else if (quizDay == today.getDayOfYear())
                            holder.dueWhen.setText("Due Today!");
                        else if (quizDay == today.plusDays(1).getDayOfYear())
                            holder.dueWhen.setText("Due Tomorrow!");
                        else if (quizDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.getWeekOfWeekyear())
                            holder.dueWhen.setText("Due This Week!");
                        else if (quizDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.plusWeeks(1).getWeekOfWeekyear())
                            holder.dueWhen.setText("Due Next Week!");
                        else holder.dueWhen.setText("Upcoming!");

                        holder.dueWhen.setVisibility(View.VISIBLE);

                    } else {
                        holder.dueWhen.setVisibility(GONE);

                    }


                } else {

                    holder.dueWhen.setVisibility(GONE);
                    holder.dueDate.setVisibility(GONE);
                }


                holder.markAsDone.setIconEnabled(quizDataModel.getCompleted(), false);
                break;


            case TYPE_ASSIGNMENT:
                final AssignmentDataModel assignmentDataModel = assignmentList.get(relativePosition);

                ((View) holder.colorView.getParent()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, QuizDetailActivity.class);
                        String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourse(assignmentDataModel));
                        String assignmentID = getCourseDataInstance().getAssignmentID(assignmentDataModel);
                        intent.putExtra("courseID", courseID);
                        intent.putExtra("assignmentID", assignmentID);
                        context.startActivity(intent);

                    }
                });

                holder.markAsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.markAsDone.switchState(true);

                        Timber.d("markAsDoneState " + holder.markAsDone.isIconEnabled());
                        getCourseDataInstance().updateTaskCompleted(assignmentDataModel, holder.markAsDone.isIconEnabled());
                        assignmentDataModel.setCompleted(holder.markAsDone.isIconEnabled());

                        if (holder.markAsDone.isIconEnabled()) { //MARK AS COMPLETE

                            assignmentList.remove(assignmentDataModel);
//                            holder.adapter.notifyDataSetChanged();
                            if (assignmentList.isEmpty()) {
                                holder.adapter.notifyDataSetChanged();
                            } else {
                                holder.adapter.notifyItemRemoved(relativePosition);
                                holder.adapter.notifyItemRangeChanged(relativePosition, assignmentList.size());
                            }

                        } else {   //MARK AS INCOMPLETE

                            if (assignmentDataModel.getDueDate() != null) {
                                LocalDate today = new LocalDate();

                                if (assignmentDataModel.getDueDate() != null) {
                                    int quizDay = assignmentDataModel.getDueDateOriginal().getDayOfYear();
                                    holder.dueDate.setVisibility(View.VISIBLE);
                                    holder.dueDate.setText(assignmentDataModel.getDueDateOriginal().toString("E, d MMM"));

                                    if (!assignmentDataModel.getCompleted()) {

                                        if (today.getDayOfYear() > quizDay)
                                            holder.dueWhen.setText("Overdue!");
                                        else if (quizDay == today.getDayOfYear())
                                            holder.dueWhen.setText("Due Today!");
                                        else if (quizDay == today.plusDays(1).getDayOfYear())
                                            holder.dueWhen.setText("Due Tomorrow!");
                                        else if (assignmentDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.getWeekOfWeekyear())
                                            holder.dueWhen.setText("Due This Week!");
                                        else if (assignmentDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.plusWeeks(1).getWeekOfWeekyear())
                                            holder.dueWhen.setText("Due Next Week!");
                                        else holder.dueWhen.setText("Upcoming!");

                                        holder.dueWhen.setVisibility(View.VISIBLE);


                                    } else {
                                        holder.dueWhen.setVisibility(GONE);

                                    }


                                } else {
                                    holder.dueWhen.setVisibility(GONE);
                                    holder.dueDate.setVisibility(GONE);
                                }
                            }


                        }
                    }
                });


                parentCourse = getCourseDataInstance().getCourse(assignmentDataModel);
                holder.courseName.setText(parentCourse.getCourseName());
                holder.colorView.setBackgroundColor(parentCourse.getCourseColorCode());
                holder.quizTitle.setText(assignmentDataModel.getAssignmentTitle());

                if (assignmentDataModel.getDueTime() != null) {
                    holder.dueTime.setVisibility(View.VISIBLE);
                    holder.dueTime.setText(assignmentDataModel.getDueTimeOriginal().toString("h:mm a"));
                } else {
                    holder.dueTime.setVisibility(View.GONE);
                }

                today = new LocalDate();

                if (assignmentDataModel.getDueDate() != null) {
                    int quizDay = assignmentDataModel.getDueDateOriginal().getDayOfYear();
                    holder.dueDate.setVisibility(View.VISIBLE);
                    holder.dueDate.setText(assignmentDataModel.getDueDateOriginal().toString("E, d MMM"));

                    if (!assignmentDataModel.getCompleted()) {

                        if (today.getDayOfYear() > quizDay)
                            holder.dueWhen.setText("Overdue!");
                        else if (quizDay == today.getDayOfYear())
                            holder.dueWhen.setText("Due Today!");
                        else if (quizDay == today.plusDays(1).getDayOfYear())
                            holder.dueWhen.setText("Due Tomorrow!");
                        else if (assignmentDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.getWeekOfWeekyear())
                            holder.dueWhen.setText("Due This Week!");
                        else if (assignmentDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.plusWeeks(1).getWeekOfWeekyear())
                            holder.dueWhen.setText("Due Next Week!");
                        else holder.dueWhen.setText("Upcoming!");
                        holder.dueWhen.setVisibility(View.VISIBLE);

                    } else {
                        holder.dueWhen.setVisibility(GONE);

                    }


                } else {

                    holder.dueWhen.setVisibility(GONE);
                    holder.dueDate.setVisibility(GONE);
                }


                holder.markAsDone.setIconEnabled(assignmentDataModel.getCompleted(), false);
                break;


            case TYPE_HOMEWORK:
                final HomeworkDataModel homeworkDataModel = homeworkList.get(relativePosition);


                ((View) holder.colorView.getParent()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, QuizDetailActivity.class);
                        String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourse(homeworkDataModel));
                        String homeworkID = getCourseDataInstance().getHomeworkID(homeworkDataModel);
                        intent.putExtra("courseID", courseID);
                        intent.putExtra("homeworkID", homeworkID);
                        context.startActivity(intent);

                    }
                });


                holder.markAsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.markAsDone.switchState(true);

                        Timber.d("markAsDoneState " + holder.markAsDone.isIconEnabled());
                        getCourseDataInstance().updateTaskCompleted(homeworkDataModel, holder.markAsDone.isIconEnabled());
                        homeworkDataModel.setCompleted(holder.markAsDone.isIconEnabled());

                        if (holder.markAsDone.isIconEnabled()) { //MARK AS COMPLETE

                            homeworkList.remove(homeworkDataModel);
//                            holder.adapter.notifyDataSetChanged();
                            if (homeworkList.isEmpty()) {
                                holder.adapter.notifyDataSetChanged();
                            } else {
                                holder.adapter.notifyItemRemoved(relativePosition);
                                holder.adapter.notifyItemRangeChanged(relativePosition, homeworkList.size());
                            }

                        } else {   //MARK AS INCOMPLETE

                            if (homeworkDataModel.getDueDate() != null) {
                                LocalDate today = new LocalDate();

                                if (homeworkDataModel.getDueDate() != null) {
                                    int quizDay = homeworkDataModel.getDueDateOriginal().getDayOfYear();
                                    holder.dueDate.setVisibility(View.VISIBLE);
                                    holder.dueDate.setText(homeworkDataModel.getDueDateOriginal().toString("E, d MMM"));

                                    if (!homeworkDataModel.getCompleted()) {

                                        if (today.getDayOfYear() > quizDay)
                                            holder.dueWhen.setText("Overdue!");
                                        else if (quizDay == today.getDayOfYear())
                                            holder.dueWhen.setText("Due Today!");
                                        else if (quizDay == today.plusDays(1).getDayOfYear())
                                            holder.dueWhen.setText("Due Tomorrow!");
                                        else if (homeworkDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.getWeekOfWeekyear())
                                            holder.dueWhen.setText("Due This Week!");
                                        else if (homeworkDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.plusWeeks(1).getWeekOfWeekyear())
                                            holder.dueWhen.setText("Due Next Week!");
                                        else holder.dueWhen.setText("Upcoming!");

                                        holder.dueWhen.setVisibility(View.VISIBLE);


                                    } else {
                                        holder.dueWhen.setVisibility(GONE);

                                    }


                                } else {
                                    holder.dueWhen.setVisibility(GONE);
                                    holder.dueDate.setVisibility(GONE);
                                }
                            }


                        }
                    }
                });


                parentCourse = getCourseDataInstance().getCourse(homeworkDataModel);
                holder.courseName.setText(parentCourse.getCourseName());
                holder.colorView.setBackgroundColor(parentCourse.getCourseColorCode());
                holder.quizTitle.setText(homeworkDataModel.getHomeworkTitle());

                if (homeworkDataModel.getDueTime() != null) {
                    holder.dueTime.setVisibility(View.VISIBLE);
                    holder.dueTime.setText(homeworkDataModel.getDueTimeOriginal().toString("h:mm a"));
                } else {
                    holder.dueTime.setVisibility(View.GONE);
                }

                today = new LocalDate();

                if (homeworkDataModel.getDueDate() != null) {
                    int quizDay = homeworkDataModel.getDueDateOriginal().getDayOfYear();
                    holder.dueDate.setVisibility(View.VISIBLE);
                    holder.dueDate.setText(homeworkDataModel.getDueDateOriginal().toString("E, d MMM"));

                    if (!homeworkDataModel.getCompleted()) {

                        if (today.getDayOfYear() > quizDay)
                            holder.dueWhen.setText("Overdue!");
                        else if (quizDay == today.getDayOfYear())
                            holder.dueWhen.setText("Due Today!");
                        else if (quizDay == today.plusDays(1).getDayOfYear())
                            holder.dueWhen.setText("Due Tomorrow!");
                        else if (homeworkDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.getWeekOfWeekyear())
                            holder.dueWhen.setText("Due This Week!");
                        else if (homeworkDataModel.getDueDateOriginal().getWeekOfWeekyear() == today.plusWeeks(1).getWeekOfWeekyear())
                            holder.dueWhen.setText("Due Next Week!");
                        else holder.dueWhen.setText("Upcoming!");
                        holder.dueWhen.setVisibility(View.VISIBLE);

                    } else {
                        holder.dueWhen.setVisibility(GONE);

                    }


                } else {

                    holder.dueWhen.setVisibility(GONE);
                    holder.dueDate.setVisibility(GONE);
                }


                holder.markAsDone.setIconEnabled(homeworkDataModel.getCompleted(), false);
                break;
        }


    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if (section == 0) {
            // VIEW_TYPE_FOOTER is -3, VIEW_TYPE_HEADER is -2, VIEW_TYPE_ITEM is -1.
            // You can return 0 or greater.
            return 0;
        }
        if (section == 1)
            return 1;

        if (section == 2)
            return 2;

        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    public void updateData(List<QuizDataModel> quizList, List<AssignmentDataModel> assignmentList, List<HomeworkDataModel> homeworkList) {
        this.quizList.clear();
        this.quizList.addAll(quizList);
        this.assignmentList.clear();
        this.assignmentList.addAll(assignmentList);
        this.homeworkList.clear();
        this.homeworkList.addAll(homeworkList);
        notifyDataSetChanged();
    }

    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = 0;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.list_item_header;
                break;
            case VIEW_TYPE_ITEM:
                layout = R.layout.quiz_layout;
                break;
            case VIEW_TYPE_FOOTER:
                layout = R.layout.quizzes_header_view;
                break;
            case 0:
            case 1:
            case 2:
                // Our custom item, which is the 0 returned in getItemViewType() above
                layout = R.layout.quiz_layout;
                break;
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MainVH(v, this);
    }

    static class MainVH extends SectionedViewHolder implements View.OnClickListener {


        View colorView;
        TextView courseName;
        TextView quizTitle;
        TextView dueDate;
        TextView dueTime;
        TextView dueWhen;
        SwitchIconView markAsDone;
        TextView title;
        ImageView caret;
        MainAdapter adapter;


//        Toast toast;

        MainVH(View itemView, MainAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.adapter = adapter;
            this.title = itemView.findViewById(R.id.title);
            this.caret = itemView.findViewById(R.id.caret);
            colorView = itemView.findViewById(R.id.color);
            courseName = itemView.findViewById(R.id.course_name);
            quizTitle = itemView.findViewById(R.id.quiz_title);
            dueDate = itemView.findViewById(R.id.due_date);
            dueTime = itemView.findViewById(R.id.due_time);
            dueWhen = itemView.findViewById(R.id.due_when);
            markAsDone = itemView.findViewById(R.id.done_checkbox);

        }

        @Override
        public void onClick(View view) {
            if (isFooter()) {
                // ignore footer clicks
                return;
            }

            if (isHeader()) {
                adapter.toggleSectionExpanded(getRelativePosition().section());
            }


        }
    }
}