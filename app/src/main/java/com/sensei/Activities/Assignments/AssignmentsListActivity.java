package com.sensei.Activities.Assignments;

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
import com.sensei.Adapters.DashboardAssignmentAdapter;
import com.sensei.Adapters.DashboardQuizAdapter;
import com.sensei.R;
import com.sensei.Utils.NavigationDrawerSetup;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class AssignmentsListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;
    private RecyclerView recyclerView;
    public DashboardAssignmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assignments");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new DashboardAssignmentAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfAssignments());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            }
        });
    }

    public void onStart() {
        super.onStart();
//        coursesRef.addValueEventListener(valueEventListener);
        navigationDrawerSetup.ConfigureDrawer();
        adapter.setNewData(getCourseDataInstance().getListOfAssignments());
        adapter.notifyDataSetChanged();
    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(5).setChecked(true); //select home by default in navigation drawer
    }
}
