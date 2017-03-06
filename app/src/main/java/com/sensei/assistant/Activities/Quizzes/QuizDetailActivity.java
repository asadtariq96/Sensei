package com.sensei.assistant.Activities.Quizzes;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.zagum.switchicon.SwitchIconView;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.R;

import timber.log.Timber;

import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class QuizDetailActivity extends AppCompatActivity {

    QuizDataModel quizDataModel;
    CourseDataModel courseDataModel;
    String courseID;
    String quizID;
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
        quizID = intent.getStringExtra("quizID");
        courseID = intent.getStringExtra("courseID");

        courseDataModel = getCourseDataInstance().CoursesID.get(courseID);
        quizDataModel = getCourseDataInstance().QuizzesID.get(quizID);

        findViewById(R.id.toolbar).setBackgroundColor(courseDataModel.getCourseColorCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(courseDataModel.getDarkerColor());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz Details");
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
            }
        });
    }

    public void updateFields() {



    }

}
