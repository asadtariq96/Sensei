package com.sensei.assistant.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collection;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.UserInfo;
import com.sensei.assistant.Activities.Assignments.AssignmentsListActivity;
import com.sensei.assistant.Activities.Classes.ClassesActivity;
import com.sensei.assistant.Activities.Courses.CoursesListActivity;
import com.sensei.assistant.Activities.Dashboard.DashboardActivity;
import com.sensei.assistant.Activities.GPA.GPACalculatorActivity;
import com.sensei.assistant.Activities.Homework.HomeworkListActivity;
import com.sensei.assistant.Activities.Quizzes.QuizzesListActivity;
import com.sensei.assistant.Activities.Settings.SettingsActivity;
import com.sensei.assistant.Activities.TimeTable.TimetableActivity;
import com.sensei.assistant.Authentication.SignInActivity;
import com.sensei.assistant.R;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;
import static com.sensei.assistant.Application.MyApplication.firebaseUser;
import static com.sensei.assistant.Application.MyApplication.mAuth;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by asad on 7/13/16.
 */

public class NavigationDrawerSetup extends AppCompatActivity {

    private final DrawerLayout drawerLayout;
    private final Toolbar toolbar;
    private final NavigationView navigationView;
    private final AppCompatActivity HostActivity;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    public NavigationDrawerSetup(DrawerLayout drawerLayout, Toolbar toolbar, NavigationView navigationView, AppCompatActivity activity) {
        this.drawerLayout = drawerLayout;
        this.toolbar = toolbar;
        this.navigationView = navigationView;
        this.HostActivity = activity;

    }

    public void ConfigureDrawer() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (!menuItem.isChecked()) {
                            menuItem.setChecked(true);
                            final Handler handler = new Handler();

                            switch (menuItem.getItemId()) {

                                case R.id.dashboard:
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(HostActivity, DashboardActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("NavDrawer", true);
                                            HostActivity.startActivity(intent);
                                        }
                                    }, 300);

                                    break;

                                case R.id.classes:
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(HostActivity, ClassesActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("NavDrawer", true);
                                            HostActivity.startActivity(intent);
                                        }
                                    }, 300);

                                    break;

                                case R.id.courses:
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(HostActivity, CoursesListActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("NavDrawer", true);
                                            HostActivity.startActivity(intent);
//                                            HostActivity.finish();
                                        }
                                    }, 300);

                                    break;

                                case R.id.timetable:
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(HostActivity, TimetableActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("NavDrawer", true);
                                            HostActivity.startActivity(intent);
//                                            HostActivity.finish();
                                        }
                                    }, 300);

                                    break;


                                case R.id.quizzes:
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(HostActivity, QuizzesListActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("NavDrawer", true);
                                            HostActivity.startActivity(intent);
//                                            HostActivity.finish();
                                        }
                                    }, 300);

                                    break;

                                case R.id.homework:
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(HostActivity, HomeworkListActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("NavDrawer", true);
                                            HostActivity.startActivity(intent);
//                                            HostActivity.finish();
                                        }
                                    }, 300);

                                    break;

                                case R.id.assignments:
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(HostActivity, AssignmentsListActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("NavDrawer", true);
                                            HostActivity.startActivity(intent);
//                                            HostActivity.finish();
                                        }
                                    }, 300);

                                    break;

                                case R.id.gpacalculator:
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(HostActivity, GPACalculatorActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("NavDrawer", true);
                                            HostActivity.startActivity(intent);
                                        }
                                    }, 300);

                                    break;

                                case R.id.settings:
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(HostActivity, SettingsActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("NavDrawer", true);
                                            HostActivity.startActivity(intent);
                                        }
                                    }, 300);

                                    break;

                                case R.id.signout:


                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            new MaterialDialog.Builder(HostActivity)
                                                    .content("Are you sure you want to sign out?")
                                                    .positiveText("SIGN OUT")
                                                    .negativeText("CANCEL")
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            signOut();
                                                        }
                                                    }).build().show();

                                        }
                                    }, 300);

                                    break;
//
//                                case R.id.drawer_shopping_list:
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Intent intent = new Intent(HostActivity, WishlistMainActivity.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                            intent.putExtra("NavDrawer", true);
//                                            HostActivity.startActivity(intent);
//                                        }
//                                    }, 300);
//                                    break;
//
//                                case R.id.drawer_profile:
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Intent intent = new Intent(HostActivity, UserProfileActivity.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                            intent.putExtra("NavDrawer", true);
//                                            HostActivity.startActivity(intent);
//                                        }
//                                    }, 300);
//                                    break;


//                                case R.id.drawer_messaging:
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            Intent intent = new Intent(HostActivity, NavMainActivity.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            intent.putExtra("destinationLatitude", 33.559309);
//                                            intent.putExtra("destinationLongitude", 73.092108);
//                                            HostActivity.startActivity(intent);
//                                            HostActivity.finish();
//                                        }
//                                    }, 300);
//
//                                    break;


                            }
                        }

//                        drawerLayout.closeDrawers();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }

        );

        View headerView = navigationView.getHeaderView(0);

        ImageView profile_pic = (ImageView) headerView.findViewById(R.id.nav_header_pic);
        //
//
        TextView nav_header_email = (TextView) headerView.findViewById(R.id.nav_header_email);
        if (firebaseUser != null)
            nav_header_email.setText(firebaseUser.getEmail());

        TextView nav_header_name = (TextView) headerView.findViewById(R.id.nav_header_name);
        nav_header_name.setVisibility(GONE);
        if (firebaseUser != null) {


            for (UserInfo profile : firebaseUser.getProviderData()) {
                String providerId = profile.getProviderId();

                if (providerId.equals("facebook.com")) {
                    String name = profile.getDisplayName();
                    nav_header_name.setVisibility(View.VISIBLE);
                    nav_header_name.setText(name);
                    String facebookUserId = profile.getUid();


//                Uri photoUrl = profile.getPhotoUrl();
                    String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

                    Picasso.with(HostActivity).load(photoUrl).placeholder(R.drawable.ic_profile).into(profile_pic);

                }
            }
        }


        actionBarDrawerToggle = new

                ActionBarDrawerToggle(HostActivity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }


                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                }

        ;
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    private void signOut() {
        Intent intent = new Intent(HostActivity, SignInActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.putExtra("NavDrawer", true);

//        HostActivity.finish();
        firebaseUser = null;
        getCourseDataInstance().clearData();
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        mAuth.signOut();

        HostActivity.finishAffinity();
        HostActivity.startActivity(intent);

    }


    public void ConfigureToolbar() {
        HostActivity.setSupportActionBar(toolbar);
        HostActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        HostActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(HostActivity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // mDrawerLayout.openDrawer(GravityCompat.START);
//                //return true;
//                super.onBackPressed();
//                return true;
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }


    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) HostActivity.getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private static <V extends View> Collection<V> findChildrenByClass(ViewGroup viewGroup, Class<V> clazz) {
        return gatherChildrenByClass(viewGroup, clazz, new ArrayList<V>());
    }

    private static <V extends View> Collection<V> gatherChildrenByClass(ViewGroup viewGroup, Class<V> clazz, Collection<V> childrenFound) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            final View child = viewGroup.getChildAt(i);
            if (clazz.isAssignableFrom(child.getClass())) {
                childrenFound.add((V) child);
            }
            if (child instanceof ViewGroup) {
                gatherChildrenByClass((ViewGroup) child, clazz, childrenFound);
            }
        }
        return childrenFound;
    }


}