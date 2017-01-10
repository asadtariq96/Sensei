package com.sensei.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sensei.Adapters.DashboardClassesAdapter;
import com.sensei.R;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;


/**
 * Created by Asad on 04-Jan-17.
 */

public class DashboardClassesFragment extends Fragment {

    RecyclerView recyclerView;
    public DashboardClassesAdapter dashboardClassesAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_dashboard_classes_layout, null);
    }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            // Setup any handles to view objects here
            // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            dashboardClassesAdapter = new DashboardClassesAdapter(getContext());
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(dashboardClassesAdapter);
        }

    public void onStart() {
        super.onStart();
        dashboardClassesAdapter.notifyDataSetChanged();
        getCourseDataInstance().registerDashboardClassesFragment(DashboardClassesFragment.this);

    }

    public void onStop() {
        super.onStop();
        getCourseDataInstance().unregisterDashboardClassesFragment();

    }
}
