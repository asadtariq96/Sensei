package com.sensei.assistant.Activities.Quizzes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.sensei.assistant.Activities.Dashboard.DashboardActivity;
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

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class AddQuizActivity extends AppCompatActivity {

    TextInputEditText CourseName;
    TextInputEditText Reminder;
    TextInputEditText DueDate;
    TextInputEditText DueTime;
    TextInputEditText Title;
    TextInputEditText Description;

    public DateTime ReminderDateTime;
    public DateTime DueDateTime;
    DateTimeDialogBuilder dateTimeDialogBuilder = new DateTimeDialogBuilder();
    private MenuItem SaveMenu;

    QuizDataModel quizDataModel;
    CourseDataModel courseDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Quiz");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quizDataModel = new QuizDataModel();
        quizDataModel.setCompleted(false);


        CourseName = findViewById(R.id.course);
        Reminder = findViewById(R.id.reminder);
        DueDate = findViewById(R.id.due_date);
        DueTime = findViewById(R.id.due_time);
        Title = findViewById(R.id.title);
        Description = findViewById(R.id.description);

        Title.setHorizontallyScrolling(false);
        Title.setMaxLines(Integer.MAX_VALUE);


        CourseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
                    @Override
                    public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                        courseDataModel = getCourseDataInstance().CoursesList.get(index);
                        CourseName.setText(courseDataModel.getCourseName());
                        CourseName.setTextColor(ActivityCompat.getColor(getApplicationContext(), R.color.primary_text_material_light));

                        dialog.dismiss();

                        String getIntent = getIntent().getStringExtra("string");
                        if (getIntent != null) {
                            TapTargetView.showFor(AddQuizActivity.this,                 // `this` is an Activity
                                    TapTarget.forBounds(getBounds(DueDate), "Great! Now let's set a due date!")
                                            .outerCircleColor(R.color.md_deep_purple_700)
                                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                            .transparentTarget(true),           // Specify whether the target is transparent (displays the content underneath)
                                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                                        @Override
                                        public void onTargetClick(TapTargetView view) {
                                            super.onTargetClick(view);      // This call is optional
                                            DueDate.performClick();
                                        }
                                    });

                        }

                    }
                });

                for (CourseDataModel item : getCourseDataInstance().CoursesList) {
                    adapter.add(new MaterialSimpleListItem.Builder(AddQuizActivity.this)
                            .content(item.getCourseName())
                            .icon(new ColorCircleDrawable(item.getCourseColorCode()))
                            .backgroundColor(Color.WHITE)
                            .build());
                }


                new MaterialDialog.Builder(AddQuizActivity.this)
                        .title("Select Course")
                        .adapter(adapter, null)
                        .show();
            }

        });


        Reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialogBuilder.BuildReminderDialog(AddQuizActivity.this, Reminder);
            }
        });

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

                        String getIntent = getIntent().getStringExtra("string");
                        if (getIntent != null) {
                            TapTargetView.showFor(AddQuizActivity.this,                 // `this` is an Activity
                                    TapTarget.forToolbarMenuItem((Toolbar) findViewById(R.id.toolbar), R.id.save_item, "Amazing! Now let's save this quiz!")
                                            .outerCircleColor(R.color.md_deep_purple_700)
                                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                            .transparentTarget(true),           // Specify whether the target is transparent (displays the content underneath)
                                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                                        @Override
                                        public void onTargetClick(TapTargetView view) {
                                            super.onTargetClick(view);      // This call is optional
                                            addQuiz();
                                            startActivity(new Intent(AddQuizActivity.this, DashboardActivity.class).putExtra("string", "intro").setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT));
                                        }
                                    });
                        }

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
                if (!CourseName.getText().toString().equals("") && !Title.getText().toString().trim().equals("")) {
                    if (SaveMenu != null)
                        SaveMenu.setEnabled(true);
                } else {
                    if (SaveMenu != null)
                        SaveMenu.setEnabled(false);
                }

            }
        };

        CourseName.addTextChangedListener(textWatcher);
        Title.addTextChangedListener(textWatcher);
    }

    public void addQuiz() {
        quizDataModel.setQuizTitle(Title.getText().toString().trim());
        if (!Description.getText().toString().isEmpty())
            quizDataModel.setQuizDescription(Description.getText().toString().trim());

        getCourseDataInstance().addQuiz(courseDataModel, quizDataModel);
//        courseDataModel.getQuizzes().add(quizDataModel);
//
//        String QuizID = coursesReference
//                .child(getCourseDataInstance().getCourseID(courseDataModel))
//                .child("quizzes").push().getKey();
//
//        getCourseDataInstance().QuizzesID.put(QuizID, quizDataModel);
//
//        coursesReference
//                .child(getCourseDataInstance().getCourseID(courseDataModel))
//                .child("quizzes")
//                .child(QuizID)
//                .setValue(quizDataModel);


        AddQuizActivity.this.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;


            case R.id.save_item:

                if (DueDate.getText().toString().equals("") && !DueTime.getText().toString().isEmpty())
                    Toast.makeText(this, "Please set a due date.", Toast.LENGTH_SHORT).show();
                else

                    addQuiz();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_save_menu, menu);
        SaveMenu = menu.getItem(0);
        SaveMenu.setEnabled(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                showTutorial();

            }
        });
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SaveMenu = menu.getItem(0);

        return super.onPrepareOptionsMenu(menu);
    }

    private int dpTopx(int dp) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }

    private Rect getBounds(View view) {
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        Rect rect = new Rect(l[0] + view.getPaddingLeft() + dpTopx(16), l[1], l[0] + view.getPaddingLeft() + dpTopx(48), l[1] + view.getHeight());
        return rect;
    }

    private void showTutorial() {
        String getIntent = getIntent().getStringExtra("string");

        if (getIntent != null) {
            SaveMenu.setEnabled(true);

            Title.setText("Test Quiz");
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forBounds(getBounds(CourseName), "Now let's add a quiz for the course you just added!", "Choose Test Course from the dialog box!")
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .transparentTarget(true),           // Specify whether the target is transparent (displays the content underneath)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional
                            CourseName.performClick();
                        }
                    });


        }
    }

}
