package com.sensei.Activities;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sensei.DataHandlers.ClassDataHandler;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.Fragments.ClassesActivityFragments.FridayFragment;
import com.sensei.Fragments.ClassesActivityFragments.MondayFragment;
import com.sensei.Fragments.ClassesActivityFragments.SaturdayFragment;
import com.sensei.Fragments.ClassesActivityFragments.ThursdayFragment;
import com.sensei.Fragments.ClassesActivityFragments.TuesdayFragment;
import com.sensei.Fragments.ClassesActivityFragments.WednesdayFragment;
import com.sensei.Fragments.DashboardClassesFragment;
import com.sensei.Fragments.DashboardTasksFragment;
import com.sensei.R;
import com.sensei.Utils.NavigationDrawerSetup;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import static com.sensei.Application.MyApplication.UID;
import static com.sensei.Application.MyApplication.databaseReference;

public class ClassesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Classes");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(new LocalDate().getDayOfWeek());

    }

    private void setupViewPager(ViewPager viewPager) {
        DashboardActivity.Adapter adapter = new DashboardActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(new MondayFragment(), "Mon");
        adapter.addFragment(new TuesdayFragment(), "Tue");
        adapter.addFragment(new WednesdayFragment(), "Wed");
        adapter.addFragment(new ThursdayFragment(), "Thu");
        adapter.addFragment(new FridayFragment(), "Fri");
        adapter.addFragment(new SaturdayFragment(), "Sat");
        viewPager.setAdapter(adapter);

    }

    public void onStart() {
        super.onStart();
        navigationDrawerSetup.ConfigureDrawer();


    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true); //select home by default in navigation drawer
    }

    public void onStop() {
        super.onStop();
    }

}
