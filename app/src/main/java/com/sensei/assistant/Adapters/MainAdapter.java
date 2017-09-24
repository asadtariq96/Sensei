package com.sensei.assistant.Adapters;

import android.content.Context;
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

    List<QuizDataModel> quizList;
    List<AssignmentDataModel> assignmentList;
    List<HomeworkDataModel> homeworkList;
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

    @Override
    public void onBindViewHolder(
            MainVH holder, int section, int relativePosition, int absolutePosition) {


        switch (section) {
            case TYPE_QUIZ:


                holder.markAsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                QuizDataModel quizDataModel = quizList.get(relativePosition);


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
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate!=null");
                    int quizDay = quizDataModel.getDueDateOriginal().getDayOfYear();
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(View.VISIBLE)");
                    holder.dueDate.setVisibility(View.VISIBLE);
                    holder.dueDate.setText(quizDataModel.getDueDateOriginal().toString("E, d MMM"));

                    if (!quizDataModel.getCompleted()) {
//                Timber.d(getViewHolderPosition(baseViewHolder) + " quiz not completed");
//                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(View.VISIBLE)");

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
//                Timber.d(getViewHolderPosition(baseViewHolder) + ":" + dueWhen.getText().toString());

                    } else {
                        holder.dueWhen.setVisibility(GONE);
//                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");

                    }


                } else {
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(GONE)");

                    holder.dueWhen.setVisibility(GONE);
                    holder.dueDate.setVisibility(GONE);
                }


                holder.markAsDone.setIconEnabled(quizDataModel.getCompleted(), false);
                break;


            case TYPE_ASSIGNMENT:
                AssignmentDataModel assignmentDataModel = assignmentList.get(relativePosition);


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
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate!=null");
                    int quizDay = assignmentDataModel.getDueDateOriginal().getDayOfYear();
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(View.VISIBLE)");
                    holder.dueDate.setVisibility(View.VISIBLE);
                    holder.dueDate.setText(assignmentDataModel.getDueDateOriginal().toString("E, d MMM"));

                    if (!assignmentDataModel.getCompleted()) {
//                Timber.d(getViewHolderPosition(baseViewHolder) + " quiz not completed");
//                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(View.VISIBLE)");

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
//                Timber.d(getViewHolderPosition(baseViewHolder) + ":" + dueWhen.getText().toString());

                    } else {
                        holder.dueWhen.setVisibility(GONE);
//                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");

                    }


                } else {
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(GONE)");

                    holder.dueWhen.setVisibility(GONE);
                    holder.dueDate.setVisibility(GONE);
                }


                holder.markAsDone.setIconEnabled(assignmentDataModel.getCompleted(), false);
                break;


            case TYPE_HOMEWORK:
                HomeworkDataModel homeworkDataModel = homeworkList.get(relativePosition);


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
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate!=null");
                    int quizDay = homeworkDataModel.getDueDateOriginal().getDayOfYear();
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(View.VISIBLE)");
                    holder.dueDate.setVisibility(View.VISIBLE);
                    holder.dueDate.setText(homeworkDataModel.getDueDateOriginal().toString("E, d MMM"));

                    if (!homeworkDataModel.getCompleted()) {
//                Timber.d(getViewHolderPosition(baseViewHolder) + " quiz not completed");
//                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(View.VISIBLE)");

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
//                Timber.d(getViewHolderPosition(baseViewHolder) + ":" + dueWhen.getText().toString());

                    } else {
                        holder.dueWhen.setVisibility(GONE);
//                Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");

                    }


                } else {
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueWhen.setVisibility(GONE)");
//            Timber.d(getViewHolderPosition(baseViewHolder) + " dueDate.setVisibility(GONE)");

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
            this.title = itemView.findViewById(R.id.title);
            this.caret = itemView.findViewById(R.id.caret);
            this.adapter = adapter;
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