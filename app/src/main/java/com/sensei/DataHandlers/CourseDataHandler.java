package com.sensei.DataHandlers;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sensei.Activities.CoursesActivity;
import com.sensei.Activities.DashboardActivity;
import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.Fragments.DashboardClassesFragment;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.sensei.Application.MyApplication.UID;
import static com.sensei.Application.MyApplication.databaseReference;

/**
 * Created by Asad on 08-Jan-17.
 */

public class CourseDataHandler {

    private static CourseDataHandler instance = null;

    private CourseDataHandler() {
    }

    public static CourseDataHandler getCourseDataInstance() {
        if (instance == null) {
            instance = new CourseDataHandler();
        }
        return instance;
    }


    public List<CourseDataModel> CoursesList = new ArrayList<>();
    public List<ClassDataModel> ClassesList = new ArrayList<>();


    private CoursesActivity coursesActivityInstance = null;
    private DashboardClassesFragment dashboardClassesFragment = null;

    public void registerCoursesActivity(CoursesActivity coursesActivity) {
        this.coursesActivityInstance = coursesActivity;
    }

    public void registerDashboardClassesFragment(DashboardClassesFragment fragment) {
        dashboardClassesFragment = fragment;
    }

    public void unregisterCoursesActivity() {
        coursesActivityInstance = null;
    }

    public void unregisterDashboardClassesFragment() {
        dashboardClassesFragment = null;
    }


    DatabaseReference coursesRef = databaseReference.child("courses").child(UID);
    List<String> tempList = new ArrayList<>();

    public void addChildListener() {
        coursesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("onChildAdded coursesRef", dataSnapshot.toString());

                CoursesList.add(dataSnapshot.getValue(CourseDataModel.class));
                tempList.add(dataSnapshot.getKey());
                if (coursesActivityInstance != null)
                    coursesActivityInstance.coursesListAdapter.notifyDataSetChanged();

                final DatabaseReference classesRef = databaseReference.child("courses").child(UID).child(dataSnapshot.getKey()).child("classes");
                classesRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        updateClassesList();
                        if (dashboardClassesFragment != null)
                            dashboardClassesFragment.dashboardClassesAdapter.notifyDataSetChanged();
                        Log.d("onChildAdded classesRef", dataSnapshot.toString());
                        ClassDataModel classDataModel = dataSnapshot.getValue(ClassDataModel.class);
                        int index = tempList.indexOf(classesRef.getParent().getKey());
                        CoursesList.get(index).getClassesList().add(classDataModel);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("ChildChanged classesRef", dataSnapshot.toString());

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("ChildRemoved classesRef", dataSnapshot.toString());

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d("onChildMoved classesRef", dataSnapshot.toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("onCancelled classesRef", databaseError.toString());

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addValueEventListener() {

        coursesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot listOfCIDs) {
                Log.d("onDataChange coursesRef", listOfCIDs.toString());

                for (DataSnapshot courseID : listOfCIDs.getChildren()) {
                    CoursesList.add(courseID.getValue(CourseDataModel.class));
                    if (coursesActivityInstance != null)
                        coursesActivityInstance.coursesListAdapter.notifyDataSetChanged();


                    DatabaseReference classesRef = databaseReference.child("courses").child(UID).child(courseID.getKey()).child("classes");
                    classesRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d("onChildAdded classesRef", dataSnapshot.toString());

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Log.d("ChildChanged classesRef", dataSnapshot.toString());

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Log.d("ChildRemoved classesRef", dataSnapshot.toString());

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            Log.d("onChildMoved classesRef", dataSnapshot.toString());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("onCancelled classesRef", databaseError.toString());

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error coursesRef", databaseError.toString());


            }
        });
    }

    public void updateClassesList() {
        ClassesList.clear();
        for (CourseDataModel course : CoursesList) {
            for (ClassDataModel classDataModel : course.getClassesList()) {
                ClassesList.add(classDataModel);
            }
        }
    }

    public List<ClassDataModel> getListOfClassesForCurrentDay() {
        DateTime dateTime = new DateTime();
        List<ClassDataModel> tempList = new ArrayList<>();
        for (ClassDataModel classDataModel : ClassesList) {
            if (classDataModel.getDayOfWeek() == dateTime.getDayOfWeek() && !new LocalTime().isAfter(classDataModel.getEndTimeOriginal()))
                tempList.add(classDataModel);
        }
        Collections.sort(tempList, new classTimeComparator());

        return tempList;
    }

    public List<ClassDataModel> getListOfClassesForDayOfWeek(int dayOfWeek) {
        List<ClassDataModel> tempList = new ArrayList<>();
        for (ClassDataModel classDataModel : ClassesList) {
            if (classDataModel.getDayOfWeek() == dayOfWeek)
                tempList.add(classDataModel);
        }
        Collections.sort(tempList, new classTimeComparator());

        return tempList;
    }


    public class classTimeComparator implements Comparator<ClassDataModel> {
        public int compare(ClassDataModel left, ClassDataModel right) {
            return left.getStartTimeOriginal().compareTo(right.getStartTimeOriginal());
        }
    }

}
