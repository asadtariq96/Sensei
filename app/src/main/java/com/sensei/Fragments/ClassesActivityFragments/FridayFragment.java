package com.sensei.Fragments.ClassesActivityFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sensei.Adapters.DayOfWeekClassesAdapter;
import com.sensei.R;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;


/**
 * Created by Asad on 07-Jan-17.
 */

public class FridayFragment extends Fragment {

    RecyclerView recyclerView;
    public DayOfWeekClassesAdapter dayOfWeekClassesAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_monday, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        dayOfWeekClassesAdapter = new DayOfWeekClassesAdapter(getContext(), getCourseDataInstance().getListOfClassesForDayOfWeek(5));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dayOfWeekClassesAdapter);
    }

    public void onStart() {
        super.onStart();
        dayOfWeekClassesAdapter.notifyDataSetChanged();

    }

    public void onStop() {
        super.onStop();

    }
}