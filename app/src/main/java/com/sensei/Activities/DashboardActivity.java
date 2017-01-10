package com.sensei.Activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import com.sensei.Fragments.DashboardClassesFragment;
import com.sensei.Fragments.DashboardTasksFragment;
import com.sensei.R;
import com.sensei.Utils.NavigationDrawerSetup;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionMenu floatingActionMenu;
    com.github.clans.fab.FloatingActionButton course;
    com.github.clans.fab.FloatingActionButton quiz;
    com.github.clans.fab.FloatingActionButton homework;
    com.github.clans.fab.FloatingActionButton assignment;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;

    //    List<ClassDataModel> ClassesList = new ArrayList<>();
//    private ValueEventListener coursesValueEventListener;
//    DatabaseReference classesRef = databaseReference.child("classes").child(UID);
//    DatabaseReference coursesRef = databaseReference.child("courses").child(UID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        getCourseDataInstance().addChildListener();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        course = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_course);
        quiz = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_quiz);
        homework = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_homework);
        assignment = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_assignment);
        course.setOnClickListener(this);
        quiz.setOnClickListener(this);
        assignment.setOnClickListener(this);
        homework.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardClassesFragment(), "Classes");
        adapter.addFragment(new DashboardTasksFragment(), "Tasks");
        viewPager.setAdapter(adapter);

    }

    public void onStart() {
        super.onStart();
        navigationDrawerSetup.ConfigureDrawer();
    }

    public void onStop() {
        super.onStop();

    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true); //select home by default in navigation drawer
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            DashboardActivity.this.moveTaskToBack(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_course:
                startActivity(new Intent(DashboardActivity.this, AddCourseActivity.class));
                break;
            case R.id.fab_quiz:
                startActivity(new Intent(DashboardActivity.this, AddQuizActivity.class));
                break;
            case R.id.fab_assignment:
                startActivity(new Intent(DashboardActivity.this, AddAssignmentActivity.class));
                break;
            case R.id.fab_homework:
                startActivity(new Intent(DashboardActivity.this, AddHomeworkActivity.class));
                break;
        }
        floatingActionMenu.close(false);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
