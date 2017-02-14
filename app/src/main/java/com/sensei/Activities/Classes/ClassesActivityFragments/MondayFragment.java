package com.sensei.Activities.Classes.ClassesActivityFragments;

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
import com.sensei.Activities.Classes.ClassDetailsActivity;
import com.sensei.Adapters.DayOfWeekClassesAdapter;
import com.sensei.Adapters.DayOfWeekClassesAdapterBRVAH;
import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.R;

import timber.log.Timber;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static org.joda.time.DateTimeConstants.MONDAY;

/**
 * Created by Asad on 07-Jan-17.
 */

public class MondayFragment extends Fragment {
    RecyclerView recyclerView;
    public DayOfWeekClassesAdapter dayOfWeekClassesAdapter;
    DayOfWeekClassesAdapterBRVAH adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView");


        return inflater.inflate(R.layout.fragment_monday, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Timber.d("onViewCreated");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        dayOfWeekClassesAdapter = new DayOfWeekClassesAdapter(getContext(), getCourseDataInstance().getListOfClassesForDayOfWeek(MONDAY));


//        bus.register(MondayFragment.this);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {


            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                ClassDataModel classDataModel = getCourseDataInstance().getListOfClassesForDayOfWeek(MONDAY).get(i);
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

//    @Subscribe
//    public void getDeletedClass(EditClassDetailsActivity.ClassDeletedEvent event) {
//        Timber.d("getDeletedEvent Called");
//        if (getCourseDataInstance().getListOfClassesForDayOfWeek(MONDAY).contains(event.getClassDataModel())) {
//            adapter.remove(getCourseDataInstance().getListOfClassesForDayOfWeek(MONDAY).indexOf(event.getClassDataModel()));
//        }
//    }

    public void onResume() {
        super.onResume();
        Timber.d("onResume");
//        adapter.notifyDataSetChanged();
    }

    public void onStart() {

        super.onStart();
        Timber.d("onStart");
        adapter = new DayOfWeekClassesAdapterBRVAH(R.layout.class_layout, getCourseDataInstance().getListOfClassesForDayOfWeek(MONDAY));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//        adapter.setNewData(getCourseDataInstance().getListOfClassesForDayOfWeek(MONDAY));

    }

    public void onStop() {

        super.onStop();
        Timber.d("onStop");

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        // Check which request we're responding to
//        if (requestCode == REQUEST_CODE_EDIT_CLASS) {
//            if (resultCode == RESULT_OK) {
//            }
//
//            if (resultCode == Activity.RESULT_CANCELED) {
//                adapter.notifyDataSetChanged();
//            }
//        }
//    }


}