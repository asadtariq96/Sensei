package com.sensei.assistant.Activities.Assignments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.sensei.assistant.DataModelClasses.AssignmentDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;

import org.joda.time.LocalDate;

import timber.log.Timber;

import static android.view.View.GONE;
import static com.sensei.assistant.Application.Constants.REQUEST_CODE_EDIT_ASSIGNMENT;
import static com.sensei.assistant.Application.Constants.RESULT_CODE_FINISH_ACTIVITY;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class AssignmentDetailActivity extends AppCompatActivity {

    AssignmentDataModel assignmentDataModel;
    CourseDataModel courseDataModel;
    String courseID;
    String assignmentID;
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
        assignmentID = intent.getStringExtra("assignmentID");
        courseID = intent.getStringExtra("courseID");

        courseDataModel = getCourseDataInstance().CoursesID.get(courseID);


        findViewById(R.id.toolbar).setBackgroundColor(courseDataModel.getCourseColorCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(courseDataModel.getDarkerColor());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assignment Details");
        getSupportActionBar().setSubtitle(courseDataModel.getCourseAbbreviation());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dueDate = findViewById(R.id.due_date);
        dueTime = findViewById(R.id.due_time);
        dueWhen = findViewById(R.id.due_when);
        taskTitle = findViewById(R.id.task_title);
        taskDetails = findViewById(R.id.task_details);
        markAsDone = findViewById(R.id.mark_as_done);
        doneIconView = findViewById(R.id.done_checkbox);
        markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneIconView.switchState(true);
                getCourseDataInstance().updateTaskCompleted(assignmentDataModel, doneIconView.isIconEnabled());
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
                                    if (assignmentDataModel.getDueDate() != null) {
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
        assignmentDataModel = getCourseDataInstance().AssignmentsID.get(assignmentID);

        Timber.d("onStart,Calling updateFields");

        updateFields();

    }

    public void updateFields() {


        if (assignmentDataModel.getDueDate() != null) {
            ((LinearLayout) (dueDate.getParent())).setVisibility(View.VISIBLE);
            dueDate.setText(assignmentDataModel.getDueDateOriginal().toString("E, d MMM"));

            if (!assignmentDataModel.getCompleted()) {

                dueWhen.setVisibility(View.VISIBLE);

                int quizDay = assignmentDataModel.getDueDateOriginal().getDayOfYear();

                LocalDate today = new LocalDate();

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
                else dueWhen.setText("Upcoming!");

            } else
                dueWhen.setVisibility(View.INVISIBLE);


        } else {
            ((LinearLayout) (dueDate.getParent().getParent())).setVisibility(View.GONE);
            dueWhen.setVisibility(View.INVISIBLE);
        }


        if (assignmentDataModel.getDueTime() != null) {
            dueTime.setVisibility(View.VISIBLE);
            dueTime.setText(assignmentDataModel.getDueTimeOriginal().toString("h:mm a"));
        } else
            ((LinearLayout) (dueTime.getParent().getParent())).setVisibility(View.GONE);


        taskTitle.setText(assignmentDataModel.getAssignmentTitle());

        if (assignmentDataModel.getAssignmentDescription() != null) {
            ((View) (taskDetails.getParent())).setVisibility(View.VISIBLE);

            taskDetails.setText(assignmentDataModel.getAssignmentDescription());
        } else {

            ((CardView) (taskDetails.getParent())).setVisibility(View.GONE);
        }


//        if (assignmentDataModel.getAssignmentDescription() != null) {
//            taskDetails.setVisibility(View.VISIBLE);
//            taskDetails.setText(assignmentDataModel.getAssignmentDescription());
//        } else {
//            taskDetails.setVisibility(GONE);
//        }


        doneIconView.setIconEnabled(assignmentDataModel.getCompleted());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.edit_class:
                Intent intent = new Intent(AssignmentDetailActivity.this, EditAssignmentActivity.class);
//                intent.putExtras(bundle);
                intent.putExtra("courseID", courseID);
                intent.putExtra("assignmentID", assignmentID);
                startActivityForResult(intent, REQUEST_CODE_EDIT_ASSIGNMENT);
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
        if (requestCode == REQUEST_CODE_EDIT_ASSIGNMENT) {
//            Timber.d("onActivityResult,calling updateFields");
//            updateFields();

            if (resultCode == RESULT_CODE_FINISH_ACTIVITY) {
                finish();
            }
        }
    }
}
