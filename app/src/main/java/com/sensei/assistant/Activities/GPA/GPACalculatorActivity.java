package com.sensei.assistant.Activities.GPA;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.NavigationDrawerSetup;

import java.util.ArrayList;
import java.util.List;

import static com.sensei.assistant.Application.Constants.GradesListStandard;
import static com.sensei.assistant.Application.Constants.gradesMap;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class GPACalculatorActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;
    private RecyclerView recyclerView;
    private GPAadapter gpaAdapter;
    private Button calculateGPA;
    private TextView gpaText;
    List<GPA> GPAList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpacalculator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GPA Calculator");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);

        calculateGPA = (Button) findViewById(R.id.calculate_gpa);
        gpaText = (TextView) findViewById(R.id.gpa);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

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

        adapter = new ArrayAdapter<>(GPACalculatorActivity.this, android.R.layout.simple_spinner_item, GradesListStandard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


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
            this.grade = gradesMap.get(gradeString);
        }
    }

    private void fillList() {
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
                    gpa.setGrade(gradesMap.get(GradesListStandard.get(i)));
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
        fillList();
        gpaAdapter.setNewData(GPAList);
        navigationDrawerSetup.ConfigureDrawer();
    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(7).setChecked(true); //select home by default in navigation drawer
    }
}
