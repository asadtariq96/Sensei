package com.sensei.assistant.Activities.Quizzes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.sensei.assistant.Activities.Classes.EditClassDetailsActivity;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.DateTimeDialogBuilder;
import com.thebluealliance.spectrum.internal.ColorCircleDrawable;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;

import static com.sensei.assistant.Application.Constants.RESULT_CODE_FINISH_ACTIVITY;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class EditQuizActivity extends AppCompatActivity {

    String quizID;
    String courseID;
    CourseDataModel courseDataModel;
    QuizDataModel quizDataModel;

    //    TextInputEditText CourseName;
    TextInputEditText Reminder;
    TextInputEditText DueDate;
    TextInputEditText DueTime;
    TextInputEditText Title;
    TextInputEditText Description;

    Button deleteButton;

    public DateTime DueDateTime;
    DateTimeDialogBuilder dateTimeDialogBuilder = new DateTimeDialogBuilder();
    private MenuItem SaveMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz_details);


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
        getSupportActionBar().setTitle("Edit Quiz");
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
//        if (quizDataModel.getDueDate() != null) {
//            DueDate.setText(quizDataModel.getDueDateOriginal().toString("E, d MMM"));
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
                        quizDataModel.setDueDate(localDate.toString());

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
                        quizDataModel.setDueDate(null);


                    }
                });
                dpd.show(getFragmentManager(), "dpd");


            }
        });

        if (quizDataModel.getDueDate() != null) {
            DueDate.setText(quizDataModel.getDueDateOriginal().toString("d MMMM, yyyy"));

        }

        if (quizDataModel.getDueTime() != null) {
            DueTime.setText(quizDataModel.getDueTimeOriginal().toString("h:mm a"));
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
                        quizDataModel.setDueTime(localTime.toString());

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
                        quizDataModel.setDueTime(null);


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

        Title.setText(quizDataModel.getQuizTitle());

        if (quizDataModel.getQuizDescription() != null)
            Description.setText(quizDataModel.getQuizDescription());

        deleteButton = (Button) findViewById(R.id.delete_quiz);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(EditQuizActivity.this)
                        .cancelable(false)
                        .canceledOnTouchOutside(false)
                        .content("Are you sure you want to delete this quiz?")
                        .positiveText("DELETE")
                        .negativeText("CANCEL")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteQuiz();
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
                updateQuiz();
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteQuiz() {
        getCourseDataInstance().deleteQuiz(courseID, quizID);
        setResult(RESULT_CODE_FINISH_ACTIVITY);
        finish();
    }

    public void updateQuiz() {
        quizDataModel.setQuizDescription(Description.getText().toString().trim());
        quizDataModel.setQuizTitle(Title.getText().toString().trim());
//        classDataModel.setLocation(classLocation.getText().toString().trim());
        getCourseDataInstance().updateQuiz(courseID, quizID, quizDataModel);

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
