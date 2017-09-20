package com.sensei.assistant.Activities.Assignments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sensei.assistant.DataModelClasses.AssignmentDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;

import static com.sensei.assistant.Application.Constants.RESULT_CODE_FINISH_ACTIVITY;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class EditAssignmentActivity extends AppCompatActivity {

    String assignmentID;
    String courseID;
    CourseDataModel courseDataModel;
    AssignmentDataModel assignmentDataModel;

    //    TextInputEditText CourseName;
    TextInputEditText Reminder;
    TextInputEditText DueDate;
    TextInputEditText DueTime;
    TextInputEditText Title;
    TextInputEditText Description;
    private MenuItem SaveMenu;

    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz_details);

        Intent intent = getIntent();
        assignmentID = intent.getStringExtra("assignmentID");
        courseID = intent.getStringExtra("courseID");
        courseDataModel = getCourseDataInstance().CoursesID.get(courseID);
        assignmentDataModel = getCourseDataInstance().AssignmentsID.get(assignmentID);

        findViewById(R.id.toolbar).setBackgroundColor(courseDataModel.getCourseColorCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(courseDataModel.getDarkerColor());
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Assignment");
        getSupportActionBar().setSubtitle(courseDataModel.getCourseAbbreviation());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        CourseName = (TextInputEditText) findViewById(R.id.course);
        Reminder = (TextInputEditText) findViewById(R.id.reminder);
        DueDate = (TextInputEditText) findViewById(R.id.due_date);
        DueTime = (TextInputEditText) findViewById(R.id.due_time);
        Title = (TextInputEditText) findViewById(R.id.title);
        Description = (TextInputEditText) findViewById(R.id.description);

        Title.setHorizontallyScrolling(false);
        Title.setMaxLines(Integer.MAX_VALUE);

//        CourseName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
//                    @Override
//                    public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
//                        courseDataModel = getCourseDataInstance().CoursesList.get(index);
//                        CourseName.setText(courseDataModel.getCourseName());
//                        CourseName.setTextColor(ActivityCompat.getColor(getApplicationContext(), R.color.primary_text_material_light));
//
//                        dialog.dismiss();
//                    }
//                });
//
//                for (CourseDataModel item : getCourseDataInstance().CoursesList) {
//                    adapter.add(new MaterialSimpleListItem.Builder(EditQuizActivity.this)
//                            .content(item.getCourseName())
//                            .icon(new ColorCircleDrawable(item.getCourseColorCode()))
//                            .backgroundColor(Color.WHITE)
//                            .build());
//                }
//
//
//                new MaterialDialog.Builder(EditQuizActivity.this)
//                        .title("Select Course")
//                        .adapter(adapter, null)
//                        .show();
//            }
//
//        });
//
//        CourseName.setText(courseDataModel.getCourseName());
//
//        if (assignmentDataModel.getDueDate() != null) {
//            DueDate.setText(assignmentDataModel.getDueDateOriginal().toString("E, d MMM"));
//        }


        DueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener onDateSetListener;

                onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        LocalDate localDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                        DueDate.setText(localDate.toString("d MMMM, yyyy"));
                        assignmentDataModel.setDueDate(localDate.toString());

                    }
                };


                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(onDateSetListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));

                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        DueDate.setText("");
                        assignmentDataModel.setDueDate(null);


                    }
                });
                dpd.show(getFragmentManager(), "dpd");


            }
        });

        if (assignmentDataModel.getDueDate() != null) {
            DueDate.setText(assignmentDataModel.getDueDateOriginal().toString("d MMMM, yyyy"));

        }

        if (assignmentDataModel.getDueTime() != null) {
            DueTime.setText(assignmentDataModel.getDueTimeOriginal().toString("h:mm a"));
        }

        DueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener;

                onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        LocalTime localTime = new LocalTime(hourOfDay, minute, second);
                        DueTime.setText(localTime.toString("h:mm a"));
                        assignmentDataModel.setDueTime(localTime.toString());

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(addQuizActivity, addQuizActivity.DueDateTime.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }, 2000);


                    }
                };

                LocalTime now = LocalTime.now();

                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        onTimeSetListener,
                        now.getHourOfDay(),
                        now.getMinuteOfHour(),
                        false);

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        DueTime.setText("");
                        assignmentDataModel.setDueTime(null);


                    }
                });

                tpd.show(getFragmentManager(), "Timepickerdialog");
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
                if (!Title.getText().toString().trim().equals("")) {
                    if (SaveMenu != null)
                        SaveMenu.setEnabled(true);
                } else {
                    if (SaveMenu != null)
                        SaveMenu.setEnabled(false);
                }

            }
        };

//        CourseName.addTextChangedListener(textWatcher);
        Title.addTextChangedListener(textWatcher);

        Title.setText(assignmentDataModel.getAssignmentTitle());

        if (assignmentDataModel.getAssignmentDescription() != null)
            Description.setText(assignmentDataModel.getAssignmentDescription());

        deleteButton = (Button) findViewById(R.id.delete_quiz);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(EditAssignmentActivity.this)
                        .cancelable(false)
                        .canceledOnTouchOutside(false)
                        .content("Are you sure you want to delete this assignment?")
                        .positiveText("DELETE")
                        .negativeText("CANCEL")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteAssignment();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.save_item:
//                if (!validateTimeFields())
//                    Toast.makeText(this, "Oops! A class can not end before it starts!", Toast.LENGTH_SHORT).show();
//                else
                updateAssignment();
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteAssignment() {
        getCourseDataInstance().deleteAssignment(courseID, assignmentID);
        setResult(RESULT_CODE_FINISH_ACTIVITY);
        finish();
    }

    public void updateAssignment() {
        assignmentDataModel.setAssignmentDescription(Description.getText().toString().trim());
        assignmentDataModel.setAssignmentTitle(Title.getText().toString().trim());
//        classDataModel.setLocation(classLocation.getText().toString().trim());
        getCourseDataInstance().updateAssignment(courseID, assignmentID, assignmentDataModel);

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_save_menu, menu);
        SaveMenu = menu.getItem(0);
        SaveMenu.setEnabled(true);
        return super.onCreateOptionsMenu(menu);


    }
}
