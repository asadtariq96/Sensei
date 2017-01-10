package com.sensei.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.sensei.Fragments.AssignmentsFragment;
import com.sensei.Fragments.CoursesFragment;
import com.sensei.Fragments.DashboardTasksFragment;
import com.sensei.Fragments.QuizzesFragment;
import com.sensei.Fragments.TaskFragment;
import com.sensei.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    FloatingActionMenu floatingActionMenu;
//    com.github.clans.fab.FloatingActionButton course;
//    com.github.clans.fab.FloatingActionButton quiz;
//    com.github.clans.fab.FloatingActionButton homework;
//    com.github.clans.fab.FloatingActionButton assignment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

//        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
//
//        course = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_course);
//        quiz = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_quiz);
//        homework = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_task);
//        assignment = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_assignment);
//
//        course.setOnClickListener(this);
//        quiz.setOnClickListener(this);
//        assignment.setOnClickListener(this);
//        homework.setOnClickListener(this);


    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardTasksFragment(), "Dashboard");
        adapter.addFragment(new CoursesFragment(), "Courses");
        adapter.addFragment(new QuizzesFragment(), "Quizzes");
        adapter.addFragment(new AssignmentsFragment(), "Assignments");
        adapter.addFragment(new TaskFragment(), "Tasks");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
//            case R.id.fab_course:
//                startActivity(new Intent(MainActivity.this, AddCourseActivity.class));
//                break;
//            case R.id.fab_quiz:
//                startActivity(new Intent(MainActivity.this, AddQuizActivity.class));
//                break;
//            case R.id.fab_assignment:
//                startActivity(new Intent(MainActivity.this, AddAssignmentActivity.class));
//                break;
//            case R.id.fab_task:
//                startActivity(new Intent(MainActivity.this, AddHomeworkActivity.class));
//                break;
        }

//        floatingActionMenu.close(false);

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
