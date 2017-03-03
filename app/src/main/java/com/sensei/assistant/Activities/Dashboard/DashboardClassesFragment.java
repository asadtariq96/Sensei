package com.sensei.assistant.Activities.Dashboard;

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
import com.sensei.assistant.Adapters.DashboardClassesAdapter;
import com.sensei.assistant.Adapters.DashboardClassesAdapterBRVAH;
import com.sensei.assistant.DataHandlers.CourseDataHandler;
import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.R;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

import static com.sensei.assistant.Application.MyApplication.bus;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static com.sensei.assistant.R.id.weekView;


/**
 * Created by Asad on 04-Jan-17.
 */

public class DashboardClassesFragment extends Fragment {

    RecyclerView recyclerView;
    public DashboardClassesAdapter dashboardClassesAdapter;
    View placeHolder;
    public DashboardClassesAdapterBRVAH adapter;
    String semKey;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
        bus.register(DashboardClassesFragment.this);
//        adapter.notifyDataSetChanged();
//        if (getCourseDataInstance().getListOfClassesForCurrentDay().isEmpty())
//            placeHolder.setVisibility(View.VISIBLE);
//        else
//            placeHolder.setVisibility(View.GONE);

    }

    public void onStop() {
        super.onStop();
        getCourseDataInstance().unregisterDashboardClassesFragment();
        bus.unregister(DashboardClassesFragment.this);

    }

    @Subscribe
    public void answerAvailable(CourseDataHandler.DataChangedEvent event) {
        adapter.setNewData(getCourseDataInstance().getListOfClassesForCurrentDay());
        Timber.d("event received");
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.timetable_refresh, menu);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        // handle arrow click here
//        if (item.getItemId() == R.id.refresh) {
//            Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
//            adapter.setNewData(getCourseDataInstance().getListOfClassesForCurrentDay());
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }


}
