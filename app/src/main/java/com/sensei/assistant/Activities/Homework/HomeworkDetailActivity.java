package com.sensei.assistant.Activities.Homework;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.zagum.switchicon.SwitchIconView;
import com.sensei.assistant.Activities.Assignments.AssignmentDetailActivity;
import com.sensei.assistant.Activities.Assignments.EditAssignmentActivity;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.HomeworkDataModel;
import com.sensei.assistant.R;

import org.joda.time.LocalDate;

import timber.log.Timber;

import static android.view.View.GONE;
import static com.sensei.assistant.Application.Constants.REQUEST_CODE_EDIT_ASSIGNMENT;
import static com.sensei.assistant.Application.Constants.REQUEST_CODE_EDIT_HOMEWORK;
import static com.sensei.assistant.Application.Constants.RESULT_CODE_FINISH_ACTIVITY;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class HomeworkDetailActivity extends AppCompatActivity {


    HomeworkDataModel homeworkDataModel;
    CourseDataModel courseDataModel;
    String courseID;
    String homeworkID;
    TextView dueDate;
    TextView dueTime;
    TextView dueWhen;
    TextView taskTitle;
    TextView taskDetails;
    View markAsDone;
    SwitchIconView doneIconView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        Intent intent = getIntent();
        homeworkID = intent.getStringExtra("homeworkID");
        courseID = intent.getStringExtra("courseID");

        courseDataModel = getCourseDataInstance().CoursesID.get(courseID);


        findViewById(R.id.toolbar).setBackgroundColor(courseDataModel.getCourseColorCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(courseDataModel.getDarkerColor());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Homework Details");
        getSupportActionBar().setSubtitle(courseDataModel.getCourseAbbreviation());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dueDate = (TextView) findViewById(R.id.due_date);
        dueTime = (TextView) findViewById(R.id.due_time);
        dueWhen = (TextView) findViewById(R.id.due_when);
        taskTitle = (TextView) findViewById(R.id.task_title);
        taskDetails = (TextView) findViewById(R.id.task_details);
        markAsDone = findViewById(R.id.mark_as_done);
        doneIconView = (SwitchIconView) findViewById(R.id.done_checkbox);
        markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneIconView.switchState(true);
                getCourseDataInstance().updateTaskCompleted(homeworkDataModel, doneIconView.isIconEnabled());
                if (doneIconView.isIconEnabled()) { //MARK AS INCOMPLETE
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
            }
        });
    }

    public void onStart() {
        super.onStart();
        homeworkDataModel = getCourseDataInstance().HomeworkID.get(homeworkID);

        Timber.d("onStart,Calling updateFields");

        updateFields();

    }

    public void updateFields() {


        if (homeworkDataModel.getDueDate() != null) {
            ((LinearLayout) (dueDate.getParent())).setVisibility(View.VISIBLE);
            dueDate.setText(homeworkDataModel.getDueDateOriginal().toString("E, d MMM"));

            if (!homeworkDataModel.getCompleted()) {

                dueWhen.setVisibility(View.VISIBLE);

                int quizDay = homeworkDataModel.getDueDateOriginal().getDayOfYear();

                LocalDate today = new LocalDate();

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

            } else
                dueWhen.setVisibility(View.INVISIBLE);


        } else {
            ((LinearLayout) (dueDate.getParent().getParent())).setVisibility(View.GONE);
            dueWhen.setVisibility(View.INVISIBLE);
        }


        if (homeworkDataModel.getDueTime() != null) {
            dueTime.setVisibility(View.VISIBLE);
            dueTime.setText(homeworkDataModel.getDueTimeOriginal().toString("h:mm a"));
        } else
            ((LinearLayout) (dueTime.getParent().getParent())).setVisibility(View.GONE);


        taskTitle.setText(homeworkDataModel.getHomeworkTitle());

        if (homeworkDataModel.getHomeworkDescription() != null) {
            ((View) (taskDetails.getParent())).setVisibility(View.VISIBLE);

            taskDetails.setText(homeworkDataModel.getHomeworkDescription());
        } else {

            ((CardView) (taskDetails.getParent())).setVisibility(View.GONE);
        }


//        if (homeworkDataModel.getAssignmentDescription() != null) {
//            taskDetails.setVisibility(View.VISIBLE);
//            taskDetails.setText(homeworkDataModel.getAssignmentDescription());
//        } else {
//            taskDetails.setVisibility(GONE);
//        }


        doneIconView.setIconEnabled(homeworkDataModel.getCompleted());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.edit_class:
                Intent intent = new Intent(HomeworkDetailActivity.this, EditHomeworkActivity.class);
//                intent.putExtras(bundle);
                intent.putExtra("courseID", courseID);
                intent.putExtra("homeworkID", homeworkID);
                startActivityForResult(intent, REQUEST_CODE_EDIT_HOMEWORK);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.class_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_EDIT_HOMEWORK) {
//            Timber.d("onActivityResult,calling updateFields");
//            updateFields();

            if (resultCode == RESULT_CODE_FINISH_ACTIVITY) {
                finish();
            }
        }
    }
}
