package com.sensei.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sensei.R;


/**
 * Created by Asad on 17-Dec-16.
 */
public class DashboardTasksFragment extends Fragment {

    RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_tasks_layout, null);

//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);  // or however you need to do it for your code
//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
//        params.setScrollFlags(0);  // clear all scroll flags

//        ((MainActivity) getActivity() ).getFAB().hide();


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
}
