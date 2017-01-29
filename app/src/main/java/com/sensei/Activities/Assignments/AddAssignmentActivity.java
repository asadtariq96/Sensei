package com.sensei.Activities.Assignments;

import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.sensei.DataModelClasses.AssignmentDataModel;
import com.sensei.R;
import com.thebluealliance.spectrum.internal.ColorCircleDrawable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.Utils.DateTimeDialogBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import static com.sensei.Application.MyApplication.UID;
import static com.sensei.Application.MyApplication.databaseReference;
import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;


public class AddAssignmentActivity extends AppCompatActivity {
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

    AssignmentDataModel assignmentDataModel;
    CourseDataModel courseDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Assignment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assignmentDataModel = new AssignmentDataModel();
        assignmentDataModel.setCompleted(false);


        CourseName = (TextInputEditText) findViewById(R.id.course);
        Reminder = (TextInputEditText) findViewById(R.id.reminder);
        DueDate = (TextInputEditText) findViewById(R.id.due_date);
        DueTime = (TextInputEditText) findViewById(R.id.due_time);
        Title = (TextInputEditText) findViewById(R.id.title);
        Description = (TextInputEditText) findViewById(R.id.description);

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
                    }
                });

                for (CourseDataModel item : getCourseDataInstance().CoursesList) {
                    adapter.add(new MaterialSimpleListItem.Builder(AddAssignmentActivity.this)
                            .content(item.getCourseName())
                            .icon(new ColorCircleDrawable(item.getCourseColorCode()))
                            .backgroundColor(Color.WHITE)
                            .build());
                }


                new MaterialDialog.Builder(AddAssignmentActivity.this)
                        .title("Select Course")
                        .adapter(adapter, null)
                        .show();
            }

        });


        Reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialogBuilder.BuildReminderDialog(AddAssignmentActivity.this, Reminder, AddAssignmentActivity.this);
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
//                        Toast.makeText(AddAssignmentActivity, AddAssignmentActivity.DueDateTime.toString(), Toast.LENGTH_SHORT).show();
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
                if (!CourseName.getText().toString().equals("") &&
                        !Title.getText().toString().trim().equals(""))
                    SaveMenu.setEnabled(true);
                else
                    SaveMenu.setEnabled(false);

            }
        };

        CourseName.addTextChangedListener(textWatcher);
        Title.addTextChangedListener(textWatcher);
    }

    public void addAssignment() {
        assignmentDataModel.setAssignmentTitle(Title.getText().toString().trim());
        assignmentDataModel.setAssignmentDescription(Description.getText().toString().trim());
        courseDataModel.getAssignments().add(assignmentDataModel);

        String AssignmentID = databaseReference
                .child("courses")
                .child(UID)
                .child(getCourseDataInstance().getCourseID(courseDataModel))
                .child("assignments").push().getKey();

        databaseReference
                .child("courses")
                .child(UID)
                .child(getCourseDataInstance().getCourseID(courseDataModel))
                .child("assignments")
                .child(AssignmentID)
                .setValue(assignmentDataModel);


        AddAssignmentActivity.this.finish();

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

                    addAssignment();


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
        return super.onCreateOptionsMenu(menu);


    }
}
