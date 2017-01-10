package com.sensei.Activities;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sensei.Adapters.CoursesListAdapter;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;
import com.sensei.Utils.NavigationDrawerSetup;

import java.util.ArrayList;
import java.util.List;

import static com.sensei.Application.MyApplication.UID;
import static com.sensei.Application.MyApplication.databaseReference;
import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class CoursesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;
    private RecyclerView recyclerView;
    public CoursesListAdapter coursesListAdapter;
//    public List<CourseDataModel> CoursesList = new ArrayList<>();
//    DatabaseReference coursesRef = databaseReference.child("courses").child(UID);
//    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Courses");

//        valueEventListener = new ValueEventListener() {
//            boolean wasCalled = false;
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (wasCalled) {
//
//                } else {
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        getCourseDataInstance().CoursesList.add(postSnapshot.getValue(CourseDataModel.class));
//                        coursesListAdapter.notifyDataSetChanged();
//                    }
//                    wasCalled = true;
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        coursesListAdapter = new CoursesListAdapter(CoursesActivity.this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(coursesListAdapter);


    }

    public void onStart() {
        super.onStart();
//        coursesRef.addValueEventListener(valueEventListener);
        getCourseDataInstance().registerCoursesActivity(CoursesActivity.this);
        navigationDrawerSetup.ConfigureDrawer();
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
