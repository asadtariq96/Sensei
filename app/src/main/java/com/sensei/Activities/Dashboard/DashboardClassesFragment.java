package com.sensei.Activities.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.sensei.Activities.Classes.ClassDetailsActivity;
import com.sensei.Adapters.DashboardClassesAdapter;
import com.sensei.Adapters.DashboardClassesAdapterBRVAH;
import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.R;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static com.sensei.R.id.placeholder;


/**
 * Created by Asad on 04-Jan-17.
 */

public class DashboardClassesFragment extends Fragment {

    RecyclerView recyclerView;
    public DashboardClassesAdapter dashboardClassesAdapter;
    View placeHolder;
    public DashboardClassesAdapterBRVAH adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        placeHolder = inflater.inflate(R.layout.no_class_placeholder, container, false);

        return inflater.inflate(R.layout.fragment_dashboard_classes_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
//        placeHolder = (TextView) view.findViewById(R.id.placeholder);
//        dashboardClassesAdapter = new DashboardClassesAdapter(getContext());
        adapter = new DashboardClassesAdapterBRVAH(R.layout.class_layout, getCourseDataInstance().getListOfClassesForCurrentDay());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(placeHolder);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ClassDataModel classDataModel = getCourseDataInstance().getListOfClassesForCurrentDay().get(i);

                final Intent intent = new Intent(getActivity(), ClassDetailsActivity.class);
                String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourseOfClass(classDataModel));
                String classID = getCourseDataInstance().getClassID(classDataModel);
                intent.putExtra("courseID", courseID);
                intent.putExtra("classID", classID);
//                Parcelable classObject = Parcels.wrap(classDataModel);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("classObject", classObject);
//                Parcelable courseObject = Parcels.wrap(getCourseDataInstance().getCourseOfClass(classDataModel));
//                bundle.putParcelable("courseObject", courseObject);
//                intent.putExtras(bundle);


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        adapter.setEmptyView(null);
    }

    public void onStart() {
        super.onStart();
        getCourseDataInstance().registerDashboardClassesFragment(DashboardClassesFragment.this);
        adapter.setNewData(getCourseDataInstance().getListOfClassesForCurrentDay());
        adapter.notifyDataSetChanged();
//        if (getCourseDataInstance().getListOfClassesForCurrentDay().isEmpty())
//            placeHolder.setVisibility(View.VISIBLE);
//        else
//            placeHolder.setVisibility(View.GONE);

    }

    public void onStop() {
        super.onStop();
        getCourseDataInstance().unregisterDashboardClassesFragment();

    }
}
