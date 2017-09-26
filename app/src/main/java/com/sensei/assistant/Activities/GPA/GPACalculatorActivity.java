package com.sensei.assistant.Activities.GPA;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.NavigationDrawerSetup;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.sensei.assistant.Application.Constants.GradesListSchemeA;
import static com.sensei.assistant.Application.Constants.GradesListSchemeB;
import static com.sensei.assistant.Application.Constants.gradesMapSchemeA;
import static com.sensei.assistant.Application.Constants.gradesMapSchemeB;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class GPACalculatorActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;
    private GPAadapter gpaAdapter;
    private TextView gpaText;
    List<GPA> GPAList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int gradeScheme;
//    RadioGroup radioGroup;
//    RadioButton scheme1;
//    RadioButton scheme2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpacalculator);
        gradeScheme = 0;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GPA Calculator");

//        radioGroup = findViewById(R.id.radio_group);
//        scheme1 = findViewById(R.id.scheme1);
//        scheme2 = findViewById(R.id.scheme2);

        List<String> GradingSchemes = new ArrayList<String>() {{
            add("A, B+, B, C+, C");
            add("A, A-, B+, B,  B-, C+, C,  C-");
        }};

        new MaterialDialog.Builder(this)
                .title("Grading Scheme")
                .items(GradingSchemes)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        gradeScheme = which;
                        fillList();
                        gpaAdapter.setNewData(GPAList);
                        Timber.d("Grade Scheme %d", gradeScheme);
                        adapter = new ArrayAdapter<>(GPACalculatorActivity.this, android.R.layout.simple_spinner_item, (gradeScheme == 0 ? GradesListSchemeA : GradesListSchemeB));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);

        Button calculateGPA = findViewById(R.id.calculate_gpa);
        gpaText = findViewById(R.id.gpa);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        gpaAdapter = new GPAadapter(R.layout.gpa_course_layout, GPAList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(gpaAdapter);

        calculateGPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float totalCreditHours = 0;
                float score = 0;

                for (GPA gpa : GPAList) {
                    totalCreditHours = totalCreditHours + gpa.course.getCreditHours();
                    score = score + (gpa.getGrade() * gpa.course.getCreditHours());
                }

                float GPAcalculated = score / totalCreditHours;
                gpaText.setText("Your GPA is " + String.format("%.2f", GPAcalculated));


            }
        });


//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                Timber.d("Checked changed");
//
//                if (i == scheme1.getId()) {
//                    gradeScheme = 1;
//                } else if (i == scheme2.getId()) {
//                    gradeScheme = 2;
//                }
//
//                adapter = new ArrayAdapter<>(GPACalculatorActivity.this, android.R.layout.simple_spinner_item, (gradeScheme == 1 ? GradesListSchemeA : GradesListSchemeB));
//                fillList();
//            }
//        });


    }

    public class GPA {
        public float getGrade() {
            return grade;
        }

        public void setGrade(float grade) {
            this.grade = grade;
        }

        CourseDataModel course;
        float grade;

        GPA(CourseDataModel courseDataModel, String gradeString) {
            this.course = courseDataModel;
            if (gradeScheme == 0)
                this.grade = gradesMapSchemeA.get(gradeString);

            else if (gradeScheme == 1)
                this.grade = gradesMapSchemeB.get(gradeString);

        }
    }

    private void fillList() {
        Timber.d("fill List");
        GPAList.clear();
        for (CourseDataModel course : getCourseDataInstance().CoursesList) {
            if (course.getCreditHours() != 0)
                GPAList.add(new GPA(course, "A"));
        }
    }

    public class GPAadapter extends BaseQuickAdapter<GPA, BaseViewHolder> {

        public GPAadapter(int layoutResId, List<GPA> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final GPA gpa) {

            View colorView;
            TextView courseName;
            TextView creditHours;
            Spinner gradeSpinner;

            colorView = baseViewHolder.getView(R.id.color);
            courseName = baseViewHolder.getView(R.id.course_name);
            gradeSpinner = baseViewHolder.getView(R.id.spinner);
            creditHours = baseViewHolder.getView(R.id.credit_hours);


            courseName.setText(gpa.course.getCourseAbbreviation());
            creditHours.setText((String.valueOf(gpa.course.getCreditHours())));
            colorView.setBackgroundColor(gpa.course.getCourseColorCode());


            gradeSpinner.setAdapter(adapter);
//            gradeSpinner.setPadding(0,0,0,0);


            gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (gradeScheme == 0)
                        gpa.setGrade(gradesMapSchemeA.get(GradesListSchemeA.get(i)));
                    else if (gradeScheme == 1)
                        gpa.setGrade(gradesMapSchemeB.get(GradesListSchemeB.get(i)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }


    }

    public void onStart() {
        super.onStart();
//        coursesRef.addValueEventListener(valueEventListener);

        navigationDrawerSetup.ConfigureDrawer();
    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(7).setChecked(true); //select home by default in navigation drawer
    }
}
