package com.sensei.Activities;

import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.sensei.R;
import com.thebluealliance.spectrum.internal.ColorCircleDrawable;

import org.joda.time.DateTime;

import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.Utils.DateTimeDialogBuilder;

import static com.sensei.Application.MyApplication.CourseList;

public class AddAssignmentActivity extends AppCompatActivity {
    TextInputEditText CourseName;
    TextInputEditText Reminder;
    TextInputEditText DueDate;
    TextInputEditText DueTime;

    public DateTime ReminderDateTime;
    public DateTime DueDateTime;
    DateTimeDialogBuilder dateTimeDialogBuilder = new DateTimeDialogBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Assignment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CourseName = (TextInputEditText) findViewById(R.id.course);
        Reminder = (TextInputEditText) findViewById(R.id.reminder);
        DueDate = (TextInputEditText) findViewById(R.id.due_date);
        DueTime = (TextInputEditText) findViewById(R.id.due_time);

        CourseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
                    @Override
                    public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                        CourseName.setText(CourseList.get(index).getCourseName());
                        CourseName.setTextColor(ActivityCompat.getColor(getApplicationContext(), R.color.primary_text_material_light));
                        dialog.dismiss();
                    }
                });

                for (CourseDataModel item : CourseList) {
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
                dateTimeDialogBuilder.DueDatePicker(DueDate, AddAssignmentActivity.this);
            }
        });

        DueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialogBuilder.DueTimePicker(DueTime, AddAssignmentActivity.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
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
