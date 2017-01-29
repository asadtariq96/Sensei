package com.sensei.Activities.Classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.sensei.Application.Constants;
import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalTime;

import static android.R.attr.button;
import static com.sensei.Application.Constants.DEFAULT_START_TIME;
import static com.sensei.Application.MyApplication.UID;
import static com.sensei.Application.MyApplication.bus;
import static com.sensei.Application.MyApplication.databaseReference;
import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static com.sensei.R.id.course;

public class AddClassActivity extends AppCompatActivity {
    RadioGroup daysOfWeek;
    EditText classLocation;
    TextView StartTime;
    TextView EndTime;
    RadioGroup classType;
    ClassDataModel classDataModel;
    CourseDataModel courseDataModel;
    LinearLayout StartTimeContainer;
    LinearLayout EndTimeContainer;
    private String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);


        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");
        courseDataModel = getCourseDataInstance().CoursesID.get(courseID);
        classDataModel = new ClassDataModel();

        findViewById(R.id.toolbar).setBackgroundColor(courseDataModel.getCourseColorCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(courseDataModel.getDarkerColor());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add a class");
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
            }
        };

        daysOfWeek = (RadioGroup) findViewById(R.id.toggle_group);
        daysOfWeek.setOnCheckedChangeListener(ToggleListener);
        classLocation = (EditText) findViewById(R.id.location);
        StartTime = (TextView) findViewById(R.id.start_time);
        EndTime = (TextView) findViewById(R.id.end_time);
        classType = (RadioGroup) findViewById(R.id.class_type);

        classDataModel.setDayOfWeek(1);

        classDataModel.setStartTime(DEFAULT_START_TIME.toString());
        StartTime.setText(classDataModel.getStartTimeOriginal().toString("h:mm a"));

        classDataModel.setEndTime(classDataModel.getStartTimeOriginal().plusMinutes(Constants.DEFAULT_CLASS_LENGTH).toString());
        EndTime.setText(classDataModel.getEndTimeOriginal().toString("h:mm a"));

        classDataModel.setClassType(ClassDataModel.ClassType.LECTURE.toString());
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
    }

    public void addNewClass() {
//        Toast.makeText(this, courseID, Toast.LENGTH_SHORT).show();
        classDataModel.setLocation(classLocation.getText().toString().trim());
        switch (daysOfWeek.getCheckedRadioButtonId()) {
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


        getCourseDataInstance().addClassToCourse(courseID,classDataModel);

//        String ClassID = databaseReference.child("courses").child(UID).child(courseID).child("classes").push().getKey();
//        databaseReference.child("courses").child(UID).child(courseID).child("classes").child(ClassID).setValue(classDataModel);
//        courseDataModel.getClasses().add(classDataModel);
//        getCourseDataInstance().ClassesID.put(ClassID, classDataModel);

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
                    addNewClass();
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
