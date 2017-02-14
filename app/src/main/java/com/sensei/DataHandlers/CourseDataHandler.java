package com.sensei.DataHandlers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sensei.Activities.Courses.CoursesListActivity;
import com.sensei.Activities.Dashboard.DashboardClassesFragment;
import com.sensei.Application.Constants;
import com.sensei.DataModelClasses.AssignmentDataModel;
import com.sensei.DataModelClasses.ClassDataModel;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.DataModelClasses.QuizDataModel;
import com.sensei.DataModelClasses.SemesterDataModel;
import com.sensei.DataModelClasses.UserSettings;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

import static com.sensei.Application.Constants.SELECTED_SEMESTER;
import static com.sensei.Application.MyApplication.UID;
import static com.sensei.Application.MyApplication.coursesReference;
import static com.sensei.Application.MyApplication.databaseReference;
import static com.sensei.Application.MyApplication.semestersReference;
import static com.sensei.Application.MyApplication.settingsReference;

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


    public void clearData() {
        CoursesList.clear();
//        SemesterMap.clear();
        ClassesID.clear();
        QuizzesID.clear();
        AssignmentsID.clear();
        CoursesID.clear();

    }

    public List<CourseDataModel> CoursesList = new ArrayList<>();
    //    public Map<String, SemesterDataModel> SemesterMap = new LinkedHashMap<>();
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



    public void getUserSettings() {

        settingsReference = databaseReference.child("settings").child(UID);
        settingsReference.keepSynced(true);
        settingsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    UserSettings userSettings = dataSnapshot.getValue(UserSettings.class);
                    Constants.DEFAULT_START_TIME = new LocalTime().parse(userSettings.getStartTime());
                    Constants.DEFAULT_END_TIME = new LocalTime().parse(userSettings.getEndTime());
                    Constants.DEFAULT_BREAK_LENGTH = userSettings.getBreakBetweenClasses();
                    Constants.DEFAULT_CLASS_LENGTH = userSettings.getClassLength();
                    Constants.SELECTED_SEMESTER = userSettings.getSelectedSemester();
                    getSemesters();
                    getCourses();

                } else addNewUserToDatabase();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    public void getSemesters() {
//        SemesterMap.clear();
        semestersReference = databaseReference.child("semesters").child(UID);
        semestersReference.keepSynced(true);
        semestersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    SemesterDataModel semesterDataModel = snapshot.getValue(SemesterDataModel.class);
                    if (snapshot.getKey().equals(Constants.SELECTED_SEMESTER)) {
                        Constants.SELECTED_SEMESTER_NAME = semesterDataModel.getSemesterName();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        semestersReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                SemesterDataModel semesterDataModel = dataSnapshot.getValue(SemesterDataModel.class);
//                if (dataSnapshot.getKey().equals(Constants.SELECTED_SEMESTER)) {
//                    Constants.SELECTED_SEMESTER_NAME = semesterDataModel.getSemesterName();
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    public void getCourses() {

        coursesReference = databaseReference.child("courses").child(UID).child(SELECTED_SEMESTER);

        CoursesList.clear();


        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot courseDataSnapshot, String s) {
//                Object o = courseDataSnapshot.getValue();
//                Timber.d("courseDataSnapshot JSON" + new JSONObject((Map) o));
//                Timber.d("onChildAdded coursesRef" + courseDataSnapshot.toString());


                CourseDataModel courseDataModel = new CourseDataModel();
                courseDataModel.setCourseAbbreviation(courseDataSnapshot.child("courseAbbreviation").getValue().toString());
                courseDataModel.setCourseName(courseDataSnapshot.child("courseName").getValue().toString());
                courseDataModel.setCourseColorCode(((Long) courseDataSnapshot.child("courseColorCode").getValue()).intValue());
                if (courseDataSnapshot.child("instructor").getValue() != null)
                    courseDataModel.setInstructor(courseDataSnapshot.child("instructor").getValue().toString());
                if (courseDataSnapshot.child("creditHours").getValue() != null)
                    courseDataModel.setCreditHours(((Long) courseDataSnapshot.child("creditHours").getValue()).intValue());

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

        coursesReference.addChildEventListener(childEventListener);
        coursesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dashboardClassesFragment != null)
                    dashboardClassesFragment.adapter.setNewData(getListOfClassesForCurrentDay());
                coursesReference.removeEventListener(childEventListener);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        String ClassID = coursesReference.child(courseID).child("classes").push().getKey();
        coursesReference.child(courseID).child("classes").child(ClassID).setValue(classDataModel);


        CoursesID.get(courseID).getClasses().add(classDataModel);
        ClassesID.put(ClassID, classDataModel);
    }

    public void updateClass(String courseID, String classID, ClassDataModel classDataModel) {
        DatabaseReference reference = coursesReference.child(courseID).child("classes").child(classID);
        reference.setValue(classDataModel);

    }

    public void deleteClass(String courseID, String classID) {
        DatabaseReference reference = coursesReference.child(courseID).child("classes").child(classID);
        reference.removeValue();
        CoursesID.get(courseID).getClasses().remove(ClassesID.get(classID));
        ClassesID.remove(classID);

    }

    public void addCourse(CourseDataModel courseDataModel, List<ClassDataModel> classesList) {
        String courseID = coursesReference.push().getKey();
        coursesReference.child(courseID).setValue(courseDataModel);

//        courseDataModel.getClasses().addAll(classesList);
        CoursesList.add(courseDataModel);
        CoursesID.put(courseID, courseDataModel);


        for (ClassDataModel classDataModel : classesList) {


            addClassToCourse(courseID, classDataModel);

        }

    }

    public void deleteCourse(String courseID) {
        CourseDataModel courseDataModel = CoursesID.get(courseID);
        DatabaseReference reference = coursesReference.child(courseID);
        reference.removeValue();
        List<ClassDataModel> classes = courseDataModel.getClasses();
        CoursesList.remove(courseDataModel);
        CoursesID.remove(courseID);
        for (ClassDataModel classDataModel : classes) {
            ClassesID.remove(getClassID(classDataModel));
        }


    }

    public void updateCourse(CourseDataModel courseDataModel, String courseID) {
        CourseDataModel originalCourse = CoursesID.get(courseID);
        int loc = CoursesList.indexOf(originalCourse);
        CoursesList.set(loc, courseDataModel);
        CoursesID.put(courseID, courseDataModel);


        Map<String, Object> map = new HashMap<>();
        map.put("courseAbbreviation", courseDataModel.getCourseAbbreviation());
        map.put("courseColorCode", courseDataModel.getCourseColorCode());
        map.put("courseName", courseDataModel.getCourseName());
        map.put("creditHours", courseDataModel.getCreditHours());
        map.put("instructor", courseDataModel.getInstructor());

        coursesReference
                .child(courseID)
                .updateChildren(map);

    }

    public void updateTaskCompleted(QuizDataModel quizDataModel, boolean isCompleted) {
        getCourse(quizDataModel)
                .getQuizzes()
                .get(getCourse(quizDataModel)
                        .getQuizzes()
                        .indexOf(quizDataModel))
                .setCompleted(isCompleted);

        String quizID = getQuizID(quizDataModel);
        coursesReference
                .child(getCourseID(getCourse(quizDataModel)))
                .child("quizzes")
                .child(quizID)
                .child("completed")
                .setValue(isCompleted);
    }


    public void addNewUserToDatabase() {
        DatabaseReference newUserRef = databaseReference.child("settings").child(UID);

        UserSettings userSettings = new UserSettings();
        userSettings.setBreakBetweenClasses(10);
        userSettings.setClassLength(50);
        userSettings.setEndTime(new LocalTime(16, 30, 0).toString());
        userSettings.setStartTime(new LocalTime(9, 0, 0).toString());
        String defSemKey = databaseReference.child("courses").child(UID).push().getKey();
        userSettings.setSelectedSemester(defSemKey);
        Constants.SELECTED_SEMESTER = defSemKey;

        databaseReference.child("semesters").child(UID).child(defSemKey)
                .setValue(new SemesterDataModel("Default", new LocalDate().toString(), ""));

        newUserRef.setValue(userSettings);


    }


}
