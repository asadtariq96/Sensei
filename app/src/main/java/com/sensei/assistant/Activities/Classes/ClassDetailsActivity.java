package com.sensei.assistant.Activities.Classes;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;

import timber.log.Timber;

import static com.sensei.assistant.Application.Constants.REQUEST_CODE_EDIT_CLASS;
import static com.sensei.assistant.Application.Constants.RESULT_CODE_FINISH_ACTIVITY;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static com.sensei.assistant.R.id.location;

public class ClassDetailsActivity extends AppCompatActivity {
    RadioGroup daysOfWeek;
    TextView classLocation;
    TextView StartTime;
    TextView EndTime;
    TextView classType;
    ClassDataModel classDataModel;
    CourseDataModel courseDataModel;
    private String classID;
    private String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);


        Intent intent = getIntent();
        classID = intent.getStringExtra("classID");
        courseID = intent.getStringExtra("courseID");

        courseDataModel = getCourseDataInstance().CoursesID.get(courseID);
        classDataModel = getCourseDataInstance().ClassesID.get(classID);

//        courseDataModel = Parcels.unwrap(intent.getParcelableExtra("courseObject"));
//        classDataModel = Parcels.unwrap(intent.getParcelableExtra("classObject"));
        findViewById(R.id.toolbar).setBackgroundColor(courseDataModel.getCourseColorCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(courseDataModel.getDarkerColor());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Class Details");
        getSupportActionBar().setSubtitle(courseDataModel.getCourseAbbreviation());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        classLocation = (TextView) findViewById(location);
        StartTime = (TextView) findViewById(R.id.start_time);
        EndTime = (TextView) findViewById(R.id.end_time);
        classType = (TextView) findViewById(R.id.class_type);
        daysOfWeek = (RadioGroup) findViewById(R.id.toggle_group);

//        Timber.d("onCreate, calling updateFields");
//        updateFields();

//        RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
//                f or (int j = 1; j < radioGroup.getChildCount(); j = j + 2) {
//                    final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
//                    view.setChecked(view.getId() == i);
//                }
//            }
//        };


//        classType.check(classDataModel.getClassType() == "LECTURE" ? R.id.rb_lecture : R.id.rb_lab);


    }

    public void updateFields() {
        Timber.d("updateFields,dayOfWeek:" + classDataModel.getDayOfWeek());
        clearChecked();
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


        classLocation.setText(classDataModel.getLocation().isEmpty() ? "No location specified" : classDataModel.getLocation());
        StartTime.setText(classDataModel.getStartTimeOriginal().toString("h:mm a"));
        EndTime.setText(classDataModel.getEndTimeOriginal().toString("h:mm a"));
        classType.setText(classDataModel.getClassType().toLowerCase());
    }

//    public void onToggle(View view) {
//        ((RadioGroup) view.getParent()).clearCheck();
//        ((RadioGroup) view.getParent()).check(view.getId());
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.edit_class:
//                Bundle bundle = new Bundle();
//                Parcelable classObject = Parcels.wrap(classDataModel);
//                bundle.putParcelable("classObject", classObject);
//                Parcelable courseObject = Parcels.wrap(courseDataModel);
//                bundle.putParcelable("courseObject", courseObject);
                Intent intent = new Intent(ClassDetailsActivity.this, EditClassDetailsActivity.class);
//                intent.putExtras(bundle);
                intent.putExtra("courseID", courseID);
                intent.putExtra("classID", classID);
                startActivityForResult(intent, REQUEST_CODE_EDIT_CLASS);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.class_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onStart() {
        super.onStart();
        classDataModel = getCourseDataInstance().ClassesID.get(classID);

        Timber.d("onStart,Calling updateFields");

        updateFields();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_EDIT_CLASS) {
//            Timber.d("onActivityResult,calling updateFields");
//            updateFields();

            if (resultCode == RESULT_CODE_FINISH_ACTIVITY) {
                finish();
            }
        }
    }

    public void clearChecked(){
        for (int j = 1; j < daysOfWeek.getChildCount(); j = j + 2) {
            final ToggleButton view = (ToggleButton) daysOfWeek.getChildAt(j);
            view.setChecked(false);

        }
    }


}
