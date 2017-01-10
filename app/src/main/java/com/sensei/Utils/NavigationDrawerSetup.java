package com.sensei.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
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
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collection;

import com.sensei.Activities.ClassesActivity;
import com.sensei.Activities.CoursesActivity;
import com.sensei.Activities.DashboardActivity;
import com.sensei.Activities.TimetableActivity;
import com.sensei.R;

import static com.sensei.Application.MyApplication.firebaseUser;

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

                                            Intent intent = new Intent(HostActivity, CoursesActivity.class);
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
                });

        View headerView = navigationView.getHeaderView(0);

//        ImageView profile_pic = (ImageView) headerView.findViewById(R.id.nav_header_pic);
//        if (appSettings.getProfilePic() != null)
//            profile_pic.setImageBitmap(SignUpProfileActivity.decodeBase64(appSettings.getProfilePic()));
//
//        TextView nav_header_name = (TextView) headerView.findViewById(R.id.nav_header_name);
//        if (appSettings.getfName() != null && appSettings.getlName() != null)
//            nav_header_name.setText(appSettings.getfName() + " " + appSettings.getlName());
//        else if (appSettings.getfName() != null)
//            nav_header_name.setText(appSettings.getfName());
//        else
//            nav_header_name.setText("");
//
//
        TextView nav_header_email = (TextView) headerView.findViewById(R.id.nav_header_email);
        nav_header_email.setText(firebaseUser.getEmail());


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
