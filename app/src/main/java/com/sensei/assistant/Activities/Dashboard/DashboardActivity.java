package com.sensei.assistant.Activities.Dashboard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.sensei.assistant.Activities.Assignments.AddAssignmentActivity;
import com.sensei.assistant.Activities.Homework.AddHomeworkActivity;
import com.sensei.assistant.Activities.Quizzes.AddQuizActivity;
import com.sensei.assistant.Activities.Courses.AddCourseActivity;
import com.sensei.assistant.Notifications.NotificationReceiver;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.NavigationDrawerSetup;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.prefs.PreferencesManager;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import io.github.kobakei.materialfabspeeddial.FabSpeedDialMenu;
import timber.log.Timber;

import static com.sensei.assistant.R.id.fabs_container;
import static com.sensei.assistant.R.id.weekView;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    //    FloatingActionMenu floatingActionMenu;
//    com.github.clans.fab.FloatingActionButton course;
//    com.github.clans.fab.FloatingActionButton quiz;
//    com.github.clans.fab.FloatingActionButton homework;
//    com.github.clans.fab.FloatingActionButton assignment;
    FabSpeedDial fabSpeedDialMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;
    private ViewPager viewPager;
    private PendingIntent pendingIntent;
    final String PREFS_NAME = "MyPrefsFile";


    //    List<ClassDataModel> ClassesList = new ArrayList<>();
//    private ValueEventListener coursesValueEventListener;
//    DatabaseReference classesRef = databaseReference.child("classes").child(UID);
//    DatabaseReference coursesRef = databaseReference.child("courses").child(UID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

//        getCourseDataInstance().getUserSettings();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);


        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

        fabSpeedDialMenu = findViewById(R.id.fabmenu);

        String getIntent = getIntent().getStringExtra("string");
        if (getIntent != null) {
            tabLayout.getTabAt(1).select();

        }

//        floatingActionMenu = findViewById(R.id.fab_menu);
//        floatingActionMenu.setClosedOnTouchOutside(true);
//        course = findViewById(R.id.fab_course);
//        quiz = findViewById(R.id.fab_quiz);
//        homework = findViewById(R.id.fab_homework);
//        assignment = findViewById(R.id.fab_assignment);
//        course.setOnClickListener(this);
//        quiz.setOnClickListener(this);
//        assignment.setOnClickListener(this);
//        homework.setOnClickListener(this);


        fabSpeedDialMenu.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton fab, TextView textView, int itemId) {
                // do something

                final Handler handler = new Handler();
                fabSpeedDialMenu.closeMenu();
                switch (itemId) {

                    case R.id.course:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(DashboardActivity.this, AddCourseActivity.class));
                            }
                        }, 100);
                        break;
                    case R.id.quiz:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(DashboardActivity.this, AddQuizActivity.class));
                            }
                        }, 100);
                        break;
                    case R.id.assignment:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(DashboardActivity.this, AddAssignmentActivity.class));
                            }
                        }, 100);
                        break;
                    case R.id.homework:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(DashboardActivity.this, AddHomeworkActivity.class));
                            }
                        }, 100);
                        break;
                }


            }
        });

//        PreferencesManager pref = new PreferencesManager(this);
//        pref.reset("fab");
//        pref.resetAll();

//        TapTargetView.showFor(this,                 // `this` is an Activity
//                TapTarget.forView(fabSpeedDialMenu.getMainFab(), "Welcome to Sensei!", "Let's start by adding a new course!")
//                        // All options below are optional
//                        .drawShadow(true)                   // Whether to draw a drop shadow or not
//                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
//                        .tintTarget(true)                   // Whether to tint the target view's color
//                        .transparentTarget(true),          // Specify whether the target is transparent (displays the content underneath)
//                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
//                    @Override
//                    public void onTargetClick(TapTargetView view) {
//                        super.onTargetClick(view);      // This call is optional
////                        doSomething();
//                        fabSpeedDialMenu.openMenu();
//                    }
//                });
//
//        final TapTarget target0 = TapTarget.forView(fabSpeedDialMenu.getMainFab(), "Welcome to Sensei!", "Let's start by adding a new course!")
//                .cancelable(false)
//                .transparentTarget(true);
//
//
//        final TapTarget target1 = TapTarget.forView(fabSpeedDialMenu.getMiniFab(0), "Press this button to add a new course! ")
//                .cancelable(false)
//                .transparentTarget(true);
        if (!getSharedPreferences(PREFS_NAME, 0).getBoolean("firstrun", false)) {
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(fabSpeedDialMenu.getMainFab(), "Welcome to Sensei!", "Let's start by adding a new course!")
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .transparentTarget(true),           // Specify whether the target is transparent (displays the content underneath)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional
                            fabSpeedDialMenu.openMenu();
                            TapTargetView.showFor(DashboardActivity.this,                 // `this` is an Activity
                                    TapTarget.forView(fabSpeedDialMenu.getMiniFab(0), "Press this button to add a new course!")
                                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                                            .outerCircleColor(R.color.md_deep_purple_700)
                                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                            .transparentTarget(true),           // Specify whether the target is transparent (displays the content underneath)
                                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                                        @Override
                                        public void onTargetClick(TapTargetView view) {
                                            super.onTargetClick(view);      // This call is optional
                                            startActivity(new Intent(DashboardActivity.this, AddCourseActivity.class).putExtra("string", "intro"));
                                            fabSpeedDialMenu.closeMenu();
                                        }
                                    });
                        }
                    });
        }

        getSharedPreferences(PREFS_NAME, 0).edit().putBoolean("firstrun", true).apply();


//        new TapTargetSequence(this)
//                .targets(target0, target1)
//                .listener(new TapTargetSequence.Listener() {
//                    // This listener will tell us when interesting(tm) events happen in regards
//                    // to the sequence
//                    @Override
//                    public void onSequenceFinish() {
//                        // Yay
//                    }
//
//                    @Override
//                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//                        Timber.d("onSequenceStep");
//                        if (lastTarget == target0) {
//
////                            fabSpeedDialMenu.openMenu();
//                        }
//                        if (lastTarget == target1) {
//                            fabSpeedDialMenu.closeMenu();
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    startActivity(new Intent(DashboardActivity.this, AddCourseActivity.class).putExtra("string", "intro"));
//                                }
//                            }, 500);
//                            fabSpeedDialMenu.closeMenu();
//                        }
//
//                    }
//
//
//                    @Override
//                    public void onSequenceCanceled(TapTarget lastTarget) {
//                        // Boo
//                    }
//                }).start();


//        new MaterialIntroView.Builder(this)
//                .enableDotAnimation(false)
//                .enableIcon(false)
//                .setFocusGravity(FocusGravity.CENTER)
//                .setFocusType(Focus.MINIMUM)
//                .setDelayMillis(500)
//                .enableFadeAnimation(true)
//                .performClick(true)
//                .setInfoText("Welcome to Sensei!\nLet's start by adding a course!")
////                .setShapeType(ShapeType.CIRCLE)
//                .setTarget(fabSpeedDialMenu.getMainFab())
//                .setUsageId("fab") //THIS SHOULD BE UNIQUE ID
//                .setListener(new MaterialIntroListener() {
//                    @Override
//                    public void onUserClicked(String materialIntroViewId) {
//                        new MaterialIntroView.Builder(DashboardActivity.this)
//                                .enableDotAnimation(false)
//                                .enableIcon(false)
//                                .setFocusGravity(FocusGravity.CENTER)
//                                .setFocusType(Focus.MINIMUM)
//                                .setDelayMillis(0)
//                                .enableFadeAnimation(true)
//                                .performClick(true)
//                                .setInfoText("Press this button to add a new course!")
////                .setShapeType(ShapeType.CIRCLE)
//                                .setTarget(fabSpeedDialMenu.getMiniFab(0))
//                                .setUsageId("fab_course") //THIS SHOULD BE UNIQUE ID
////                                .setListener(new MaterialIntroListener() {
////                                    @Override
////                                    public void onUserClicked(String materialIntroViewId) {
////                                        startActivity(new Intent(DashboardActivity.this, AddCourseActivity.class));
////
////                                    }
////                                })
//                                .show();
//                    }
//                })
//                .show();

//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500); // half second between each showcase view

//        MaterialShowcaseView.resetSingleUse(this, SHOWCASE_ID);
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
//
//        sequence.setConfig(config);
//
//        sequence.addSequenceItem(fabSpeedDialMenu.getMainFab(),
//                "Welcome to Sensei!\nLet's start by adding a new course!", "GOT IT");
//
//        sequence.addSequenceItem(fabSpeedDialMenu.getMiniFab(0),
//                "Click this button to add a new course!", "GOT IT");
//
//        sequence.start();


//        new MaterialShowcaseView.Builder(this)
//                .setTarget(fabSpeedDialMenu.getMainFab())
////                .setDismissText("GOT IT")
//                .setContentText()
////                .setDelay(withDelay) // optional but starting animations immediately in onCreate can make them choppy
////                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
//                .show();


    }

    void setNotification() {
        Intent notificationIntent = new Intent(DashboardActivity.this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(DashboardActivity.this, 0, notificationIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        DateTime dateTime = new DateTime().withHourOfDay(18).withMinuteOfHour(0);
        DateTime now = DateTime.now();
//        if (now.isAfter(dateTime))
//            dateTime = dateTime.plusDays(1);

        Timber.d(dateTime.toString());
        manager.setRepeating(AlarmManager.RTC_WAKEUP, dateTime.toCalendar(Locale.getDefault()).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
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
        setNotification();


    }


    public void onStop() {
        super.onStop();

    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true); //select home by default in navigation drawer
    }

    public void onBackPressed() {
//        if (floatingActionMenu.isOpened())
//            floatingActionMenu.close(true);
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (viewPager.getCurrentItem() == 1)
            viewPager.setCurrentItem(0);


        else
            DashboardActivity.this.moveTaskToBack(true);
    }


    @Override
    public void onClick(View view) {
        final Handler handler = new Handler();

        switch (view.getId()) {

            case R.id.fab_course:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DashboardActivity.this, AddCourseActivity.class));
                    }
                }, 300);
                break;
            case R.id.fab_quiz:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DashboardActivity.this, AddQuizActivity.class));
                    }
                }, 300);
                break;
            case R.id.fab_assignment:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DashboardActivity.this, AddAssignmentActivity.class));
                    }
                }, 300);
                break;
            case R.id.fab_homework:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DashboardActivity.this, AddHomeworkActivity.class));
                    }
                }, 300);
                break;
        }
//        floatingActionMenu.close(true);
    }

    public static class Adapter extends FragmentPagerAdapter {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.timetable_refresh, menu);
        return true;
    }


}
