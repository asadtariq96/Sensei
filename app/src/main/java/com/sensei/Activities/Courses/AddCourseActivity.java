package com.sensei.Activities.Courses;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sensei.Application.Constants;
import com.sensei.DataModelClasses.ClassDataModel;

import static com.sensei.Application.Constants.COLORS_LIST;
import static com.sensei.Application.Constants.DEFAULT_START_TIME;
import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static com.sensei.R.id.friday_class_linear_layout;
import static com.sensei.R.id.monday_class_linear_layout;
import static com.sensei.R.id.saturday_class_linear_layout;
import static com.sensei.R.id.thursday_class_linear_layout;
import static com.sensei.R.id.tuesday_class_linear_layout;
import static com.sensei.R.id.wednesday_class_linear_layout;

public class AddCourseActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {
    TextInputEditText CourseName;
    TextInputEditText CourseAbbreviation;
    TextInputEditText CourseInstructor;
    TextInputEditText CreditHours;
    LinearLayout ColorSelector;
    View ColorBox;
    Button MondayAdd;
    Button TuesdayAdd;
    Button WednesdayAdd;
    Button ThursdayAdd;
    Button FridayAdd;
    Button SaturdayAdd;
    LinearLayout MondayClassContainer;
    LinearLayout TuesdayClassContainer;
    LinearLayout WednesdayClassContainer;
    LinearLayout ThursdayClassContainer;
    LinearLayout FridayClassContainer;
    LinearLayout SaturdayClassContainer;
    ToggleButton MonToggle;
    ToggleButton TueToggle;
    ToggleButton WedToggle;
    ToggleButton ThuToggle;
    ToggleButton FriToggle;
    ToggleButton SatToggle;
    LinearLayout Monday;
    LinearLayout Tuesday;
    LinearLayout Wednesday;
    LinearLayout Thursday;
    LinearLayout Friday;
    LinearLayout Saturday;

    List<ClassDataModel> ClassesList = new ArrayList<>();
    List<LinearLayout> ContainerList;
    private MenuItem SaveMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(AddCourseActivity.this, "onDataChange Fired", Toast.LENGTH_SHORT).show();                // ...
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("AddCourseActivity", "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//        DatabaseReference courses = databaseReference.child("courses");
//        courses.addValueEventListener(valueEventListener);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Course");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ColorBox = findViewById(R.id.color_box);
        ColorBox.setBackgroundColor(Constants.getRandomColor());

        CourseName = (TextInputEditText) findViewById(R.id.course_name);
        CourseAbbreviation = (TextInputEditText) findViewById(R.id.course_abbreviation);
        CourseInstructor = (TextInputEditText) findViewById(R.id.course_instructor);
        CreditHours = (TextInputEditText) findViewById(R.id.credit_hours);
        CourseName.setHorizontallyScrolling(false);
        CourseName.setMaxLines(Integer.MAX_VALUE);
        CourseAbbreviation.setHorizontallyScrolling(false);
        CourseAbbreviation.setMaxLines(1);
        CourseInstructor.setHorizontallyScrolling(false);
        CourseInstructor.setMaxLines(1);
        CreditHours.setHorizontallyScrolling(false);
        CreditHours.setMaxLines(1);
        CreditHours.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {
                    {
                        CreditHours.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(CreditHours.getWindowToken(), 0);
                    }
                }
                return false;
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


                if (!CourseName.getText().toString().trim().equals("") && !CourseAbbreviation.getText().toString().trim().equals("")) {
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


        MonToggle = (ToggleButton) findViewById(R.id.toggle_mon);
        TueToggle = (ToggleButton) findViewById(R.id.toggle_tue);
        WedToggle = (ToggleButton) findViewById(R.id.toggle_wed);
        ThuToggle = (ToggleButton) findViewById(R.id.toggle_thu);
        FriToggle = (ToggleButton) findViewById(R.id.toggle_fri);
        SatToggle = (ToggleButton) findViewById(R.id.toggle_sat);

        Monday = (LinearLayout) findViewById(R.id.monday);
        Tuesday = (LinearLayout) findViewById(R.id.tuesday);
        Wednesday = (LinearLayout) findViewById(R.id.wednesday);
        Thursday = (LinearLayout) findViewById(R.id.thursday);
        Friday = (LinearLayout) findViewById(R.id.friday);
        Saturday = (LinearLayout) findViewById(R.id.saturday);

        MonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MonToggle.isChecked()) {
                    Monday.setVisibility(View.VISIBLE);
                } else {
                    Monday.setVisibility(View.GONE);
                    deleteCoursesForThatDay(1);
                }

            }
        });

        TueToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TueToggle.isChecked()) {
                    Tuesday.setVisibility(View.VISIBLE);
                } else {
                    Tuesday.setVisibility(View.GONE);
                    deleteCoursesForThatDay(2);
                }
            }
        });

        WedToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WedToggle.isChecked()) {
                    Wednesday.setVisibility(View.VISIBLE);
                } else {
                    Wednesday.setVisibility(View.GONE);
                    deleteCoursesForThatDay(3);
                }
            }
        });

        ThuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ThuToggle.isChecked()) {
                    Thursday.setVisibility(View.VISIBLE);
                } else {
                    Thursday.setVisibility(View.GONE);
                    deleteCoursesForThatDay(4);
                }
            }
        });

        FriToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FriToggle.isChecked()) {
                    Friday.setVisibility(View.VISIBLE);
                } else {
                    Friday.setVisibility(View.GONE);
                    deleteCoursesForThatDay(5);
                }
            }
        });

        SatToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SatToggle.isChecked()) {
                    Saturday.setVisibility(View.VISIBLE);
                } else {
                    Saturday.setVisibility(View.GONE);
                    deleteCoursesForThatDay(6);
                }
            }
        });


        MondayAdd = (Button) findViewById(R.id.monday_add);
        MondayClassContainer = (LinearLayout) findViewById(monday_class_linear_layout);
        TuesdayAdd = (Button) findViewById(R.id.tuesday_add);
        TuesdayClassContainer = (LinearLayout) findViewById(tuesday_class_linear_layout);
        WednesdayAdd = (Button) findViewById(R.id.wednesday_add);
        WednesdayClassContainer = (LinearLayout) findViewById(wednesday_class_linear_layout);
        ThursdayAdd = (Button) findViewById(R.id.thursday_add);
        ThursdayClassContainer = (LinearLayout) findViewById(thursday_class_linear_layout);
        FridayAdd = (Button) findViewById(R.id.friday_add);
        FridayClassContainer = (LinearLayout) findViewById(friday_class_linear_layout);
        SaturdayAdd = (Button) findViewById(R.id.saturday_add);
        SaturdayClassContainer = (LinearLayout) findViewById(saturday_class_linear_layout);


        final LayoutInflater inflater = getLayoutInflater();

        final List<Button> ButtonsList = new ArrayList<>();
        ButtonsList.add(MondayAdd);
        ButtonsList.add(TuesdayAdd);
        ButtonsList.add(WednesdayAdd);
        ButtonsList.add(ThursdayAdd);
        ButtonsList.add(FridayAdd);
        ButtonsList.add(SaturdayAdd);

        ContainerList = new ArrayList<>();
        ContainerList.add(MondayClassContainer);
        ContainerList.add(TuesdayClassContainer);
        ContainerList.add(WednesdayClassContainer);
        ContainerList.add(ThursdayClassContainer);
        ContainerList.add(FridayClassContainer);
        ContainerList.add(SaturdayClassContainer);

        for (final Button button : ButtonsList) {

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final View classView = inflater.inflate(R.layout.add_class_view, ContainerList.get(ButtonsList.indexOf(button)), false);
                    ContainerList.get(ButtonsList.indexOf(button)).addView(classView);
                    final ClassDataModel classDataModel = new ClassDataModel();
                    ClassesList.add(classDataModel);
                    classDataModel.setDayOfWeek(ButtonsList.indexOf(button) + 1);
                    classDataModel.setStartTime(getStartTimeOfNewClass(ButtonsList.indexOf(button) + 1).toString());
                    classDataModel.setEndTime(classDataModel.getStartTimeOriginal().plusMinutes(Constants.DEFAULT_CLASS_LENGTH).toString());
                    classDataModel.setClassType(ClassDataModel.ClassType.LECTURE.toString());
                    classDataModel.setLocation("");

                    ((TextView) classView.findViewById(R.id.start_time)).setText(classDataModel.getStartTimeOriginal().toString("h:mm a"));
                    ((TextView) classView.findViewById(R.id.end_time)).setText(classDataModel.getEndTimeOriginal().toString("h:mm a"));
                    ((TextInputEditText) classView.findViewById(R.id.location)).setHorizontallyScrolling(false);
                    ((TextInputEditText) classView.findViewById(R.id.location)).setMaxLines(Integer.MAX_VALUE);


                    ((EditText) classView.findViewById(R.id.location)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() != 0)
                                classDataModel.setLocation(charSequence.toString());
                            else
                                classDataModel.setLocation("");

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    classView.findViewById(R.id.delete_class).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ClassesList.remove(classDataModel);
                            ContainerList.get(ButtonsList.indexOf(button)).removeView(classView);
                        }
                    });

                    ((RadioGroup) classView.findViewById(R.id.radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            classDataModel.setClassType(i == 0 ? ClassDataModel.ClassType.LECTURE.toString() : ClassDataModel.ClassType.LAB.toString());

                        }
                    });


                    classView.findViewById(R.id.start_time_container).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TimePickerDialog.OnTimeSetListener onTimeSetListener;

                            onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                    LocalTime localTime = new LocalTime(hourOfDay, minute, second);
                                    classDataModel.setStartTime(localTime.toString());
                                    ((TextView) classView.findViewById(R.id.start_time)).setText(localTime.toString("h:mm a"));
                                    ((TextView) classView.findViewById(R.id.end_time)).setText(localTime.plusMinutes(Constants.DEFAULT_CLASS_LENGTH).toString("h:mm a"));
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


                    classView.findViewById(R.id.end_time_container).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TimePickerDialog.OnTimeSetListener onTimeSetListener;

                            onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {


                                    LocalTime localTime = new LocalTime(hourOfDay, minute, second);

                                    if (localTime.isBefore(classDataModel.getStartTimeOriginal())) {
                                        Toast.makeText(AddCourseActivity.this, "Oops! A class can not end before it starts!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        classDataModel.setEndTime(localTime.toString());
                                        ((TextView) classView.findViewById(R.id.end_time)).setText(localTime.toString("h:mm a"));
                                    }

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
                }
            });


        }


        ColorSelector = (LinearLayout) findViewById(R.id.color_chooser);

        ColorSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorChooserDialog.Builder(AddCourseActivity.this, R.string.color_palette)
                        .allowUserColorInput(false)
                        .customColors(COLORS_LIST, null)
                        .preselect(((ColorDrawable) ColorBox.getBackground()).getColor())
                        .doneButton(R.string.md_done_label)  // changes label of the done button
                        .cancelButton(R.string.md_cancel_label)  // changes label of the cancel button
                        .backButton(R.string.md_back_label)  // changes label of the back button
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


                CourseDataModel courseDataModel = new CourseDataModel(CourseName.getText().toString().trim(),
                        CourseAbbreviation.getText().toString().trim(),
                        ((ColorDrawable) ColorBox.getBackground()).getColor(),
                        CourseInstructor.getText().toString().trim(),
                        (CreditHours.getText().toString().isEmpty() ? 0 : Integer.valueOf(CreditHours.getText().toString()))
                );


                getCourseDataInstance().addCourse(courseDataModel, ClassesList);


//                databaseReference.child("courses")
//                        .child(UID)
//                        .child(courseID)
//                        .setValue(courseDataModel);
//
//                for (ClassDataModel classDataModel : ClassesList) {
//
//                    getCourseDataInstance().addClassToCourse(courseID, classDataModel);
////                    String ClassID = databaseReference.child("courses").child(UID).child(courseID).child("classes").push().getKey();
////                    databaseReference.child("courses").child(UID).child(courseID).child("classes").child(ClassID).setValue(classDataModel);
//
//                }

                AddCourseActivity.this.finish();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_save_menu, menu);
        SaveMenu = menu.getItem(0);
        SaveMenu.setEnabled(false);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        ColorBox.setBackgroundColor(selectedColor);

    }

    public LocalTime getStartTimeOfNewClass(int dayOfWeek) {
        //TODO add default start time here
        LocalTime localTime = DEFAULT_START_TIME;

        for (ClassDataModel classDataModel : ClassesList) {
            if (classDataModel.getDayOfWeek() == dayOfWeek && classDataModel.getEndTimeOriginal() != null) {
                localTime = classDataModel.getEndTimeOriginal().plusMinutes(Constants.DEFAULT_BREAK_LENGTH);
            }
        }

        return localTime;
    }

    public void deleteCoursesForThatDay(int dayOfWeek) {
        Iterator<ClassDataModel> classDataModelIterator = ClassesList.iterator();
        while (classDataModelIterator.hasNext()) {
            ClassDataModel obj = classDataModelIterator.next(); // must be called before you can call i.remove()
            if (obj.getDayOfWeek() == dayOfWeek) {
                classDataModelIterator.remove();
                ContainerList.get(dayOfWeek - 1).removeAllViews();
            }
        }
    }

}
