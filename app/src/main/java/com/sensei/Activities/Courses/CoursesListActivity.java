package com.sensei.Activities.Courses;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.clans.fab.FloatingActionButton;
import com.sensei.Adapters.CoursesListAdapterBRVAH;
import com.sensei.DataHandlers.CourseDataHandler;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;
import com.sensei.Utils.NavigationDrawerSetup;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class CoursesListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;
    private RecyclerView recyclerView;
    //    public CoursesListAdapter coursesListAdapter;
    public CoursesListAdapterBRVAH adapter;
    FloatingActionButton addCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Courses");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);

        addCourse = (FloatingActionButton) findViewById(R.id.add_course);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoursesListActivity.this, AddCourseActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        coursesListAdapter = new CoursesListAdapter(CoursesListActivity.this);
        adapter = new CoursesListAdapterBRVAH(R.layout.course_layout, getCourseDataInstance().CoursesList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                CourseDataModel courseDataModel = getCourseDataInstance().CoursesList.get(i);
                final Intent intent = new Intent(CoursesListActivity.this, CourseDetailActivity.class);
                String courseID = getCourseDataInstance().getCourseID(courseDataModel);
                intent.putExtra("courseID", courseID);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(intent);

                    }
                }, 200);
//                startActivity(new Intent(CoursesListActivity.this, CourseDetailActivity.class   ));
            }
        });


    }

    public void onStart() {
        super.onStart();
//        coursesRef.addValueEventListener(valueEventListener);
        getCourseDataInstance().registerCoursesActivity(CoursesListActivity.this);
        navigationDrawerSetup.ConfigureDrawer();
        adapter.setNewData(getCourseDataInstance().CoursesList);
//        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void answerAvailable(CourseDataHandler.DataChangedEvent event) {
        adapter.setNewData(getCourseDataInstance().CoursesList);
        Timber.d("event received");
    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(3).setChecked(true); //select home by default in navigation drawer
    }

    public void onStop() {
        super.onStop();
        getCourseDataInstance().unregisterCoursesActivity();

    }
}
