package com.sensei.DataHandlers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sensei.Activities.Courses.CoursesListActivity;
import com.sensei.Activities.Dashboard.DashboardClassesFragment;
import com.sensei.DataModelClasses.AssignmentDataModel;
import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.DataModelClasses.QuizDataModel;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import timber.log.Timber;

import static com.sensei.Application.MyApplication.UID;
import static com.sensei.Application.MyApplication.database;
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
    public Map<String, ClassDataModel> ClassesID = new HashMap<>();
    public Map<String, QuizDataModel> QuizzesID = new HashMap<>();
    public Map<String, AssignmentDataModel> AssignmentsID = new HashMap<>();

    public Map<String, CourseDataModel> CoursesID = new HashMap<>();

    private CoursesListActivity coursesActivityInstance = null;
    private DashboardClassesFragment dashboardClassesFragment = null;

    public void registerCoursesActivity(CoursesListActivity coursesActivity) {
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


    List<String> tempList = new ArrayList<>();

    public void addChildListener() {
        final DatabaseReference coursesRef = databaseReference.child("courses").child(UID);
        coursesRef.keepSynced(true);
        CoursesList.clear();

        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot courseDataSnapshot, String s) {
                Object o = courseDataSnapshot.getValue();
                Timber.d("courseDataSnapshot JSON" + new JSONObject((Map) o));
                Timber.d("onChildAdded coursesRef" + courseDataSnapshot.toString());


                CourseDataModel courseDataModel = new CourseDataModel();
                courseDataModel.setCourseAbbreviation(courseDataSnapshot.child("courseAbbreviation").getValue().toString());
                courseDataModel.setCourseName(courseDataSnapshot.child("courseName").getValue().toString());
                courseDataModel.setCourseColorCode(((Long) courseDataSnapshot.child("courseColorCode").getValue()).intValue());

                DataSnapshot classes = courseDataSnapshot.child("classes");
                for (DataSnapshot classObj : classes.getChildren()) {
                    ClassDataModel classDataModel = classObj.getValue(ClassDataModel.class);
                    String classID = classObj.getKey();
                    courseDataModel.getClasses().add(classDataModel);
                    ClassesID.put(classID, classDataModel);
                }


                DataSnapshot quizzes = courseDataSnapshot.child("quizzes");
                for (DataSnapshot quizObj : quizzes.getChildren()) {
                    QuizDataModel quizDataModel = quizObj.getValue(QuizDataModel.class);
                    String quizID = quizObj.getKey();
                    courseDataModel.getQuizzes().add(quizDataModel);
                    QuizzesID.put(quizID, quizDataModel);
                }


                DataSnapshot assignments = courseDataSnapshot.child("assignments");
                for (DataSnapshot assgObj : assignments.getChildren()) {
                    AssignmentDataModel assignmentDataModel = assgObj.getValue(AssignmentDataModel.class);
                    String assgID = assgObj.getKey();
                    courseDataModel.getAssignments().add(assignmentDataModel);
                    AssignmentsID.put(assgID, assignmentDataModel);
                }

                CoursesList.add(courseDataModel);
                CoursesID.put(courseDataSnapshot.getKey(), courseDataModel);
//                updateClassesList();

//                if (coursesActivityInstance != null)
//                    coursesActivityInstance.adapter.notifyDataSetChanged();

//                final DatabaseReference classesRef = databaseReference.child("courses").child(UID).child(courseDataSnapshot.getKey()).child("classes");
//                classesRef.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot classDataSnapshot, String s) {
//                        if (dashboardClassesFragment != null) {
//                            dashboardClassesFragment.dashboardClassesAdapter.notifyDataSetChanged();
//                        }
//
//                        Timber.d("onChildAdded classesRef" + classDataSnapshot.toString());
//
//                        ClassDataModel classDataModel = classDataSnapshot.getValue(ClassDataModel.class);
//                        int index = tempList.indexOf(classesRef.getParent().getKey());
//                        CoursesList.get(index).getClasses().add(classDataModel); //add this class to list of classes for that course in courses list
//                        CoursesID.get(courseDataSnapshot.getKey()).getClasses().add(classDataModel); //add this class to list of classes for that course in coursesID Hashmap
//
//                        ClassesID.put(classDataSnapshot.getKey(), classDataModel); //add this class to classesID Hashmap
//
//                        updateClassesList();
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        Timber.d("ChildChanged classesRef" + dataSnapshot.toString());
//
////                        ClassDataModel newClass = dataSnapshot.getValue(ClassDataModel.class);
////                        String CourseKey = dataSnapshot.getRef().getParent().getParent().getKey();
////                        CourseDataModel courseDataModel = CoursesID.get(CourseKey);
////                        String ClassKey = dataSnapshot.getKey();
////                        ClassDataModel oldClass = ClassesID.get(ClassKey);
//
////                        int indexOfOldClass = courseDataModel.getClasses().indexOf(oldClass);
////                        courseDataModel.getClasses().set(indexOfOldClass, newClass);
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//                        Timber.d("ChildRemoved classesRef" + dataSnapshot.toString());
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                        Timber.d("onChildMoved classesRef" + dataSnapshot.toString());
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Timber.d("onCancelled classesRef" + databaseError.toString());
//
//                    }
//                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Timber.d("onChildChanged courses" + dataSnapshot.toString());
                Object o = dataSnapshot.getValue();
                Timber.d("dataSnapshot JSON" + new JSONObject((Map) o));
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
        };

        coursesRef.addChildEventListener(childEventListener);
        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coursesRef.removeEventListener(childEventListener);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    public void addValueEventListener() {
//
//        coursesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot listOfCIDs) {
//                Log.d("onDataChange coursesRef", listOfCIDs.toString());
//
//                for (DataSnapshot courseID : listOfCIDs.getChildren()) {
//                    CoursesList.add(courseID.getValue(CourseDataModel.class));
//                    if (coursesActivityInstance != null)
//                        coursesActivityInstance.coursesListAdapter.notifyDataSetChanged();
//
//
//                    DatabaseReference classesRef = databaseReference.child("courses").child(UID).child(courseID.getKey()).child("classes");
//                    classesRef.addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            Log.d("onChildAdded classesRef", dataSnapshot.toString());
//
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                            Log.d("ChildChanged classesRef", dataSnapshot.toString());
//
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//                            Log.d("ChildRemoved classesRef", dataSnapshot.toString());
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                            Log.d("onChildMoved classesRef", dataSnapshot.toString());
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            Log.d("onCancelled classesRef", databaseError.toString());
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("Error coursesRef", databaseError.toString());
//
//
//            }
//        });
//    }

    public List<ClassDataModel> getAllClasses() {
        List<ClassDataModel> classesList = new ArrayList<>();
        for (CourseDataModel course : CoursesList) {
            for (ClassDataModel classDataModel : course.getClasses()) {
//                if (!course.getClasses().contains(classDataModel))
                classesList.add(classDataModel);
            }
        }
        return classesList;
    }

//    public void updateClassesList() {
//        ClassesList.clear();
//        for (CourseDataModel course : CoursesList) {
//            for (ClassDataModel classDataModel : course.getClasses()) {
////                if (!course.getClasses().contains(classDataModel))
//                ClassesList.add(classDataModel);
//            }
//        }
//    }

    public List<ClassDataModel> getListOfClassesForCurrentDay() {
        DateTime dateTime = new DateTime();
        List<ClassDataModel> tempList = new ArrayList<>();
        for (ClassDataModel classDataModel : getAllClasses()) {
            if (classDataModel.getDayOfWeek() == dateTime.getDayOfWeek() && !new LocalTime().isAfter(classDataModel.getEndTimeOriginal()))
                tempList.add(classDataModel);
        }
        Collections.sort(tempList, new classTimeComparator());

        return tempList;
    }

    public List<ClassDataModel> getListOfClassesForDayOfWeek(int dayOfWeek) {
        List<ClassDataModel> tempList = new ArrayList<>();
        for (ClassDataModel classDataModel : getAllClasses()) {
            if (classDataModel.getDayOfWeek() == dayOfWeek)
                tempList.add(classDataModel);
        }
        Collections.sort(tempList, new classTimeComparator());

        return tempList;
    }

    public List<ClassDataModel> getListOfClassesForCourse(CourseDataModel courseDataModel) {
        List<ClassDataModel> tempList = new ArrayList<>();
        tempList.addAll(courseDataModel.getClasses());
        Collections.sort(tempList, new classDayComparator());
        return tempList;
    }

    public List<QuizDataModel> getListOfQuizzes() {
        List<QuizDataModel> tempList = new ArrayList<>();
        for (CourseDataModel courseDataModel : CoursesList)
            tempList.addAll(courseDataModel.getQuizzes());
        return tempList;
    }

    public List<QuizDataModel> getListOfIncompleteQuizzes() {
        List<QuizDataModel> tempList = new ArrayList<>();
        for (CourseDataModel courseDataModel : CoursesList)
            tempList.addAll(courseDataModel.getIncompleteQuizzes());
        return tempList;
    }

    public List<AssignmentDataModel> getListOfAssignments() {
        List<AssignmentDataModel> tempList = new ArrayList<>();
        for (CourseDataModel courseDataModel : CoursesList)
            tempList.addAll(courseDataModel.getAssignments());
        return tempList;
    }


    public static class classTimeComparator implements Comparator<ClassDataModel> {
        public int compare(ClassDataModel left, ClassDataModel right) {
            return left.getStartTimeOriginal().compareTo(right.getStartTimeOriginal());
        }
    }

    public static class classDayComparator implements Comparator<ClassDataModel> {
        public int compare(ClassDataModel left, ClassDataModel right) {
            return left.getDayOfWeek() - right.getDayOfWeek();
        }
    }

    public String getCourseID(CourseDataModel value) {
        for (Map.Entry<String, CourseDataModel> entry : CoursesID.entrySet()) {

//            if (Objects.equals(value, entry.getValue())) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getClassID(ClassDataModel value) {
        for (Map.Entry<String, ClassDataModel> entry : ClassesID.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getQuizID(QuizDataModel value) {
        for (Map.Entry<String, QuizDataModel> entry : QuizzesID.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public CourseDataModel getCourseOfClass(ClassDataModel classDataModel) {
        for (CourseDataModel courseDataModel : CoursesList) {
            if (courseDataModel.getClasses().contains(classDataModel)) {
                return courseDataModel;
            }
        }
        return null;
    }

    public CourseDataModel getCourse(QuizDataModel quizDataModel) {
        for (CourseDataModel courseDataModel : CoursesList) {
            if (courseDataModel.getQuizzes().contains(quizDataModel)) {
                return courseDataModel;
            }
        }
        return null;
    }

    public CourseDataModel getCourse(AssignmentDataModel assignmentDataModel) {
        for (CourseDataModel courseDataModel : CoursesList) {
            if (courseDataModel.getAssignments().contains(assignmentDataModel)) {
                return courseDataModel;
            }
        }
        return null;
    }

    public synchronized void addClassToCourse(String courseID, ClassDataModel classDataModel) {
        String ClassID = databaseReference.child("courses").child(UID).child(courseID).child("classes").push().getKey();
        databaseReference.child("courses").child(UID).child(courseID).child("classes").child(ClassID).setValue(classDataModel);


        CoursesID.get(courseID).getClasses().add(classDataModel);
        ClassesID.put(ClassID, classDataModel);
    }

    public void updateClass(String courseID, String classID, ClassDataModel classDataModel) {
        DatabaseReference reference = databaseReference.child("courses").child(UID).child(courseID).child("classes").child(classID);
        reference.setValue(classDataModel);

    }

    public void deleteClass(String courseID, String classID) {
        DatabaseReference reference = databaseReference.child("courses").child(UID).child(courseID).child("classes").child(classID);
        reference.removeValue();
        CoursesID.get(courseID).getClasses().remove(ClassesID.get(classID));
        ClassesID.remove(classID);

    }

    public void addCourse(CourseDataModel courseDataModel, List<ClassDataModel> classesList) {
        String courseID = databaseReference.child("courses").push().getKey();
        databaseReference.child("courses")
                .child(UID)
                .child(courseID)
                .setValue(courseDataModel);

//        courseDataModel.getClasses().addAll(classesList);
        CoursesList.add(courseDataModel);
        CoursesID.put(courseID, courseDataModel);


        for (ClassDataModel classDataModel : classesList) {


            addClassToCourse(courseID, classDataModel);

        }

    }

    public void updateTaskCompleted(QuizDataModel quizDataModel, boolean isCompleted) {
        getCourse(quizDataModel)
                .getQuizzes()
                .get(getCourse(quizDataModel)
                        .getQuizzes()
                        .indexOf(quizDataModel))
                .setCompleted(isCompleted);

        String quizID = getQuizID(quizDataModel);
        databaseReference.child("courses")
                .child(UID)
                .child(getCourseID(getCourse(quizDataModel)))
                .child("quizzes")
                .child(quizID)
                .child("completed")
                .setValue(isCompleted);
    }

    public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }


}
