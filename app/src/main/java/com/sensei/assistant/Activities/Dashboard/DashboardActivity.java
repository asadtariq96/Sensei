package com.sensei.assistant.Activities.Dashboard;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.sensei.assistant.Activities.Assignments.AddAssignmentActivity;
import com.sensei.assistant.Activities.Courses.AddCourseActivity;
import com.sensei.assistant.Activities.Homework.AddHomeworkActivity;
import com.sensei.assistant.Activities.Quizzes.AddQuizActivity;
import com.sensei.assistant.Activities.TimeTable.TimetableActivity;
import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.Notifications.NotificationReceiver;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.NavigationDrawerSetup;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.goncalves.pugnotification.notification.Load;
import br.com.goncalves.pugnotification.notification.PugNotification;
import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import timber.log.Timber;

import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

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

            createShortcutOfApp();

            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(fabSpeedDialMenu.getMainFab(), "Welcome to Sensei!", "Let's start by adding a new course!")
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)
                            // Whether tapping outside the outer circle dismisses the view
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


    }

//    private void scheduleFirstNotification(Notification notification, long delay) {
//        Intent notificationIntent = new Intent(this, PushNotifReceiver.FirstPushNotif.class);
//        //we need a unique identifier for each notification
//        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
//        //we need to set an action for each notification, and then define a class in the Manifest that uses this action
//        //as a filter
//        notificationIntent.setAction("first_notif_action");
//        notificationIntent.putExtra(PushNotifReceiver.NOTIFICATION_ID, uniqueInt);
//        notificationIntent.putExtra(PushNotifReceiver.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//    }

    private void createShortcutOfApp() {

        Intent shortcutIntent = new Intent(getApplicationContext(),
                TimetableActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Sensei Timetable");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.mipmap.ic_timetable));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so   don't duplicate
        getApplicationContext().sendBroadcast(addIntent);
    }

    void setNotification() {
        Intent notificationIntent = new Intent(DashboardActivity.this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(DashboardActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        DateTime dateTime = new DateTime().withHourOfDay(18).withMinuteOfHour(0);
        DateTime now = DateTime.now();
//        if (now.isAfter(dateTime))
//            dateTime = dateTime.plusDays(1);

        Timber.d(dateTime.toString());
        manager.setRepeating(AlarmManager.RTC_WAKEUP, dateTime.toCalendar(Locale.getDefault()).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    void setClassNotifications() {
        for (ClassDataModel classDataModel : getCourseDataInstance().getListOfClassesForWeek()) {

            int day = classDataModel.getDayOfWeek();
            LocalTime localTime = classDataModel.getStartTimeOriginal().minusMinutes(15);
            String course = getCourseDataInstance().getCourseOfClass(classDataModel).getCourseAbbreviation();
            Load mLoad = PugNotification.with(DashboardActivity.this).load()
                    .smallIcon(R.drawable.pugnotification_ic_launcher)
                    .largeIcon(R.drawable.pugnotification_ic_launcher)
                    .title(course + " class in 15 mins")

                    .flags(Notification.DEFAULT_ALL);
        }

    }


    void setNotificationForClass(Load load, long delay, String classID) {
        Intent notificationIntent = new Intent(DashboardActivity.this, NotificationReceiver.ClassNotificationReceiver.class);
        notificationIntent.putExtra("classID", classID);
        pendingIntent = PendingIntent.getBroadcast(DashboardActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);


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
//        setNotification();


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
