package com.sensei.assistant.Activities.Classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sensei.assistant.Application.Constants;
import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalTime;

import static com.sensei.assistant.Application.Constants.RESULT_CODE_FINISH_ACTIVITY;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class EditClassDetailsActivity extends AppCompatActivity {
    RadioGroup daysOfWeek;
    EditText classLocation;
    TextView StartTime;
    TextView EndTime;
    RadioGroup classType;
    ClassDataModel classDataModel;
    CourseDataModel courseDataModel;
    LinearLayout StartTimeContainer;
    LinearLayout EndTimeContainer;
    private String classID;
    private String courseID;
    Button deleteClassBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class_details);

        Intent intent = getIntent();
        classID = intent.getStringExtra("classID");
        courseID = intent.getStringExtra("courseID");
        courseDataModel = getCourseDataInstance().CoursesID.get(courseID);
        classDataModel = getCourseDataInstance().ClassesID.get(classID);

        findViewById(R.id.toolbar).setBackgroundColor(courseDataModel.getCourseColorCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(courseDataModel.getDarkerColor());
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Class");
        getSupportActionBar().setSubtitle(courseDataModel.getCourseAbbreviation());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
                for (int j = 1; j < radioGroup.getChildCount(); j = j + 2) {
                    final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                    view.setChecked(view.getId() == i);

                }

                switch (i) {
                    case R.id.toggle_mon:
                        classDataModel.setDayOfWeek(1);
                        break;
                    case R.id.toggle_tue:
                        classDataModel.setDayOfWeek(2);
                        break;
                    case R.id.toggle_wed:
                        classDataModel.setDayOfWeek(3);
                        break;
                    case R.id.toggle_thu:
                        classDataModel.setDayOfWeek(4);
                        break;
                    case R.id.toggle_fri:
                        classDataModel.setDayOfWeek(5);
                        break;
                    case R.id.toggle_sat:
                        classDataModel.setDayOfWeek(6);
                        break;
                }


            }
        };

        daysOfWeek = (RadioGroup) findViewById(R.id.toggle_group);
        daysOfWeek.setOnCheckedChangeListener(ToggleListener);
        switch (classDataModel.getDayOfWeek()) {
            case 1:
                ((ToggleButton) findViewById(R.id.toggle_mon)).setChecked(true);
                break;
            case 2:
                ((ToggleButton) findViewById(R.id.toggle_tue)).setChecked(true);
                break;
            case 3:
                ((ToggleButton) findViewById(R.id.toggle_wed)).setChecked(true);
                break;
            case 4:
                ((ToggleButton) findViewById(R.id.toggle_thu)).setChecked(true);
                break;
            case 5:
                ((ToggleButton) findViewById(R.id.toggle_fri)).setChecked(true);
                break;
            case 6:
                ((ToggleButton) findViewById(R.id.toggle_sat)).setChecked(true);
                break;
        }

        classLocation = (EditText) findViewById(R.id.location);
        StartTime = (TextView) findViewById(R.id.start_time);
        EndTime = (TextView) findViewById(R.id.end_time);
        classType = (RadioGroup) findViewById(R.id.class_type);

        classLocation.setText(classDataModel.getLocation());
        StartTime.setText(classDataModel.getStartTimeOriginal().toString("h:mm a"));
        EndTime.setText(classDataModel.getEndTimeOriginal().toString("h:mm a"));
        classType.check(classDataModel.getClassType() == "LECTURE" ? R.id.rb_lecture : R.id.rb_lab);

        StartTimeContainer = (LinearLayout) findViewById(R.id.start_time_container);
        EndTimeContainer = (LinearLayout) findViewById(R.id.end_time_container);

        StartTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener;

                onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        LocalTime localTime = new LocalTime(hourOfDay, minute, second);
                        classDataModel.setStartTime(localTime.toString());
                        StartTime.setText(localTime.toString("h:mm a"));
                        EndTime.setText(localTime.plusMinutes(Constants.DEFAULT_CLASS_LENGTH).toString("h:mm a"));
                        classDataModel.setEndTime(localTime.plusMinutes(Constants.DEFAULT_CLASS_LENGTH).toString());


                    }
                };

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        onTimeSetListener,
                        classDataModel.getStartTimeOriginal().getHourOfDay(),
                        classDataModel.getStartTimeOriginal().getMinuteOfHour(),
                        false);

                timePickerDialog.show(getFragmentManager(), "tpd");

            }
        });

        EndTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener;

                onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        LocalTime localTime = new LocalTime(hourOfDay, minute, second);
                        classDataModel.setEndTime(localTime.toString());
                        EndTime.setText(localTime.toString("h:mm a"));


                    }
                };

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        onTimeSetListener,
                        classDataModel.getEndTimeOriginal().getHourOfDay(),
                        classDataModel.getEndTimeOriginal().getMinuteOfHour(),
                        false);

                timePickerDialog.show(getFragmentManager(), "tpd");

            }
        });

        classType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                classDataModel.setClassType(i == R.id.rb_lecture ? ClassDataModel.ClassType.LECTURE.toString() : ClassDataModel.ClassType.LAB.toString());

            }
        });


        deleteClassBtn = (Button) findViewById(R.id.delete_class);
        deleteClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(EditClassDetailsActivity.this)
                        .cancelable(false)
                        .canceledOnTouchOutside(false)
                        .content("Are you sure you want to delete this class?")
                        .positiveText("DELETE")
                        .negativeText("CANCEL")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteClass();
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

    private void deleteClass() {
        getCourseDataInstance().deleteClass(courseID, classID);
        setResult(RESULT_CODE_FINISH_ACTIVITY);
        finish();
    }

    public void updateClass() {
        classDataModel.setLocation(classLocation.getText().toString().trim());
        getCourseDataInstance().updateClass(courseID, classID, classDataModel);

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public boolean validateTimeFields() {
        return classDataModel.getEndTimeOriginal().isAfter(classDataModel.getStartTimeOriginal());

    }

    public void onToggle(View view) {
        ((RadioGroup) view.getParent()).clearCheck();
        ((RadioGroup) view.getParent()).check(view.getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.save_item:
                if (!validateTimeFields())
                    Toast.makeText(this, "Oops! A class can not end before it starts!", Toast.LENGTH_SHORT).show();
                else
                    updateClass();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_save_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

}
