package com.sensei.assistant.Activities.Classes;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.sensei.assistant.Activities.Dashboard.DashboardActivity;
import com.sensei.assistant.Activities.Classes.ClassesActivityFragments.FridayFragment;
import com.sensei.assistant.Activities.Classes.ClassesActivityFragments.MondayFragment;
import com.sensei.assistant.Activities.Classes.ClassesActivityFragments.ThursdayFragment;
import com.sensei.assistant.Activities.Classes.ClassesActivityFragments.TuesdayFragment;
import com.sensei.assistant.Activities.Classes.ClassesActivityFragments.WednesdayFragment;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.NavigationDrawerSetup;

import org.joda.time.LocalDate;

import timber.log.Timber;

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

        Timber.d("setCurrentItem " + (new LocalDate().getDayOfWeek() - 1));

        viewPager.setCurrentItem(new LocalDate().getDayOfWeek() - 1);

    }

    private void setupViewPager(ViewPager viewPager) {
        Timber.d("setupViewPager");
        DashboardActivity.Adapter adapter = new DashboardActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(new MondayFragment(), "Mon");
        adapter.addFragment(new TuesdayFragment(), "Tue");
        adapter.addFragment(new WednesdayFragment(), "Wed");
        adapter.addFragment(new ThursdayFragment(), "Thu");
        adapter.addFragment(new FridayFragment(), "Fri");
//        adapter.addFragment(new SaturdayFragment(), "Sat");
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
