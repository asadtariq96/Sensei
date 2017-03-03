package com.sensei.assistant.DataHandlers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sensei.assistant.Activities.Courses.CoursesListActivity;
import com.sensei.assistant.Activities.Dashboard.DashboardClassesFragment;
import com.sensei.assistant.Application.Constants;
import com.sensei.assistant.DataModelClasses.AssignmentDataModel;
import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.HomeworkDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.DataModelClasses.SemesterDataModel;
import com.sensei.assistant.DataModelClasses.UserSettings;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

import static com.sensei.assistant.Application.Constants.SELECTED_SEMESTER;
import static com.sensei.assistant.Application.MyApplication.UID;
import static com.sensei.assistant.Application.MyApplication.assignmentsReference;
import static com.sensei.assistant.Application.MyApplication.bus;
import static com.sensei.assistant.Application.MyApplication.classesReference;
import static com.sensei.assistant.Application.MyApplication.coursesReference;
import static com.sensei.assistant.Application.MyApplication.databaseReference;
import static com.sensei.assistant.Application.MyApplication.homeworkReference;
import static com.sensei.assistant.Application.MyApplication.quizzesReference;
import static com.sensei.assistant.Application.MyApplication.semestersReference;
import static com.sensei.assistant.Application.MyApplication.settingsReference;

/**
 * Created by Asad on 08-Jan-17.
 */

public class CourseDataHandler {

    private static CourseDataHandler instance = null;
    private ChildEventListener coursesChildEventListener;
    private ChildEventListener classesChildEventListener;
    private ChildEventListener quizzesChildEventListener;
    private ChildEventListener assignmentsChildEventListener;
    private ChildEventListener homeworkChildEventListener;

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
    public List<ClassDataModel> ClassesList = new ArrayList<>();
    public Map<String, ClassDataModel> ClassesID = new HashMap<>();
    public Map<String, QuizDataModel> QuizzesID = new HashMap<>();
    public Map<String, AssignmentDataModel> AssignmentsID = new HashMap<>();
    public Map<String, HomeworkDataModel> HomeworkID = new HashMap<>();

    public Map<String, CourseDataModel> CoursesID = new HashMap<>();

    private HashMap<DatabaseReference, ChildEventListener> childEventListenerHashMap = new HashMap<>();

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

        semestersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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


    }

    public void getCourses() {

        removeListeners();


        coursesReference = databaseReference.child("courses").child(SELECTED_SEMESTER);
        coursesReference.keepSynced(true);


        CoursesList.clear();


        classesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot classSnapShot, String s) {

                ClassDataModel classDataModel = classSnapShot.getValue(ClassDataModel.class);
                CoursesID.get(classSnapShot.getRef().getParent().getKey()).getClasses().add(classDataModel);
                ClassesID.put(classSnapShot.getKey(), classDataModel);
                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post classChildAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot classSnapShot, String s) {
                ClassDataModel classDataModel = classSnapShot.getValue(ClassDataModel.class);

                ClassDataModel oldClassDataModel = ClassesID.get(classSnapShot.getKey());

                int index = CoursesID.get(classSnapShot.getRef().getParent().getKey()).getClasses().indexOf(oldClassDataModel);

                CoursesID.get(classSnapShot.getRef().getParent().getKey()).getClasses().set(index, classDataModel);

                ClassesID.put(classSnapShot.getKey(), classDataModel);

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post classChildChanged");

            }

            @Override
            public void onChildRemoved(DataSnapshot classSnapShot) {


                String courseKey = classSnapShot.getRef().getParent().getKey();

                if (CoursesID.containsKey(courseKey)) {
                    CoursesID.get(classSnapShot.getRef().getParent().getKey()).getClasses().remove(ClassesID.get(classSnapShot.getKey()));
                }
                ClassesID.remove(classSnapShot.getKey());

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post classChildRemoved");

            }

            @Override
            public void onChildMoved(DataSnapshot classSnapShot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        quizzesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot quizSnapshot, String s) {

                QuizDataModel quizDataModel = quizSnapshot.getValue(QuizDataModel.class);
                CoursesID.get(quizSnapshot.getRef().getParent().getKey()).getQuizzes().add(quizDataModel);
                QuizzesID.put(quizSnapshot.getKey(), quizDataModel);
                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post quizAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot quizSnapshot, String s) {
                QuizDataModel quizDataModel = quizSnapshot.getValue(QuizDataModel.class);

                QuizDataModel oldQuizDataModel = QuizzesID.get(quizSnapshot.getKey());

                int index = CoursesID.get(quizSnapshot.getRef().getParent().getKey()).getQuizzes().indexOf(oldQuizDataModel);

                CoursesID.get(quizSnapshot.getRef().getParent().getKey()).getQuizzes().set(index, quizDataModel);

                QuizzesID.put(quizSnapshot.getKey(), quizDataModel);

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post quizChanged");

            }

            @Override
            public void onChildRemoved(DataSnapshot quizSnapshot) {


                String courseKey = quizSnapshot.getRef().getParent().getKey();

                if (CoursesID.containsKey(courseKey)) {
                    CoursesID.get(quizSnapshot.getRef().getParent().getKey()).getQuizzes().remove(QuizzesID.get(quizSnapshot.getKey()));
                }
                QuizzesID.remove(quizSnapshot.getKey());

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post quizRemoved");

            }

            @Override
            public void onChildMoved(DataSnapshot quizSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        assignmentsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot assignmentSnapshot, String s) {

                AssignmentDataModel assignmentDataModel = assignmentSnapshot.getValue(AssignmentDataModel.class);
                CoursesID.get(assignmentSnapshot.getRef().getParent().getKey()).getAssignments().add(assignmentDataModel);
                AssignmentsID.put(assignmentSnapshot.getKey(), assignmentDataModel);
                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post assgAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot assignmentSnapshot, String s) {
                AssignmentDataModel assignmentDataModel = assignmentSnapshot.getValue(AssignmentDataModel.class);

                AssignmentDataModel oldAssignmentDataModel = AssignmentsID.get(assignmentSnapshot.getKey());

                int index = CoursesID.get(assignmentSnapshot.getRef().getParent().getKey()).getAssignments().indexOf(oldAssignmentDataModel);

                CoursesID.get(assignmentSnapshot.getRef().getParent().getKey()).getAssignments().set(index, assignmentDataModel);

                AssignmentsID.put(assignmentSnapshot.getKey(), assignmentDataModel);

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post assgChanged");

            }

            @Override
            public void onChildRemoved(DataSnapshot assignmentSnapshot) {

                String courseKey = assignmentSnapshot.getRef().getParent().getKey();

                if (CoursesID.containsKey(courseKey)) {
                    CoursesID.get(assignmentSnapshot.getRef().getParent().getKey()).getAssignments().remove(AssignmentsID.get(assignmentSnapshot.getKey()));
                }
                AssignmentsID.remove(assignmentSnapshot.getKey());

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post assgRemoved");

            }

            @Override
            public void onChildMoved(DataSnapshot quizSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        homeworkChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot homeworkSnapshot, String s) {

                HomeworkDataModel homeworkDataModel = homeworkSnapshot.getValue(HomeworkDataModel.class);
                CoursesID.get(homeworkSnapshot.getRef().getParent().getKey()).getHomework().add(homeworkDataModel);
                HomeworkID.put(homeworkSnapshot.getKey(), homeworkDataModel);

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post homeworkAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot homeworkSnapshot, String s) {
                HomeworkDataModel homeworkDataModel = homeworkSnapshot.getValue(HomeworkDataModel.class);

                HomeworkDataModel oldHomeworkDataModel = HomeworkID.get(homeworkSnapshot.getKey());

                int index = CoursesID.get(homeworkSnapshot.getRef().getParent().getKey()).getHomework().indexOf(oldHomeworkDataModel);

                CoursesID.get(homeworkSnapshot.getRef().getParent().getKey()).getHomework().set(index, homeworkDataModel);

                HomeworkID.put(homeworkSnapshot.getKey(), homeworkDataModel);

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post homeworkChanged");

            }

            @Override
            public void onChildRemoved(DataSnapshot homeworkSnapshot) {

                String courseKey = homeworkSnapshot.getRef().getParent().getKey();

                if (CoursesID.containsKey(courseKey)) {
                    CoursesID.get(homeworkSnapshot.getRef().getParent().getKey()).getHomework().remove(HomeworkID.get(homeworkSnapshot.getKey()));
                }
                HomeworkID.remove(homeworkSnapshot.getKey());

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post homeworkRemoved");

            }

            @Override
            public void onChildMoved(DataSnapshot quizSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        coursesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot courseDataSnapshot, String s) {


                CourseDataModel courseDataModel = courseDataSnapshot.getValue(CourseDataModel.class);
                CoursesList.add(courseDataModel);
                CoursesID.put(courseDataSnapshot.getKey(), courseDataModel);
                databaseReference.child("classes").child(courseDataSnapshot.getKey()).addChildEventListener(classesChildEventListener);
                childEventListenerHashMap.put(databaseReference.child("classes").child(courseDataSnapshot.getKey()), classesChildEventListener);
                databaseReference.child("quizzes").child(courseDataSnapshot.getKey()).addChildEventListener(quizzesChildEventListener);
                childEventListenerHashMap.put(databaseReference.child("quizzes").child(courseDataSnapshot.getKey()), quizzesChildEventListener);
                databaseReference.child("assignments").child(courseDataSnapshot.getKey()).addChildEventListener(assignmentsChildEventListener);
                childEventListenerHashMap.put(databaseReference.child("assignments").child(courseDataSnapshot.getKey()), assignmentsChildEventListener);
                databaseReference.child("homework").child(courseDataSnapshot.getKey()).addChildEventListener(homeworkChildEventListener);
                childEventListenerHashMap.put(databaseReference.child("homework").child(courseDataSnapshot.getKey()), homeworkChildEventListener);

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post coursesChildAdded");

            }

            @Override
            public void onChildChanged(DataSnapshot courseDataSnapshot, String s) {
                CourseDataModel courseDataModel = courseDataSnapshot.getValue(CourseDataModel.class);

                CourseDataModel original = CoursesID.get(courseDataSnapshot.getKey());

                original.setCreditHours(courseDataModel.getCreditHours());
                original.setCourseName(courseDataModel.getCourseName());
                original.setInstructor(courseDataModel.getInstructor());
                original.setCourseAbbreviation(courseDataModel.getCourseAbbreviation());
                original.setCourseColorCode(courseDataModel.getCourseColorCode());

                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post coursesChildChanged");


            }

            @Override
            public void onChildRemoved(DataSnapshot courseDataSnapshot) {

                CourseDataModel courseDataModel = courseDataSnapshot.getValue(CourseDataModel.class);

//                List<ClassDataModel> classes = CoursesID.get(courseDataSnapshot.getKey()).getClasses();

                CoursesList.remove(CoursesID.get(courseDataSnapshot.getKey()));
                CoursesID.remove(courseDataSnapshot.getKey());

//                for (ClassDataModel classDataModel : classes) {
//                    ClassesID.remove(getClassID(classDataModel));
//                }
                bus.post(new DataChangedEvent(true));
                Timber.d("bus.post coursesChildRemoved");

            }

            @Override
            public void onChildMoved(DataSnapshot courseDataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        coursesReference.addChildEventListener(coursesChildEventListener);
        childEventListenerHashMap.put(coursesReference, coursesChildEventListener);
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

    public List<AssignmentDataModel> getListOfIncompleteAssignments() {
        List<AssignmentDataModel> tempList = new ArrayList<>();
        for (CourseDataModel courseDataModel : CoursesList)
            tempList.addAll(courseDataModel.getIncompleteAssignments());
        return tempList;
    }


    public List<HomeworkDataModel> getListOfHomework() {
        List<HomeworkDataModel> tempList = new ArrayList<>();
        for (CourseDataModel courseDataModel : CoursesList)
            tempList.addAll(courseDataModel.getHomework());
        return tempList;
    }

    public List<HomeworkDataModel> getListOfIncompleteHomework() {
        List<HomeworkDataModel> tempList = new ArrayList<>();
        for (CourseDataModel courseDataModel : CoursesList)
            tempList.addAll(courseDataModel.getIncompleteHomework());
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

    public CourseDataModel getCourse(HomeworkDataModel homeworkDataModel) {
        for (CourseDataModel courseDataModel : CoursesList) {
            if (courseDataModel.getHomework().contains(homeworkDataModel)) {
                return courseDataModel;
            }
        }
        return null;
    }

    public synchronized void addClassToCourse(String courseID, ClassDataModel classDataModel) {

        classesReference.child(courseID).push().setValue(classDataModel);

//        String ClassID = coursesReference.child(courseID).child("classes").push().getKey();
//        coursesReference.child(courseID).child("classes").child(ClassID).setValue(classDataModel);
//
//
//        CoursesID.get(courseID).getClasses().add(classDataModel);
//        ClassesID.put(ClassID, classDataModel);
    }

    public void updateClass(String courseID, String classID, ClassDataModel classDataModel) {
//        DatabaseReference reference = coursesReference.child(courseID).child("classes").child(classID);
//        reference.setValue(classDataModel);

        classesReference.child(courseID).child(classID).setValue(classDataModel);
    }

    public void deleteClass(String courseID, String classID) {
//        DatabaseReference reference = coursesReference.child(courseID).child("classes").child(classID);
//        reference.removeValue();
//        CoursesID.get(courseID).getClasses().remove(ClassesID.get(classID));
//        ClassesID.remove(classID);
        classesReference.child(courseID).child(classID).removeValue();


    }

    public void addCourse(CourseDataModel courseDataModel, List<ClassDataModel> classesList) {

        String courseID = coursesReference.push().getKey();
        coursesReference.child(courseID).setValue(courseDataModel);

//        String courseID = coursesReference.push().getKey();
//        coursesReference.child(courseID).setValue(courseDataModel);
//
//        CoursesList.add(courseDataModel);
//        CoursesID.put(courseID, courseDataModel);
//
//
        for (ClassDataModel classDataModel : classesList) {


            addClassToCourse(courseID, classDataModel);

        }

    }

    public void deleteCourse(String courseID) {

        coursesReference.child(courseID).removeValue();
        classesReference.child(courseID).removeValue();


//


    }

    public void updateCourse(CourseDataModel courseDataModel, String courseID) {

//        CourseDataModel originalCourse = CoursesID.get(courseID);
//        int loc = CoursesList.indexOf(originalCourse);
//        CoursesList.set(loc, courseDataModel);
//        CoursesID.put(courseID, courseDataModel);


        Map<String, Object> map = new HashMap<>();
        map.put("courseAbbreviation", courseDataModel.getCourseAbbreviation());
        map.put("courseColorCode", courseDataModel.getCourseColorCode());
        map.put("courseName", courseDataModel.getCourseName());
        map.put("creditHours", courseDataModel.getCreditHours());
        map.put("instructor", courseDataModel.getInstructor());

        coursesReference.child(courseID).updateChildren(map);


    }

    public void updateTaskCompleted(QuizDataModel quizDataModel, boolean isCompleted) {

        String quizID = getQuizID(quizDataModel);
        String courseID = getCourseID(getCourse(quizDataModel));
        quizzesReference.child(courseID).child(quizID).child("completed").setValue(isCompleted);
//        getCourse(quizDataModel)
//                .getQuizzes()
//                .get(getCourse(quizDataModel)
//                        .getQuizzes()
//                        .indexOf(quizDataModel))
//                .setCompleted(isCompleted);
//
//        String quizID = getQuizID(quizDataModel);
//        coursesReference
//                .child(getCourseID(getCourse(quizDataModel)))
//                .child("quizzes")
//                .child(quizID)
//                .child("completed")
//                .setValue(isCompleted);
    }

    public void addQuiz(CourseDataModel courseDataModel, QuizDataModel quizDataModel) {
        String courseID = getCourseID(courseDataModel);
        quizzesReference.child(courseID).push().setValue(quizDataModel);


    }

    public void addAssignment(CourseDataModel courseDataModel, AssignmentDataModel assignmentDataModel) {
        String courseID = getCourseID(courseDataModel);
        assignmentsReference.child(courseID).push().setValue(assignmentDataModel);


    }

    public void addHomework(CourseDataModel courseDataModel, HomeworkDataModel homeworkDataModel) {
        String courseID = getCourseID(courseDataModel);
        homeworkReference.child(courseID).push().setValue(homeworkDataModel);


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

    public void removeListeners() {
        for (Map.Entry<DatabaseReference, ChildEventListener> entry : childEventListenerHashMap.entrySet()) {
            DatabaseReference ref = entry.getKey();
            ChildEventListener listener = entry.getValue();
            ref.removeEventListener(listener);
        }
    }


    public static class DataChangedEvent {
        boolean var;

        public DataChangedEvent(boolean var) {
            this.var = var;
        }

        public boolean isVar() {
            return var;
        }

        public void setVar(boolean var) {
            this.var = var;
        }
    }


}
