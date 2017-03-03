package com.sensei.assistant.Activities.Courses;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;

import static com.sensei.assistant.Application.Constants.COLORS_LIST;
import static com.sensei.assistant.Application.Constants.RESULT_CODE_FINISH_ACTIVITY;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class EditCourseDetailsActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {
    TextInputEditText CourseName;
    TextInputEditText CourseAbbreviation;
    TextInputEditText CourseInstructor;
    TextInputEditText CreditHours;
    LinearLayout ColorSelector;
    View ColorBox;
    Button DeleteCourse;
    String courseID;
    private CourseDataModel courseDataModel;
    private MenuItem SaveMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course_details);

        Intent intent = getIntent();
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
        getSupportActionBar().setTitle("Edit Course");
        getSupportActionBar().setSubtitle(courseDataModel.getCourseAbbreviation());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CourseName = (TextInputEditText) findViewById(R.id.course_name);
        CourseName.setHorizontallyScrolling(false);
        CourseName.setMaxLines(Integer.MAX_VALUE);
        CourseAbbreviation = (TextInputEditText) findViewById(R.id.course_abbreviation);
        CourseInstructor = (TextInputEditText) findViewById(R.id.course_instructor);
        CreditHours = (TextInputEditText) findViewById(R.id.credit_hours);
        ColorBox = findViewById(R.id.color_box);
        ColorBox.setBackgroundColor(courseDataModel.getCourseColorCode());

        CourseName.setText(courseDataModel.getCourseName());
        CourseAbbreviation.setText(courseDataModel.getCourseAbbreviation());
        CourseInstructor.setText(courseDataModel.getInstructor());
        if (courseDataModel.getCreditHours() != 0)
            CreditHours.setText(String.valueOf(courseDataModel.getCreditHours()));
        DeleteCourse = (Button) findViewById(R.id.delete_course);
        DeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(EditCourseDetailsActivity.this)
                        .title("Delete Course")
                        .content("Are you sure you want to delete this course? All data associated with this course will also be deleted.")
                        .positiveText("DELETE")
                        .canceledOnTouchOutside(false)
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteCourse();

                            }
                        })
                        .negativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .build().show();
            }
        });

        ColorSelector = (LinearLayout) findViewById(R.id.color_chooser);

        ColorSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new ColorChooserDialog.Builder(EditCourseDetailsActivity.this, R.string.color_palette)
                        .allowUserColorInput(false)
                        .customColors(COLORS_LIST, null)
                        .preselect(((ColorDrawable) ColorBox.getBackground()).getColor())
                        .doneButton(R.string.md_done_label)  // changes label of the done button
                        .cancelButton(R.string.md_cancel_label)  // changes label of the cancel button
                        .backButton(R.string.md_back_label)  // changes label of the back button
                        .show();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (!CourseName.getText().toString().trim().equals("")
                        && !CourseAbbreviation.getText().toString().trim().equals("")
                        && !CreditHours.getText().toString().isEmpty()
                        && Integer.parseInt(CreditHours.getText().toString()) != 0) {
                    if (SaveMenu != null)
                        SaveMenu.setEnabled(true);
                } else {
                    if (SaveMenu != null)
                        SaveMenu.setEnabled(false);
                }
            }
        };

        CourseName.addTextChangedListener(textWatcher);
        CourseAbbreviation.addTextChangedListener(textWatcher);
        CreditHours.addTextChangedListener(textWatcher);

    }

    private void deleteCourse() {
        getCourseDataInstance().deleteCourse(courseID);
        setResult(RESULT_CODE_FINISH_ACTIVITY);
        finish();
    }

    public void updateCourse() {
        getCourseDataInstance().updateCourse(courseDataModel, courseID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_save_menu, menu);
        SaveMenu = menu.getItem(0);
        SaveMenu.setEnabled(true);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.save_item:

                courseDataModel.setCourseName(CourseName.getText().toString().trim());
                courseDataModel.setCourseAbbreviation(CourseAbbreviation.getText().toString().trim());
                courseDataModel.setInstructor(CourseInstructor.getText().toString().trim());

                courseDataModel.setCreditHours(CreditHours.getText().toString().isEmpty() ? 0 : Integer.valueOf(CreditHours.getText().toString()));
//                CourseDataModel courseDataModel = new CourseDataModel(CourseName.getText().toString().trim(),
//                        CourseAbbreviation.getText().toString().trim(),
//                        ((ColorDrawable) ColorBox.getBackground()).getColor(),
//                        CourseInstructor.getText().toString().trim(),
//                        Integer.valueOf(CreditHours.getText().toString()));

                getCourseDataInstance().updateCourse(courseDataModel, courseID);
                EditCourseDetailsActivity.this.finish();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        courseDataModel.setCourseColorCode(selectedColor);
        ColorBox.setBackgroundColor(selectedColor);
        findViewById(R.id.toolbar).setBackgroundColor(courseDataModel.getCourseColorCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(courseDataModel.getDarkerColor());
        }

    }
}
