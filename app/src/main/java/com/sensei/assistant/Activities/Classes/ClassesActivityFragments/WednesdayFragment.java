package com.sensei.assistant.Activities.Classes.ClassesActivityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.sensei.assistant.Activities.Classes.ClassDetailsActivity;
import com.sensei.assistant.Adapters.DayOfWeekClassesAdapter;
import com.sensei.assistant.Adapters.DayOfWeekClassesAdapterBRVAH;
import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.R;

import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static org.joda.time.DateTimeConstants.WEDNESDAY;


/**
 * Created by Asad on 07-Jan-17.
 */

public class WednesdayFragment extends Fragment {

    RecyclerView recyclerView;
    public DayOfWeekClassesAdapter dayOfWeekClassesAdapter;
    DayOfWeekClassesAdapterBRVAH adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_monday, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        dayOfWeekClassesAdapter = new DayOfWeekClassesAdapter(getContext(), getCourseDataInstance().getListOfClassesForDayOfWeek(WEDNESDAY));

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {


            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                ClassDataModel classDataModel = getCourseDataInstance().getListOfClassesForDayOfWeek(WEDNESDAY).get(i);
//                Toast.makeText(getContext(), "" + classDataModel.getLocation(), Toast.LENGTH_SHORT).show();
                final Intent intent = new Intent(getActivity(), ClassDetailsActivity.class);
                String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourseOfClass(classDataModel));
                String classID = getCourseDataInstance().getClassID(classDataModel);
                intent.putExtra("courseID", courseID);
                intent.putExtra("classID", classID);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(intent);

                    }
                }, 200);

            }

        });
    }

    public void onStart() {
        super.onStart();
        adapter = new DayOfWeekClassesAdapterBRVAH(R.layout.class_layout, getCourseDataInstance().getListOfClassesForDayOfWeek(WEDNESDAY));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onStop() {
        super.onStop();

    }
}